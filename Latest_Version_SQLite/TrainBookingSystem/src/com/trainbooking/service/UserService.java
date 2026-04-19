package com.trainbooking.service;

import com.trainbooking.db.DatabaseManager;
import com.trainbooking.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserService {

	private DatabaseManager db;
	private User loggedInUser;

	public UserService() {
		db = DatabaseManager.getInstance();
	}

	// register a new user, returns "OK" or error message
	public String register(String username, String password, String confirmPassword,
			String fullName, String email) {

		if (username == null || username.trim().equals(""))
			return "Username is required.";
		if (password == null || password.trim().equals(""))
			return "Password is required.";
		if (fullName == null || fullName.trim().equals(""))
			return "Full name is required.";
		if (email == null || email.trim().equals(""))
			return "Email is required.";
		if (username.trim().length() < 3)
			return "Username must be at least 3 characters.";
		if (password.length() < 6)
			return "Password must be at least 6 characters.";
		if (!password.equals(confirmPassword))
			return "Passwords do not match.";
		if (!email.contains("@"))
			return "Invalid email.";

		String passwordHash = hashPassword(password);
		if (passwordHash == null)
			return "Error hashing password.";

		try {
			boolean success = db.createUser(username.trim(), passwordHash,
					fullName.trim(), email.trim());
			if (success) {
				return "OK";
			} else {
				return "Username or email already taken.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Database error: " + e.getMessage();
		}
	}

	public String login(String username, String password) {
		if (username == null || username.trim().equals(""))
			return "Enter your username.";
		if (password == null || password.trim().equals(""))
			return "Enter your password.";

		try {
			User user = db.getUserByUsername(username.trim());
			if (user == null)
				return "Username not found.";

			String passwordHash = hashPassword(password);
			if (passwordHash == null)
				return "Error checking password.";
			if (!user.getPasswordHash().equals(passwordHash))
				return "Wrong password.";

			loggedInUser = user;
			return "OK";

		} catch (SQLException e) {
			e.printStackTrace();
			return "Database error: " + e.getMessage();
		}
	}

	public void logout() {
		loggedInUser = null;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public boolean isLoggedIn() {
		return loggedInUser != null;
	}

	// hash password with SHA-256
	private String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hashBytes.length; i++) {
				String hex = Integer.toHexString(0xff & hashBytes[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
