package com.trainbooking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Train {
	private int id;
	private String trainNumber;
	private String trainName;
	private String origin;
	private String destination;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	private int totalSeats;
	private double pricePerSeat;

	public static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

	public Train() {
	}

	public Train(int id, String trainNumber, String trainName, String origin,
			String destination, LocalDateTime departureTime,
			LocalDateTime arrivalTime, int totalSeats, double pricePerSeat) {
		this.id = id;
		this.trainNumber = trainNumber;
		this.trainName = trainName;
		this.origin = origin;
		this.destination = destination;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.totalSeats = totalSeats;
		this.pricePerSeat = pricePerSeat;
	}

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public LocalDateTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public double getPricePerSeat() {
		return pricePerSeat;
	}

	public void setPricePerSeat(double pricePerSeat) {
		this.pricePerSeat = pricePerSeat;
	}

	public String getFormattedDeparture() {
		return departureTime != null ? departureTime.format(DISPLAY_FORMAT) : "";
	}

	public String getFormattedArrival() {
		return arrivalTime != null ? arrivalTime.format(DISPLAY_FORMAT) : "";
	}

	@Override
	public String toString() {
		return trainNumber + " - " + trainName + " (" + origin + " → " + destination + ")";
	}
}
