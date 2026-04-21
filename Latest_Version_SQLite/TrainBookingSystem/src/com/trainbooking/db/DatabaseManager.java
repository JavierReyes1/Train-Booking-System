package com.trainbooking.db;

import com.trainbooking.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
	private static final String DB_URL = "jdbc:sqlite:" + getJarDirectory() + "/trainbooking.db";

	private static final DateTimeFormatter DB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static DatabaseManager instance;
	private Connection connection;

	private DatabaseManager() {
	}

	// This finds the database file in the projec's directory.
	private static String getJarDirectory() {
		try {
			return new java.io.File(DatabaseManager.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI())
					.getParentFile().getAbsolutePath();
		} catch (Exception e) {
			return System.getProperty("user.home");
		}
	}

	// singleton
	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}

	public void connect() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(DB_URL);
			connection.createStatement().execute("PRAGMA foreign_keys = ON");
		}
	}

	public void disconnect() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// create all tables
	public void createTables() throws SQLException {
		String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
				"    id            INTEGER PRIMARY KEY AUTOINCREMENT," +
				"    username      TEXT    NOT NULL UNIQUE," +
				"    password_hash TEXT    NOT NULL," +
				"    full_name     TEXT    NOT NULL," +
				"    email         TEXT    NOT NULL UNIQUE" +
				")";

		String createTrains = "CREATE TABLE IF NOT EXISTS trains (" +
				"    id             INTEGER PRIMARY KEY AUTOINCREMENT," +
				"    train_number   TEXT    NOT NULL UNIQUE," +
				"    train_name     TEXT    NOT NULL," +
				"    origin         TEXT    NOT NULL," +
				"    destination    TEXT    NOT NULL," +
				"    departure_time TEXT    NOT NULL," +
				"    arrival_time   TEXT    NOT NULL," +
				"    total_seats    INTEGER NOT NULL," +
				"    price_per_seat REAL    NOT NULL" +
				")";

		String createSeats = "CREATE TABLE IF NOT EXISTS seats (" +
				"    id          INTEGER PRIMARY KEY AUTOINCREMENT," +
				"    train_id    INTEGER NOT NULL," +
				"    seat_number TEXT    NOT NULL," +
				"    seat_class  TEXT    NOT NULL DEFAULT 'STANDARD'," +
				"    is_booked   INTEGER NOT NULL DEFAULT 0," +
				"    FOREIGN KEY (train_id) REFERENCES trains(id)" +
				")";

		String createBookings = "CREATE TABLE IF NOT EXISTS bookings (" +
				"    id          INTEGER PRIMARY KEY AUTOINCREMENT," +
				"    user_id     INTEGER NOT NULL," +
				"    train_id    INTEGER NOT NULL," +
				"    seat_id     INTEGER NOT NULL," +
				"    status      TEXT    NOT NULL DEFAULT 'CONFIRMED'," +
				"    booked_at   TEXT    NOT NULL," +
				"    total_price REAL    NOT NULL," +
				"    FOREIGN KEY (user_id)  REFERENCES users(id)," +
				"    FOREIGN KEY (train_id) REFERENCES trains(id)," +
				"    FOREIGN KEY (seat_id)  REFERENCES seats(id)" +
				")";

		Statement stmt = connection.createStatement();
		stmt.execute(createUsers);
		stmt.execute(createTrains);
		stmt.execute(createSeats);
		stmt.execute(createBookings);
		stmt.close();
	}

	// adds sample trains if db is empty
	public void seedSampleData() throws SQLException {
		if (getTrainCount() > 0) {
			return;
		}

		String[][] trains = {
				{ "IE101", "Dublin Express", "Dublin", "Cork", "2025-05-01 07:00:00", "2025-05-01 09:30:00", "60", "35.00" },
				{ "IE102", "Cork Morning Star", "Cork", "Dublin", "2025-05-01 08:00:00", "2025-05-01 10:30:00", "60", "35.00" },
				{ "IE103", "Galway Flyer", "Dublin", "Galway", "2025-05-01 09:00:00", "2025-05-01 11:15:00", "80", "28.00" },
				{ "IE104", "Belfast Express", "Dublin", "Belfast", "2025-05-01 10:00:00", "2025-05-01 12:00:00", "100",
						"22.00" },
				{ "IE105", "Limerick Connect", "Dublin", "Limerick", "2025-05-01 11:00:00", "2025-05-01 13:00:00", "60",
						"30.00" },
				{ "IE106", "Waterford Intercity", "Dublin", "Waterford", "2025-05-01 12:00:00", "2025-05-01 14:00:00", "60",
						"25.00" },
				{ "IE107", "Sligo Arrow", "Dublin", "Sligo", "2025-05-01 13:00:00", "2025-05-01 15:30:00", "50", "32.00" },
				{ "IE108", "Evening Express", "Dublin", "Cork", "2025-05-01 17:00:00", "2025-05-01 19:30:00", "60", "35.00" }
		};

		for (int i = 0; i < trains.length; i++) {
			String[] t = trains[i];
			int trainId = insertTrain(t[0], t[1], t[2], t[3], t[4], t[5],
					Integer.parseInt(t[6]), Double.parseDouble(t[7]));
			generateSeats(trainId, Integer.parseInt(t[6]));
		}
	}

	private int insertTrain(String number, String name, String origin, String destination,
			String departure, String arrival, int seats, double price)
			throws SQLException {
		String sql = "INSERT INTO trains (train_number, train_name, origin, destination, " +
				"departure_time, arrival_time, total_seats, price_per_seat) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, number);
		ps.setString(2, name);
		ps.setString(3, origin);
		ps.setString(4, destination);
		ps.setString(5, departure);
		ps.setString(6, arrival);
		ps.setInt(7, seats);
		ps.setDouble(8, price);
		ps.executeUpdate();

		ResultSet keys = ps.getGeneratedKeys();
		int generatedId = -1;
		if (keys.next()) {
			generatedId = keys.getInt(1);
		}
		ps.close();
		return generatedId;
	}

	private void generateSeats(int trainId, int totalSeats) throws SQLException {
		String sql = "INSERT INTO seats (train_id, seat_number, seat_class) VALUES (?, ?, ?)";
		String[] cols = { "A", "B", "C", "D" };

		PreparedStatement ps = connection.prepareStatement(sql);
		int seatCount = 0;
		int row = 1;

		while (seatCount < totalSeats) {
			for (int c = 0; c < cols.length; c++) {
				if (seatCount >= totalSeats)
					break;

				String seatNum = row + cols[c];
				String seatClass;
				if (row <= 2) {
					seatClass = "FIRST";
				} else if (row <= 6) {
					seatClass = "SECOND";
				} else {
					seatClass = "STANDARD";
				}

				ps.setInt(1, trainId);
				ps.setString(2, seatNum);
				ps.setString(3, seatClass);
				ps.addBatch();
				seatCount++;
			}
			row++;
		}
		ps.executeBatch();
		ps.close();
	}

	private int getTrainCount() throws SQLException {
		ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM trains");
		int count = 0;
		if (rs.next())
			count = rs.getInt(1);
		return count;
	}

	// ---- user stuff ----

	public boolean createUser(String username, String passwordHash,
			String fullName, String email) throws SQLException {
		String sql = "INSERT INTO users (username, password_hash, full_name, email) VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, passwordHash);
			ps.setString(3, fullName);
			ps.setString(4, email);
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			if (e.getMessage().contains("UNIQUE")) {
				return false; // username or email taken
			}
			throw e;
		}
	}

	public User getUserByUsername(String username) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		User user = null;
		if (rs.next()) {
			user = new User(rs.getInt("id"), rs.getString("username"),
					rs.getString("password_hash"), rs.getString("full_name"),
					rs.getString("email"));
		}
		ps.close();
		return user;
	}

	public User getUserById(int id) throws SQLException {
		String sql = "SELECT * FROM users WHERE id = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		User user = null;
		if (rs.next()) {
			user = new User(rs.getInt("id"), rs.getString("username"),
					rs.getString("password_hash"), rs.getString("full_name"),
					rs.getString("email"));
		}
		ps.close();
		return user;
	}

	// ---- train stuff ----

	public List<Train> searchTrains(String origin, String destination, String date)
			throws SQLException {
		String sql = "SELECT * FROM trains " +
				"WHERE LOWER(origin) = LOWER(?) " +
				"AND LOWER(destination) = LOWER(?) " +
				"AND departure_time LIKE ? " +
				"ORDER BY departure_time";

		List<Train> trains = new ArrayList<Train>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, origin);
			ps.setString(2, destination);
			ps.setString(3, date + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				trains.add(mapTrain(rs));
			}
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return trains;
	}

	public List<Train> getAllTrains() throws SQLException {
		List<Train> trains = new ArrayList<Train>();
		ResultSet rs = connection.createStatement()
				.executeQuery("SELECT * FROM trains ORDER BY departure_time");
		while (rs.next()) {
			trains.add(mapTrain(rs));
		}
		return trains;
	}

	public List<String> getDistinctOrigins() throws SQLException {
		List<String> origins = new ArrayList<String>();
		ResultSet rs = connection.createStatement()
				.executeQuery("SELECT DISTINCT origin FROM trains ORDER BY origin");
		while (rs.next())
			origins.add(rs.getString(1));
		return origins;
	}

	public List<String> getDistinctDestinations() throws SQLException {
		List<String> dests = new ArrayList<String>();
		ResultSet rs = connection.createStatement()
				.executeQuery("SELECT DISTINCT destination FROM trains ORDER BY destination");
		while (rs.next())
			dests.add(rs.getString(1));
		return dests;
	}

	private Train mapTrain(ResultSet rs) throws SQLException {
		return new Train(
				rs.getInt("id"), rs.getString("train_number"), rs.getString("train_name"),
				rs.getString("origin"), rs.getString("destination"),
				LocalDateTime.parse(rs.getString("departure_time"), DB_FORMAT),
				LocalDateTime.parse(rs.getString("arrival_time"), DB_FORMAT),
				rs.getInt("total_seats"), rs.getDouble("price_per_seat"));
	}

	// ---- seats ----

	public List<Seat> getSeatsByTrain(int trainId) throws SQLException {
		String sql = "SELECT * FROM seats WHERE train_id = ? ORDER BY seat_number";
		List<Seat> seats = new ArrayList<Seat>();
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, trainId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			seats.add(new Seat(rs.getInt("id"), rs.getInt("train_id"),
					rs.getString("seat_number"), rs.getString("seat_class"),
					rs.getInt("is_booked") == 1));
		}
		ps.close();
		return seats;
	}

	public int getAvailableSeatCount(int trainId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM seats WHERE train_id = ? AND is_booked = 0";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, trainId);
		ResultSet rs = ps.executeQuery();
		int count = 0;
		if (rs.next())
			count = rs.getInt(1);
		ps.close();
		return count;
	}

	public boolean markSeatBooked(int seatId, boolean booked) throws SQLException {
		String sql = "UPDATE seats SET is_booked = ? WHERE id = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, booked ? 1 : 0);
		ps.setInt(2, seatId);
		int rows = ps.executeUpdate();
		ps.close();
		return rows > 0;
	}

	// ---- bookings ----

	public Booking createBooking(int userId, int trainId, int seatId,
			double totalPrice) throws SQLException {
		String sql = "INSERT INTO bookings (user_id, train_id, seat_id, status, booked_at, total_price) " +
				"VALUES (?, ?, ?, 'CONFIRMED', ?, ?)";
		PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		LocalDateTime now = LocalDateTime.now();
		ps.setInt(1, userId);
		ps.setInt(2, trainId);
		ps.setInt(3, seatId);
		ps.setString(4, now.format(DB_FORMAT));
		ps.setDouble(5, totalPrice);
		ps.executeUpdate();

		ResultSet keys = ps.getGeneratedKeys();
		Booking booking = null;
		if (keys.next()) {
			booking = new Booking(keys.getInt(1), userId, trainId, seatId,
					"CONFIRMED", now, totalPrice);
		}
		ps.close();
		return booking;
	}

	public boolean cancelBooking(int bookingId) throws SQLException {
		String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, bookingId);
		int rows = ps.executeUpdate();
		ps.close();
		return rows > 0;
	}

	// uses inner join to get all booking info in one query
	public List<Booking> getBookingsByUser(int userId) throws SQLException {
		String sql = "SELECT b.*, t.train_number, t.train_name, t.origin, t.destination, " +
				"t.departure_time, s.seat_number, s.seat_class " +
				"FROM bookings b " +
				"JOIN trains t ON b.train_id = t.id " +
				"JOIN seats s ON b.seat_id = s.id " +
				"WHERE b.user_id = ? " +
				"ORDER BY b.booked_at DESC";

		List<Booking> bookings = new ArrayList<Booking>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Booking b = new Booking(
						rs.getInt("id"), rs.getInt("user_id"), rs.getInt("train_id"),
						rs.getInt("seat_id"), rs.getString("status"),
						LocalDateTime.parse(rs.getString("booked_at"), DB_FORMAT),
						rs.getDouble("total_price"));
				b.setTrainNumber(rs.getString("train_number"));
				b.setTrainName(rs.getString("train_name"));
				b.setOrigin(rs.getString("origin"));
				b.setDestination(rs.getString("destination"));
				b.setDepartureTime(rs.getString("departure_time"));
				b.setSeatNumber(rs.getString("seat_number"));
				b.setSeatClass(rs.getString("seat_class"));
				bookings.add(b);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return bookings;
	}

	public int getSeatIdFromBooking(int bookingId) throws SQLException {
		String sql = "SELECT seat_id FROM bookings WHERE id = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, bookingId);
		ResultSet rs = ps.executeQuery();
		int seatId = -1;
		if (rs.next())
			seatId = rs.getInt(1);
		ps.close();
		return seatId;
	}
}
