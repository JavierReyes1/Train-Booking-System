package com.trainbooking.gui;

import com.trainbooking.model.Booking;
import com.trainbooking.service.BookingService;
import com.trainbooking.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

public class BookingsPanel extends JPanel {

    private MainFrame mainFrame;
    private BookingService bookingService;
    private UserService userService;

    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private List<Booking> currentBookings;

    private JLabel titleLabel;
    private JLabel messageLabel;
    private JLabel summaryLabel;

    private JButton filterAllBtn;
    private JButton filterConfirmedBtn;
    private JButton filterCancelledBtn;
    private String currentFilter;

    private static final String[] COLUMNS = {
        "Ref #", "Train", "Route", "Departure", "Seat", "Class", "Price", "Status", "Booked On"
    };

    public BookingsPanel(MainFrame mainFrame, BookingService bookingService,
                          UserService userService) {
        this.mainFrame      = mainFrame;
        this.bookingService = bookingService;
        this.userService    = userService;
        this.currentBookings = new ArrayList<Booking>();
        this.currentFilter   = "ALL";

        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(240, 244, 255));
        add(buildTopSection(), BorderLayout.NORTH);
        add(buildTableSection(), BorderLayout.CENTER);
        add(buildBottomSection(), BorderLayout.SOUTH);
    }

    private JPanel buildTopSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 244, 255));
        panel.setBorder(new EmptyBorder(20, 30, 10, 30));

        titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 80, 160));

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterRow.setBackground(new Color(240, 244, 255));
        filterRow.setBorder(new EmptyBorder(10, 0, 0, 0));

        filterRow.add(new JLabel("Show:") {{ setFont(new Font("Arial", Font.BOLD, 13)); }});

        filterAllBtn       = makeFilterButton("All");
        filterConfirmedBtn = makeFilterButton("Confirmed");
        filterCancelledBtn = makeFilterButton("Cancelled");

        filterAllBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyFilter("ALL");
            }
        });
        filterConfirmedBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyFilter("CONFIRMED");
            }
        });
        filterCancelledBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyFilter("CANCELLED");
            }
        });

        filterRow.add(filterAllBtn);
        filterRow.add(filterConfirmedBtn);
        filterRow.add(filterCancelledBtn);

        summaryLabel = new JLabel(" ");
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        summaryLabel.setForeground(new Color(80, 80, 80));
        summaryLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JPanel leftStack = new JPanel();
        leftStack.setLayout(new BoxLayout(leftStack, BoxLayout.Y_AXIS));
        leftStack.setBackground(new Color(240, 244, 255));
        leftStack.add(titleLabel);
        leftStack.add(filterRow);
        leftStack.add(summaryLabel);

        panel.add(leftStack, BorderLayout.WEST);
        setActiveFilter(filterAllBtn);
        return panel;
    }

    private JPanel buildTableSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 244, 255));
        panel.setBorder(new EmptyBorder(5, 30, 10, 30));

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        bookingsTable = new JTable(tableModel);
        bookingsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        bookingsTable.setRowHeight(30);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        bookingsTable.getTableHeader().setBackground(new Color(30, 80, 160));
        bookingsTable.getTableHeader().setForeground(Color.WHITE);
        bookingsTable.setGridColor(new Color(220, 220, 220));
        bookingsTable.setSelectionBackground(new Color(180, 210, 255));

        DefaultTableCellRenderer centreRenderer = new DefaultTableCellRenderer();
        centreRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < COLUMNS.length; i++) {
            bookingsTable.getColumnModel().getColumn(i).setCellRenderer(centreRenderer);
        }

        int[] widths = {50, 80, 160, 140, 55, 80, 70, 90, 130};
        for (int i = 0; i < widths.length; i++) {
            bookingsTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // colour the status column
        bookingsTable.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildBottomSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            new EmptyBorder(12, 30, 12, 30)));

        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton cancelBtn = new JButton("Cancel Selected Booking");
        cancelBtn.setBackground(new Color(200, 50, 50));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(230, 40));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCancelBooking();
            }
        });

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(30, 80, 160));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        refreshBtn.setPreferredSize(new Dimension(100, 40));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadBookings();
            }
        });

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.add(refreshBtn);
        buttonRow.add(cancelBtn);

        panel.add(messageLabel, BorderLayout.WEST);
        panel.add(buttonRow, BorderLayout.EAST);
        return panel;
    }

    public void loadBookings() {
        if (userService.getLoggedInUser() == null) return;
        int userId = userService.getLoggedInUser().getId();
        currentBookings = bookingService.getBookingsForUser(userId);
        applyFilter(currentFilter);
    }

    private void applyFilter(String filter) {
        currentFilter = filter;

        if (filter.equals("ALL")) setActiveFilter(filterAllBtn);
        else if (filter.equals("CONFIRMED")) setActiveFilter(filterConfirmedBtn);
        else setActiveFilter(filterCancelledBtn);

        List<Booking> filtered = new ArrayList<Booking>();
        for (int i = 0; i < currentBookings.size(); i++) {
            Booking b = currentBookings.get(i);
            if (filter.equals("ALL")) filtered.add(b);
            else if (filter.equals("CONFIRMED") && b.getStatus().equals("CONFIRMED")) filtered.add(b);
            else if (filter.equals("CANCELLED") && b.getStatus().equals("CANCELLED")) filtered.add(b);
        }

        populateTable(filtered);

        if (filtered.size() == 0) summaryLabel.setText("No bookings found.");
        else if (filtered.size() == 1) summaryLabel.setText("1 booking found.");
        else summaryLabel.setText(filtered.size() + " bookings found.");

        messageLabel.setText(" ");
    }

    private void populateTable(List<Booking> bookings) {
        tableModel.setRowCount(0);
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            Object[] row = {
                "#" + b.getId(), b.getTrainNumber(),
                b.getOrigin() + " to " + b.getDestination(),
                b.getDepartureTime(), b.getSeatNumber(), b.getSeatClass(),
                "EUR " + String.format("%.2f", b.getTotalPrice()),
                b.getStatus(), b.getFormattedBookedAt()
            };
            tableModel.addRow(row);
        }
        currentBookings = bookings;
    }

    private void handleCancelBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow == -1) { showMessage("Select a booking first.", Color.RED); return; }
        if (selectedRow >= currentBookings.size()) { showMessage("Invalid selection.", Color.RED); return; }

        Booking booking = currentBookings.get(selectedRow);

        if (!booking.getStatus().equals("CONFIRMED")) {
            showMessage("Already cancelled.", new Color(180, 100, 0));
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
            "Cancel this booking?\n\n" +
            "Train: " + booking.getTrainNumber() + " - " + booking.getTrainName() + "\n" +
            "Route: " + booking.getOrigin() + " to " + booking.getDestination() + "\n" +
            "Seat: " + booking.getSeatNumber() + " (" + booking.getSeatClass() + ")\n" +
            "Price: EUR " + String.format("%.2f", booking.getTotalPrice()) + "\n\n" +
            "This can't be undone.",
            "Cancel Booking", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) return;

        boolean success = bookingService.cancelBooking(booking.getId());
        if (success) {
            showMessage("Booking #" + booking.getId() + " cancelled.", new Color(0, 130, 0));
            loadBookings();
        } else {
            showMessage("Failed to cancel. Try again.", Color.RED);
        }
    }

    private void showMessage(String text, Color colour) {
        messageLabel.setText(text);
        messageLabel.setForeground(colour);
    }

    private JButton makeFilterButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(110, 30));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setActiveFilter(JButton activeBtn) {
        filterAllBtn.setBackground(new Color(220, 220, 220));
        filterAllBtn.setForeground(new Color(60, 60, 60));
        filterAllBtn.setBorderPainted(true);
        filterConfirmedBtn.setBackground(new Color(220, 220, 220));
        filterConfirmedBtn.setForeground(new Color(60, 60, 60));
        filterConfirmedBtn.setBorderPainted(true);
        filterCancelledBtn.setBackground(new Color(220, 220, 220));
        filterCancelledBtn.setForeground(new Color(60, 60, 60));
        filterCancelledBtn.setBorderPainted(true);

        activeBtn.setBackground(new Color(30, 80, 160));
        activeBtn.setForeground(Color.WHITE);
        activeBtn.setBorderPainted(false);
    }

    // custom renderer for the status column.  green for confirmed, red for cancelled
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);

            if (value != null && !isSelected) {
                if (value.toString().equals("CONFIRMED")) {
                    cell.setBackground(new Color(210, 245, 215));
                    cell.setForeground(new Color(0, 120, 0));
                } else if (value.toString().equals("CANCELLED")) {
                    cell.setBackground(new Color(250, 215, 215));
                    cell.setForeground(new Color(180, 0, 0));
                } else {
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.BLACK);
                }
            }
            return cell;
        }
    }
}
