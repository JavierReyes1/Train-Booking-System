package com.trainbooking.gui;

import com.trainbooking.model.Seat;
import com.trainbooking.model.Train;
import com.trainbooking.model.Booking;
import com.trainbooking.service.BookingService;
import com.trainbooking.service.TrainService;
import com.trainbooking.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

public class SeatSelectionPanel extends JPanel {

	private MainFrame mainFrame;
	private TrainService trainService;
	private BookingService bookingService;
	private UserService userService;

	private Train currentTrain;
	private Seat selectedSeat;
	private JButton selectedButton;
	private List<JButton> seatButtons;
	private List<Seat> currentSeats;

	private JLabel trainInfoLabel;
	private JLabel selectedSeatLabel;
	private JLabel priceLabel;
	private JLabel messageLabel;
	private JPanel seatGridPanel;
	private JScrollPane seatScrollPane;

	// seat colours
	private static final Color GREEN = new Color(60, 180, 80);
	private static final Color RED = new Color(200, 60, 60);
	private static final Color BLUE = new Color(30, 80, 200);
	private static final Color GOLD = new Color(180, 140, 0);
	private static final Color LBLUE = new Color(80, 140, 200);

	public SeatSelectionPanel(MainFrame mainFrame, TrainService trainService,
			BookingService bookingService, UserService userService) {
		this.mainFrame = mainFrame;
		this.trainService = trainService;
		this.bookingService = bookingService;
		this.userService = userService;
		this.seatButtons = new ArrayList<JButton>();
		this.currentSeats = new ArrayList<Seat>();

		setLayout(new BorderLayout(0, 0));
		setBackground(new Color(240, 244, 255));
		add(buildTopSection(), BorderLayout.NORTH);
		add(buildCentreSection(), BorderLayout.CENTER);
		add(buildBottomSection(), BorderLayout.SOUTH);
	}

