package com.trainbooking.gui;

import com.trainbooking.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// login and register screen with tabs
public class LoginPanel extends JPanel {

	private MainFrame mainFrame;
	private UserService userService;

	private JTextField loginUsernameField;
	private JPasswordField loginPasswordField;
	private JLabel loginMessageLabel;

	private JTextField regUsernameField;
	private JPasswordField regPasswordField;
	private JPasswordField regConfirmPasswordField;
	private JTextField regFullNameField;
	private JTextField regEmailField;
	private JLabel regMessageLabel;

	public LoginPanel(MainFrame mainFrame, UserService userService) {
		this.mainFrame = mainFrame;
		this.userService = userService;

		setLayout(new BorderLayout());
		setBackground(new Color(240, 244, 255));

		JLabel titleLabel = new JLabel("Welcome to Train Booking", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
		titleLabel.setForeground(new Color(30, 80, 160));
		titleLabel.setBorder(new EmptyBorder(40, 0, 20, 0));
		add(titleLabel, BorderLayout.NORTH);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
		tabbedPane.addTab("Login", buildLoginTab());
		tabbedPane.addTab("Register", buildRegisterTab());

		JPanel centreWrapper = new JPanel(new GridBagLayout());
		centreWrapper.setBackground(new Color(240, 244, 255));
		tabbedPane.setPreferredSize(new Dimension(420, 620));
		centreWrapper.add(tabbedPane);
		add(centreWrapper, BorderLayout.CENTER);
	}

	private JPanel buildLoginTab() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(20, 30, 20, 30));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridx = 0;
		gbc.gridwidth = 2;

		gbc.gridy = 0;
		panel.add(makeLabel("Username:"), gbc);
		gbc.gridy = 1;
		loginUsernameField = new JTextField();
		styleTextField(loginUsernameField);
		panel.add(loginUsernameField, gbc);

		gbc.gridy = 2;
		panel.add(makeLabel("Password:"), gbc);
		gbc.gridy = 3;
		loginPasswordField = new JPasswordField();
		styleTextField(loginPasswordField);
		panel.add(loginPasswordField, gbc);

		gbc.gridy = 4;
		loginMessageLabel = new JLabel(" ");
		loginMessageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		loginMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(loginMessageLabel, gbc);

		gbc.gridy = 5;
		JButton loginButton = new JButton("Login");
		stylePrimaryButton(loginButton);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleLogin();
			}
		});
		panel.add(loginButton, gbc);

		loginPasswordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleLogin();
			}
		});
		return panel;
	}

	private JPanel buildRegisterTab() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(20, 30, 20, 30));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridwidth = 2;
		gbc.gridx = 0;

		gbc.gridy = 0;
		panel.add(makeLabel("Full Name:"), gbc);
		gbc.gridy = 1;
		regFullNameField = new JTextField();
		styleTextField(regFullNameField);
		panel.add(regFullNameField, gbc);

		gbc.gridy = 2;
		panel.add(makeLabel("Email:"), gbc);
		gbc.gridy = 3;
		regEmailField = new JTextField();
		styleTextField(regEmailField);
		panel.add(regEmailField, gbc);

		gbc.gridy = 4;
		panel.add(makeLabel("Username:"), gbc);
		gbc.gridy = 5;
		regUsernameField = new JTextField();
		styleTextField(regUsernameField);
		panel.add(regUsernameField, gbc);

		gbc.gridy = 6;
		panel.add(makeLabel("Password:"), gbc);
		gbc.gridy = 7;
		regPasswordField = new JPasswordField();
		styleTextField(regPasswordField);
		panel.add(regPasswordField, gbc);

		gbc.gridy = 8;
		panel.add(makeLabel("Confirm Password:"), gbc);
		gbc.gridy = 9;
		regConfirmPasswordField = new JPasswordField();
		styleTextField(regConfirmPasswordField);
		panel.add(regConfirmPasswordField, gbc);

		gbc.gridy = 10;
		regMessageLabel = new JLabel(" ");
		regMessageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		regMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(regMessageLabel, gbc);

		gbc.gridy = 11;
		JButton registerButton = new JButton("Create Account");
		stylePrimaryButton(registerButton);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleRegister();
			}
		});
		panel.add(registerButton, gbc);

		return panel;
	}

	private void handleLogin() {
		String username = loginUsernameField.getText();
		String password = new String(loginPasswordField.getPassword());
		String result = userService.login(username, password);

		if (result.equals("OK")) {
			loginMessageLabel.setForeground(new Color(0, 150, 0));
			loginMessageLabel.setText("Login successful!");
			mainFrame.onLoginSuccess();
		} else {
			loginMessageLabel.setForeground(Color.RED);
			loginMessageLabel.setText(result);
		}
	}

	private void handleRegister() {
		String username = regUsernameField.getText();
		String password = new String(regPasswordField.getPassword());
		String confirmPassword = new String(regConfirmPasswordField.getPassword());
		String fullName = regFullNameField.getText();
		String email = regEmailField.getText();

		String result = userService.register(username, password, confirmPassword, fullName, email);

		if (result.equals("OK")) {
			regMessageLabel.setForeground(new Color(0, 150, 0));
			regMessageLabel.setText("Account created! You can now log in.");
			clearRegisterFields();
			regMessageLabel.setText("Account created! You can now log in.");
		} else {
			regMessageLabel.setForeground(Color.RED);
			regMessageLabel.setText(result);
		}
	}

	private JLabel makeLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 13));
		label.setForeground(new Color(60, 60, 60));
		return label;
	}

	private void styleTextField(JTextField field) {
		field.setFont(new Font("Arial", Font.PLAIN, 13));
		field.setPreferredSize(new Dimension(300, 32));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(180, 180, 180)),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));
	}

	private void stylePrimaryButton(JButton button) {
		button.setBackground(new Color(30, 80, 160));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setPreferredSize(new Dimension(200, 38));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public void clearFields() {
		loginUsernameField.setText("");
		loginPasswordField.setText("");
		loginMessageLabel.setText(" ");
		clearRegisterFields();
	}

	private void clearRegisterFields() {
		regUsernameField.setText("");
		regPasswordField.setText("");
		regConfirmPasswordField.setText("");
		regFullNameField.setText("");
		regEmailField.setText("");
		regMessageLabel.setText(" ");
	}
}
