package com.trainbooking.service;

import com.trainbooking.db.DatabaseManager;
import com.trainbooking.model.Seat;
import com.trainbooking.model.Train;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainService {

	private DatabaseManager db;

	public TrainService() {
		db = DatabaseManager.getInstance();
	}

	public List<Train> searchTrains(String origin, String destination, String date) {
		if (origin == null || origin.trim().equals(""))
			return new ArrayList<Train>();
		if (destination == null || destination.trim().equals(""))
			return new ArrayList<Train>();
		if (date == null || date.trim().equals(""))
			return new ArrayList<Train>();
		if (origin.trim().equalsIgnoreCase(destination.trim()))
			return new ArrayList<Train>();

		try {
			return db.searchTrains(origin.trim(), destination.trim(), date.trim());
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Train>();
		}
	}

	public List<Train> getAllTrains() {
		try {
			return db.getAllTrains();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Train>();
		}
	}

	public List<Seat> getSeatsForTrain(int trainId) {
		try {
			return db.getSeatsByTrain(trainId);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Seat>();
		}
	}

	public List<Seat> getAvailableSeats(int trainId) {
		List<Seat> allSeats = getSeatsForTrain(trainId);
		List<Seat> available = new ArrayList<Seat>();
		for (int i = 0; i < allSeats.size(); i++) {
			if (!allSeats.get(i).isBooked()) {
				available.add(allSeats.get(i));
			}
		}
		return available;
	}

	public int getAvailableSeatCount(int trainId) {
		try {
			return db.getAvailableSeatCount(trainId);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	// for the dropdown menus
	public List<String> getOrigins() {
		try {
			return db.getDistinctOrigins();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public List<String> getDestinations() {
		try {
			return db.getDistinctDestinations();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}
}