	private JPanel buildTopSection() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(30, 80, 160));
		panel.setBorder(new EmptyBorder(15, 30, 15, 30));

		JLabel titleLabel = new JLabel("Select Your Seat");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(Color.WHITE);

		trainInfoLabel = new JLabel("No train selected");
		trainInfoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		trainInfoLabel.setForeground(new Color(200, 220, 255));

		JButton backButton = new JButton("< Back to Search");
		backButton.setBackground(new Color(255, 255, 255, 60));
		backButton.setForeground(Color.WHITE);
		backButton.setFont(new Font("Arial", Font.PLAIN, 13));
		backButton.setFocusPainted(false);
		backButton.setBorderPainted(false);
		backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.showScreen("SEARCH");
			}
		});

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(new Color(30, 80, 160));
		leftPanel.add(titleLabel);
		leftPanel.add(Box.createVerticalStrut(4));
		leftPanel.add(trainInfoLabel);

		panel.add(leftPanel, BorderLayout.WEST);
		panel.add(backButton, BorderLayout.EAST);
		return panel;
	}

	private JPanel buildCentreSection() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(new Color(240, 244, 255));
		wrapper.setBorder(new EmptyBorder(15, 30, 10, 30));

		// legend
		JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		legend.setBackground(new Color(240, 244, 255));
		legend.add(makeLegendItem(GREEN, "Available (Standard)"));
		legend.add(makeLegendItem(GOLD, "Available (First)"));
		legend.add(makeLegendItem(LBLUE, "Available (Second)"));
		legend.add(makeLegendItem(RED, "Booked"));
		legend.add(makeLegendItem(BLUE, "Your Selection"));
		legend.setBorder(new EmptyBorder(0, 0, 10, 0));

		seatGridPanel = new JPanel();
		seatGridPanel.setBackground(new Color(250, 252, 255));

		seatScrollPane = new JScrollPane(seatGridPanel);
		seatScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		seatScrollPane.getVerticalScrollBar().setUnitIncrement(16);

		wrapper.add(legend, BorderLayout.NORTH);
		wrapper.add(seatScrollPane, BorderLayout.CENTER);
		return wrapper;
	}

	private JPanel buildBottomSection() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
				new EmptyBorder(12, 30, 12, 30)));

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(Color.WHITE);

		selectedSeatLabel = new JLabel("No seat selected");
		selectedSeatLabel.setFont(new Font("Arial", Font.BOLD, 14));
		selectedSeatLabel.setForeground(new Color(60, 60, 60));

		priceLabel = new JLabel(" ");
		priceLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		priceLabel.setForeground(new Color(30, 80, 160));

		messageLabel = new JLabel(" ");
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		infoPanel.add(selectedSeatLabel);
		infoPanel.add(Box.createVerticalStrut(3));
		infoPanel.add(priceLabel);
		infoPanel.add(Box.createVerticalStrut(3));
		infoPanel.add(messageLabel);

		JButton confirmButton = new JButton("Confirm Booking");
		confirmButton.setBackground(new Color(0, 150, 80));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFont(new Font("Arial", Font.BOLD, 15));
		confirmButton.setPreferredSize(new Dimension(200, 42));
		confirmButton.setFocusPainted(false);
		confirmButton.setBorderPainted(false);
		confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleConfirmBooking();
			}
		});

		panel.add(infoPanel, BorderLayout.WEST);
		panel.add(confirmButton, BorderLayout.EAST);
		return panel;
	}

	// called by MainFrame when user picks a train
	public void loadTrain(Train train) {
		this.currentTrain = train;
		this.selectedSeat = null;
		this.selectedButton = null;

		trainInfoLabel.setText(train.getTrainNumber() + "  " + train.getTrainName() +
				"   " + train.getOrigin() + "  to  " + train.getDestination() +
				"   Departs: " + train.getFormattedDeparture());

		selectedSeatLabel.setText("No seat selected");
		priceLabel.setText(" ");
		messageLabel.setText(" ");

		currentSeats = trainService.getSeatsForTrain(train.getId());
		seatButtons = new ArrayList<JButton>();
		buildSeatGrid();
	}

	private void buildSeatGrid() {
		seatGridPanel.removeAll();

		int totalSeats = currentSeats.size();
		int numRows = (int) Math.ceil(totalSeats / 4.0);

		// 5 columns: row label + 4 seats (A B C D)
		seatGridPanel.setLayout(new GridLayout(numRows + 1, 5, 6, 6));
		seatGridPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

		// header
		seatGridPanel.add(new JLabel(""));
		String[] colLabels = { "A", "B", "C", "D" };
		for (int c = 0; c < colLabels.length; c++) {
			JLabel lbl = new JLabel(colLabels[c], SwingConstants.CENTER);
			lbl.setFont(new Font("Arial", Font.BOLD, 13));
			lbl.setForeground(new Color(80, 80, 80));
			seatGridPanel.add(lbl);
		}

		for (int row = 0; row < numRows; row++) {
			JLabel rowLabel = new JLabel(String.valueOf(row + 1), SwingConstants.CENTER);
			rowLabel.setFont(new Font("Arial", Font.BOLD, 12));
			rowLabel.setForeground(new Color(100, 100, 100));
			seatGridPanel.add(rowLabel);

			for (int col = 0; col < 4; col++) {
				int seatIndex = row * 4 + col;
				if (seatIndex < currentSeats.size()) {
					Seat seat = currentSeats.get(seatIndex);
					JButton btn = buildSeatButton(seat);
					seatButtons.add(btn);
					seatGridPanel.add(btn);
				} else {
					seatGridPanel.add(new JLabel(""));
				}
			}
		}
		seatGridPanel.revalidate();
		seatGridPanel.repaint();
	}

	private JButton buildSeatButton(Seat seat) {
		JButton btn = new JButton(seat.getSeatNumber());
		btn.setFont(new Font("Arial", Font.BOLD, 11));
		btn.setPreferredSize(new Dimension(58, 40));
		btn.setFocusPainted(false);
		btn.setBorderPainted(true);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setForeground(Color.WHITE);

		if (seat.isBooked()) {
			btn.setBackground(RED);
			btn.setEnabled(false);
			btn.setToolTipText(seat.getSeatNumber() + " - booked");
		} else {
			// colour by class
			if (seat.getSeatClass().equals("FIRST"))
				btn.setBackground(GOLD);
			else if (seat.getSeatClass().equals("SECOND"))
				btn.setBackground(LBLUE);
			else
				btn.setBackground(GREEN);

			btn.setToolTipText(seat.getSeatNumber() + " - " + seat.getSeatClass());
			final Seat clickedSeat = seat;
			final JButton clickedBtn = btn;
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					handleSeatClick(clickedSeat, clickedBtn);
				}
			});
		}
		return btn;
	}

	private void handleSeatClick(Seat seat, JButton button) {
		// deselect previous
		if (selectedButton != null && selectedSeat != null) {
			if (selectedSeat.getSeatClass().equals("FIRST"))
				selectedButton.setBackground(GOLD);
			else if (selectedSeat.getSeatClass().equals("SECOND"))
				selectedButton.setBackground(LBLUE);
			else
				selectedButton.setBackground(GREEN);
		}

		// toggle off if clicking same seat
		if (selectedSeat != null && selectedSeat.getId() == seat.getId()) {
			selectedSeat = null;
			selectedButton = null;
			selectedSeatLabel.setText("No seat selected");
			priceLabel.setText(" ");
			messageLabel.setText(" ");
			return;
		}

		selectedSeat = seat;
		selectedButton = button;
		button.setBackground(BLUE);

		double price = bookingService.calculatePrice(currentTrain.getPricePerSeat(), seat.getSeatClass());
		selectedSeatLabel.setText("Selected: Seat " + seat.getSeatNumber() + "  (" + seat.getSeatClass() + " class)");
		priceLabel.setText("Price: EUR " + String.format("%.2f", price));
		messageLabel.setText(" ");
	}

	private void handleConfirmBooking() {
		if (selectedSeat == null) {
			messageLabel.setForeground(Color.RED);
			messageLabel.setText("Pick a seat first.");
			return;
		}

		int userId = userService.getLoggedInUser().getId();
		double price = bookingService.calculatePrice(currentTrain.getPricePerSeat(), selectedSeat.getSeatClass());

		int choice = JOptionPane.showConfirmDialog(this,
				"Book seat " + selectedSeat.getSeatNumber() +
						" (" + selectedSeat.getSeatClass() + ")" +
						"\non " + currentTrain.getTrainNumber() + " - " + currentTrain.getTrainName() +
						"\nDeparting: " + currentTrain.getFormattedDeparture() +
						"\n\nPrice: EUR " + String.format("%.2f", price) +
						"\n\nConfirm?",
				"Confirm Booking", JOptionPane.YES_NO_OPTION);

		if (choice != JOptionPane.YES_OPTION)
			return;

		Booking booking = bookingService.bookSeat(userId, currentTrain, selectedSeat);

		if (booking != null) {
			JOptionPane.showMessageDialog(this,
					"Booking confirmed!\nRef: #" + booking.getId() +
							"\nSeat: " + selectedSeat.getSeatNumber() +
							" (" + selectedSeat.getSeatClass() + ")" +
							"\nTrain: " + currentTrain.getTrainNumber() +
							"\nTotal: EUR " + String.format("%.2f", booking.getTotalPrice()),
					"Confirmed", JOptionPane.INFORMATION_MESSAGE);
			loadTrain(currentTrain); // refresh
		} else {
			JOptionPane.showMessageDialog(this,
					"Booking failed. Seat might have been taken.\nTry another one.",
					"Failed", JOptionPane.ERROR_MESSAGE);
			loadTrain(currentTrain);
		}
	}

	private JPanel makeLegendItem(Color colour, String label) {
		JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		item.setBackground(new Color(240, 244, 255));

		JPanel colourBox = new JPanel();
		colourBox.setBackground(colour);
		colourBox.setPreferredSize(new Dimension(18, 18));
		colourBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

		JLabel text = new JLabel(label);
		text.setFont(new Font("Arial", Font.PLAIN, 12));

		item.add(colourBox);
		item.add(text);
		return item;
	}
}
