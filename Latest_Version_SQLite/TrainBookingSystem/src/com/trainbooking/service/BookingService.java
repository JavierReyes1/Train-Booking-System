package com.trainbooking.service;

import com.trainbooking.db.DatabaseManager;
import com.trainbooking.model.Booking;
import com.trainbooking.model.Seat;
import com.trainbooking.model.Train;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

	private DatabaseManager db;

	public BookingService() {
		db = DatabaseManager.getInstance();
	}

	public Booking bookSeat(int userId, Train train, Seat seat) {
		if (seat.isBooked())
			return null;

		try {
			boolean seatMarked = db.markSeatBooked(seat.getId(), true);
			if (!seatMarked)
				return null;

			// calculate price based on class
			double price = calculatePrice(train.getPricePerSeat(), seat.getSeatClass());

			Booking booking = db.createBooking(userId, train.getId(), seat.getId(), price);
			return booking;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean cancelBooking(int bookingId) {
		try {
			int seatId = db.getSeatIdFromBooking(bookingId);
			if (seatId == -1)
				return false;

			// free the seat back up
			db.markSeatBooked(seatId, false);
			return db.cancelBooking(bookingId);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Booking> getBookingsForUser(int userId) {
		try {
			return db.getBookingsByUser(userId);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Booking>();
		}
	}

	public List<Booking> getActiveBookingsForUser(int userId) {
		List<Booking> all = getBookingsForUser(userId);
		List<Booking> active = new ArrayList<Booking>();
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).getStatus().equals("CONFIRMED"))
				active.add(all.get(i));
		}
		return active;
	}

	public List<Booking> getCancelledBookingsForUser(int userId) {
		List<Booking> all = getBookingsForUser(userId);
		List<Booking> cancelled = new ArrayList<Booking>();
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).getStatus().equals("CANCELLED"))
				cancelled.add(all.get(i));
		}
		return cancelled;
	}

	// first class is 1.5x, second is 1.2x, standard is base price
	public double calculatePrice(double basePrice, String seatClass) {
		double price = basePrice;
		if (seatClass.equals("FIRST"))
			price = price * 1.5;
		if (seatClass.equals("SECOND"))
			price = price * 1.2;
		return Math.round(price * 100.0) / 100.0;
	}
}
