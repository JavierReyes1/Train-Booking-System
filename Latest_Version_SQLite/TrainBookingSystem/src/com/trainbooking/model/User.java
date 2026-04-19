package com.trainbooking.model;

public class User {
	private int id;
	private String username;
	private String passwordHash;
	private String fullName;
	private String email;

	public User() {
	}

	public User(int id, String username, String passwordHash, String fullName, String email) {
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;
		this.fullName = fullName;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User{id=" + id + ", username='" + username + "'}";
	}
}
