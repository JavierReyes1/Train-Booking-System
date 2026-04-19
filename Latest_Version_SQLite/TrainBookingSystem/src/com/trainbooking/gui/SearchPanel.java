package com.trainbooking.gui;

import com.trainbooking.model.Train;
import com.trainbooking.service.TrainService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class SearchPanel extends JPanel {

	private MainFrame mainFrame;
	private TrainService trainService;

	private JComboBox<String> originBox;
	private JComboBox<String> destinationBox;
	private JTextField dateField;
	private JLabel messageLabel;

	private JTable resultsTable;
	private DefaultTableModel tableModel;
	private List<Train> currentResults;

	private static final String[] COLUMNS = {
			"Train No.", "Train Name", "From", "To", "Departs", "Arrives", "Available Seats", "Price"
	};

	public SearchPanel(MainFrame mainFrame, TrainService trainService) {
		this.mainFrame = mainFrame;
		this.trainService = trainService;
		this.currentResults = new ArrayList<Train>();

		setLayout(new BorderLayout(0, 0));
		setBackground(new Color(240, 244, 255));
		add(buildTopSection(), BorderLayout.NORTH);
		add(buildResultsSection(), BorderLayout.CENTER);
	}

	private JPanel buildTopSection() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(240, 244, 255));
		topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));

		JLabel titleLabel = new JLabel("Search Trains");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
		titleLabel.setForeground(new Color(30, 80, 160));
		titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

		// search form
		JPanel formRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		formRow.setBackground(new Color(240, 244, 255));

		formRow.add(makeLabel("From:"));
		originBox = new JComboBox<String>();
		originBox.setPreferredSize(new Dimension(140, 32));
		originBox.setFont(new Font("Arial", Font.PLAIN, 13));
		formRow.add(originBox);

		formRow.add(makeLabel("To:"));
		destinationBox = new JComboBox<String>();
		destinationBox.setPreferredSize(new Dimension(140, 32));
		destinationBox.setFont(new Font("Arial", Font.PLAIN, 13));
		formRow.add(destinationBox);

		formRow.add(makeLabel("Date (yyyy-MM-dd):"));
		dateField = new JTextField(12);
		dateField.setFont(new Font("Arial", Font.PLAIN, 13));
		dateField.setPreferredSize(new Dimension(130, 32));
		dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		formRow.add(dateField);

		JButton searchButton = new JButton("Search");
		searchButton.setBackground(new Color(30, 80, 160));
		searchButton.setForeground(Color.WHITE);
		searchButton.setFont(new Font("Arial", Font.BOLD, 13));
		searchButton.setPreferredSize(new Dimension(100, 32));
		searchButton.setFocusPainted(false);
		searchButton.setBorderPainted(false);
		searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSearch();
			}
		});
		formRow.add(searchButton);

		messageLabel = new JLabel(" ");
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		topPanel.add(titleLabel, BorderLayout.NORTH);
		topPanel.add(formRow, BorderLayout.CENTER);
		topPanel.add(messageLabel, BorderLayout.SOUTH);
		return topPanel;
	}

	private JPanel buildResultsSection() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(240, 244, 255));
		panel.setBorder(new EmptyBorder(0, 30, 20, 30));

		tableModel = new DefaultTableModel(COLUMNS, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		resultsTable = new JTable(tableModel);
		resultsTable.setFont(new Font("Arial", Font.PLAIN, 13));
		resultsTable.setRowHeight(30);
		resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
		resultsTable.getTableHeader().setBackground(new Color(30, 80, 160));
		resultsTable.getTableHeader().setForeground(Color.WHITE);
		resultsTable.setGridColor(new Color(220, 220, 220));
		resultsTable.setSelectionBackground(new Color(180, 210, 255));

		// centre align all columns
		DefaultTableCellRenderer centreRenderer = new DefaultTableCellRenderer();
		centreRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < COLUMNS.length; i++) {
			resultsTable.getColumnModel().getColumn(i).setCellRenderer(centreRenderer);
		}

		int[] widths = { 80, 160, 90, 90, 140, 140, 110, 80 };
		for (int i = 0; i < widths.length; i++) {
			resultsTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
		}

		JScrollPane scrollPane = new JScrollPane(resultsTable);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

		// bottom bar
		JPanel bottomBar = new JPanel(new BorderLayout());
		bottomBar.setBackground(new Color(240, 244, 255));
		bottomBar.setBorder(new EmptyBorder(10, 0, 0, 0));

		JLabel hintLabel = new JLabel("Select a train from the table, then click Select Seats.");
		hintLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		hintLabel.setForeground(new Color(100, 100, 100));

		JButton selectButton = new JButton("Select Seats  >");
		selectButton.setBackground(new Color(0, 150, 80));
		selectButton.setForeground(Color.WHITE);
		selectButton.setFont(new Font("Arial", Font.BOLD, 14));
		selectButton.setPreferredSize(new Dimension(160, 38));
		selectButton.setFocusPainted(false);
		selectButton.setBorderPainted(false);
		selectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSelectTrain();
			}
		});

		bottomBar.add(hintLabel, BorderLayout.WEST);
		bottomBar.add(selectButton, BorderLayout.EAST);

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(bottomBar, BorderLayout.SOUTH);
		return panel;
	}

	private void handleSearch() {
		String origin = (String) originBox.getSelectedItem();
		String destination = (String) destinationBox.getSelectedItem();
		String date = dateField.getText().trim();

		if (origin == null || origin.equals("")) {
			showMessage("Select origin.", Color.RED);
			return;
		}
		if (destination == null || destination.equals("")) {
			showMessage("Select destination.", Color.RED);
			return;
		}
		if (origin.equals(destination)) {
			showMessage("Origin and destination can't be the same.", Color.RED);
			return;
		}
		if (date.equals("")) {
			showMessage("Enter a date.", Color.RED);
			return;
		}

		List<Train> results = trainService.searchTrains(origin, destination, date);
		currentResults = results;
		populateTable(results);

		if (results.size() == 0) {
			showMessage("No trains found.", new Color(180, 100, 0));
		} else {
			showMessage("Found " + results.size() + " train(s).", new Color(0, 130, 0));
		}
	}

	private void handleSelectTrain() {
		int selectedRow = resultsTable.getSelectedRow();
		if (selectedRow == -1) {
			showMessage("Select a train first.", Color.RED);
			return;
		}
		if (selectedRow >= currentResults.size()) {
			showMessage("Invalid selection.", Color.RED);
			return;
		}

		Train chosenTrain = currentResults.get(selectedRow);
		mainFrame.goToSeatSelection(chosenTrain);
	}

	private void populateTable(List<Train> trains) {
		tableModel.setRowCount(0);
		for (int i = 0; i < trains.size(); i++) {
			Train t = trains.get(i);
			int available = trainService.getAvailableSeatCount(t.getId());
			Object[] row = {
					t.getTrainNumber(), t.getTrainName(), t.getOrigin(), t.getDestination(),
					t.getFormattedDeparture(), t.getFormattedArrival(),
					available + " / " + t.getTotalSeats(),
					"EUR " + String.format("%.2f", t.getPricePerSeat())
			};
			tableModel.addRow(row);
		}
	}

	// load stations into dropdowns
	public void loadStations() {
		originBox.removeAllItems();
		destinationBox.removeAllItems();

		List<String> origins = trainService.getOrigins();
		for (int i = 0; i < origins.size(); i++) {
			originBox.addItem(origins.get(i));
			destinationBox.addItem(origins.get(i));
		}

		List<String> destinations = trainService.getDestinations();
		for (int i = 0; i < destinations.size(); i++) {
			boolean alreadyAdded = false;
			for (int j = 0; j < destinationBox.getItemCount(); j++) {
				if (destinationBox.getItemAt(j).equals(destinations.get(i))) {
					alreadyAdded = true;
					break;
				}
			}
			if (!alreadyAdded)
				destinationBox.addItem(destinations.get(i));
		}
	}

	private void showMessage(String text, Color colour) {
		messageLabel.setText(text);
		messageLabel.setForeground(colour);
	}

	private JLabel makeLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 13));
		label.setForeground(new Color(60, 60, 60));
		return label;
	}
}
