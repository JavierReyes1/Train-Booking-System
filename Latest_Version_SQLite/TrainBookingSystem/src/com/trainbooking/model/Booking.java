package com.trainbooking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
	private int id;
	private int userId;
	private int trainId;
	private int seatId;
	private String status; // CONFIRMED or CANCELLED
	private LocalDateTime bookedAt;
	private double totalPrice;

	// these are from the join query, not actually in bookings table
	private String trainNumber;
	private String trainName;
	private String origin;
	private String destination;
	private String departureTime;
	private String seatNumber;
	private String seatClass;

	public static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

	public Booking() {
	}

	public Booking(int id, int userId, int trainId, int seatId,
			String status, LocalDateTime bookedAt, double totalPrice) {
		this.id = id;
		this.userId = userId;
		this.trainId = trainId;
		this.seatId = seatId;
		this.status = status;
		this.bookedAt = bookedAt;
		this.totalPrice = totalPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(LocalDateTime bookedAt) {
		this.bookedAt = bookedAt;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getFormattedBookedAt() {
		return bookedAt != null ? bookedAt.format(DISPLAY_FORMAT) : "";
	}

	// joined field getters/setters
	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
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

	@Override
	public String toString() {
		return "Booking #" + id + " - " + trainNumber + " seat " + seatNumber + " (" + status + ")";
	}
}
