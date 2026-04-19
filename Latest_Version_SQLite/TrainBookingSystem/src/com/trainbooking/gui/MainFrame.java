package com.trainbooking.gui;

import com.trainbooking.service.BookingService;
import com.trainbooking.service.TrainService;
import com.trainbooking.service.UserService;
import com.trainbooking.model.Train;
import com.trainbooking.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// main window - uses CardLayout to switch between screens
public class MainFrame extends JFrame {

	private CardLayout cardLayout;
	private JPanel cardPanel;

	private LoginPanel loginPanel;
	private SearchPanel searchPanel;
	private SeatSelectionPanel seatSelectionPanel;
	private BookingsPanel bookingsPanel;

	private UserService userService;
	private TrainService trainService;
	private BookingService bookingService;

	private JPanel navBar;
	private JLabel welcomeLabel;

	public MainFrame() {
		userService = new UserService();
		trainService = new TrainService();
		bookingService = new BookingService();

		setTitle("Train Booking System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 650);
		setMinimumSize(new Dimension(800, 600));
		setLocationRelativeTo(null);

		buildNavBar();
		buildCardPanel();

		setLayout(new BorderLayout());
		add(navBar, BorderLayout.NORTH);
		add(cardPanel, BorderLayout.CENTER);

		showScreen("LOGIN");
	}

	private void buildNavBar() {
		navBar = new JPanel(new BorderLayout());
		navBar.setBackground(new Color(30, 80, 160));
		navBar.setPreferredSize(new Dimension(900, 50));

		JLabel titleLabel = new JLabel("  Train Booking System");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(Color.WHITE);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		rightPanel.setBackground(new Color(30, 80, 160));

		welcomeLabel = new JLabel("");
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 13));

		JButton searchBtn = new JButton("Search Trains");
		JButton bookingsBtn = new JButton("My Bookings");
		JButton logoutBtn = new JButton("Logout");

		styleNavButton(searchBtn);
		styleNavButton(bookingsBtn);
		styleNavButton(logoutBtn);

		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showScreen("SEARCH");
			}
		});
		bookingsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookingsPanel.loadBookings();
				showScreen("BOOKINGS");
			}
		});
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});

		rightPanel.add(welcomeLabel);
		rightPanel.add(searchBtn);
		rightPanel.add(bookingsBtn);
		rightPanel.add(logoutBtn);

		navBar.add(titleLabel, BorderLayout.WEST);
		navBar.add(rightPanel, BorderLayout.EAST);
		navBar.setVisible(false);
	}

	private void styleNavButton(JButton button) {
		button.setBackground(new Color(255, 255, 255, 50));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.PLAIN, 13));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	private void buildCardPanel() {
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		loginPanel = new LoginPanel(this, userService);
		searchPanel = new SearchPanel(this, trainService);
		seatSelectionPanel = new SeatSelectionPanel(this, trainService, bookingService, userService);
		bookingsPanel = new BookingsPanel(this, bookingService, userService);

		cardPanel.add(loginPanel, "LOGIN");
		cardPanel.add(searchPanel, "SEARCH");
		cardPanel.add(seatSelectionPanel, "SEATS");
		cardPanel.add(bookingsPanel, "BOOKINGS");
	}

	public void showScreen(String screenName) {
		cardLayout.show(cardPanel, screenName);
	}

	public void onLoginSuccess() {
		User user = userService.getLoggedInUser();
		welcomeLabel.setText("Hello, " + user.getFullName() + "  |  ");
		navBar.setVisible(true);
		searchPanel.loadStations();
		showScreen("SEARCH");
	}

	public void goToSeatSelection(Train train) {
		seatSelectionPanel.loadTrain(train);
		showScreen("SEATS");
	}

	private void logout() {
		int choice = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to log out?", "Logout",
				JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			userService.logout();
			navBar.setVisible(false);
			welcomeLabel.setText("");
			loginPanel.clearFields();
			showScreen("LOGIN");
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public TrainService getTrainService() {
		return trainService;
	}

	public BookingService getBookingService() {
		return bookingService;
	}
}
