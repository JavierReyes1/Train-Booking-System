package com.trainbooking.model;

public class Seat {
	private int id;
	private int trainId;
	private String seatNumber;
	private String seatClass; // FIRST, SECOND, STANDARD
	private boolean isBooked;

	public Seat() {
	}

	public Seat(int id, int trainId, String seatNumber, String seatClass, boolean isBooked) {
		this.id = id;
		this.trainId = trainId;
		this.seatNumber = seatNumber;
		this.seatClass = seatClass;
		this.isBooked = isBooked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean booked) {
		isBooked = booked;
	}

	@Override
	public String toString() {
		return seatNumber + " [" + seatClass + "]" + (isBooked ? " (Booked)" : " (Available)");
	}
}
