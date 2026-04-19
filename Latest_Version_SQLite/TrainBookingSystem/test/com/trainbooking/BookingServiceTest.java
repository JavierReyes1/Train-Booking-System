package com.trainbooking;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.trainbooking.service.BookingService;
import com.trainbooking.service.UserService;
import com.trainbooking.service.TrainService;
import com.trainbooking.db.DatabaseManager;

class BookingServiceTest {

	private static BookingService bookingService;
	private static UserService userService;
	private static TrainService trainService;

	@BeforeAll
	static void beforeAll() {
		System.out.println("Setting up services for testing");
		bookingService = new BookingService();
		userService = new UserService();
		trainService = new TrainService();

		// connect to db so services work
		try {
			DatabaseManager db = DatabaseManager.getInstance();
			db.connect();
			db.createTables();
			db.seedSampleData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@BeforeEach
	void beforeEach(TestInfo info) {
		System.out.println("Running: " + info.getDisplayName());
	}

	// test price calculation for standard class
	@Test
	void testCalculatePriceStandard() {
		double result = bookingService.calculatePrice(35.00, "STANDARD");
		assertEquals(35.00, result);
	}

	// test price calculation for first class (1.5x)
	@Test
	void testCalculatePriceFirst() {
		double result = bookingService.calculatePrice(35.00, "FIRST");
		assertEquals(52.50, result);
	}

	// test price calculation for second class (1.2x)
	@Test
	void testCalculatePriceSecond() {
		double result = bookingService.calculatePrice(35.00, "SECOND");
		assertEquals(42.00, result);
	}

	// test that price rounds to 2 decimal places
	@Test
	void testCalculatePriceRounding() {
		double result = bookingService.calculatePrice(33.33, "FIRST");
		// 33.33 * 1.5 = 49.995 should round to 50.0
		assertEquals(50.0, result);
	}

	// test registration with empty username
	@Test
	void testRegisterEmptyUsername() {
		String result = userService.register("", "password123", "password123", "Test User", "test@email.com");
		assertNotEquals("OK", result);
	}

	// test registration with short password
	@Test
	void testRegisterShortPassword() {
		String result = userService.register("testuser", "123", "123", "Test User", "test@email.com");
		assertEquals("Password must be at least 6 characters.", result);
	}

	// test registration with mismatched passwords
	@Test
	void testRegisterPasswordMismatch() {
		String result = userService.register("testuser", "password123", "password456", "Test User", "test@email.com");
		assertEquals("Passwords do not match.", result);
	}

	// test registration with bad email
	@Test
	void testRegisterInvalidEmail() {
		String result = userService.register("testuser", "password123", "password123", "Test User", "bademail");
		assertEquals("Invalid email.", result);
	}

	// test login with empty fields
	@Test
	void testLoginEmptyUsername() {
		String result = userService.login("", "password");
		assertEquals("Enter your username.", result);
	}

	@Test
	void testLoginEmptyPassword() {
		String result = userService.login("someuser", "");
		assertEquals("Enter your password.", result);
	}

	// test login with wrong username
	@Test
	void testLoginWrongUsername() {
		String result = userService.login("nonexistentuser12345", "password");
		assertEquals("Username not found.", result);
	}

	// test that search returns empty when origin = destination
	@Test
	void testSearchSameOriginDestination() {
		var results = trainService.searchTrains("Dublin", "Dublin", "2025-05-01");
		assertEquals(0, results.size());
	}

	// test search with empty fields returns empty list
	@Test
	void testSearchEmptyFields() {
		var results = trainService.searchTrains("", "", "");
		assertEquals(0, results.size());
	}

	@AfterEach
	void afterEach(TestInfo info) {
		System.out.println("Finished: " + info.getDisplayName());
	}

	@AfterAll
	static void afterAll() {
		System.out.println("All tests complete");
		DatabaseManager.getInstance().disconnect();
	}
}
