# Train Booking System

A Java desktop application built with Swing and SQLite.

---

## What You Need Before Starting

- Java JDK 17 or higher installed
- The `sqlite-jdbc` JAR file (download instructions below)

---

## Project Structure

```
TrainBookingSystem/
├── src/
│   └── com/trainbooking/
│       ├── Main.java
│       ├── model/
│       │   ├── User.java
│       │   ├── Train.java
│       │   ├── Seat.java
│       │   └── Booking.java
│       ├── db/
│       │   └── DatabaseManager.java
│       ├── service/
│       │   ├── UserService.java
│       │   ├── TrainService.java
│       │   └── BookingService.java
│       └── gui/
│           ├── MainFrame.java
│           ├── LoginPanel.java
│           ├── SearchPanel.java
│           ├── SeatSelectionPanel.java
│           └── BookingsPanel.java
├── lib/
│   └── sqlite-jdbc-3.45.1.0.jar 
├── out/                            <-- compiled classes go here
├── compile.bat                     <-- Windows compile script
├── run.bat                         <-- Windows run script
├── compile.sh                      <-- Mac/Linux compile script
├── run.sh                          <-- Mac/Linux run script
└── README.md
```

---

## Set Up the Project in Your IDE

### If you are using IntelliJ IDEA:
1. Open IntelliJ and choose "Open" - select the TrainBookingSystem folder
2. Right-click the `src` folder -> Mark Directory as -> Sources Root
3. Add the SQLite JAR as a library:
   - Go to File -> Project Structure -> Libraries
   - Click the + button -> Java
   - Navigate to your `lib` folder and select the JAR file
   - Click OK
4. Make sure your Project SDK is set to Java 17 or higher

### If you are using Eclipse:
1. File -> New -> Java Project
2. Uncheck "Use default location" and point it to the TrainBookingSystem folder
3. Right-click the project -> Build Path -> Add External Archives
4. Select the sqlite-jdbc JAR from  lib folder

---

## Step 3 — Compile and Run

### Option A: Using Your IDE
- Right-click `Main.java` -> Run As -> Java Application

### Option B: Using the command line on Windows
1. Open Command Prompt inside the TrainBookingSystem folder
2. Run:
```
compile.bat
run.bat
```

### Option B: Using the command line on Mac or Linux
1. Open Terminal inside the TrainBookingSystem folder
2. Run:
```
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

---

## How to Use the App

### First Time
1. Click the "Register" tab
2. Fill in your full name, email, username and password
3. Click "Create Account"
4. Switch to the "Login" tab and log in

### Searching for Trains
1. Select an origin and destination from the dropdowns
2. Enter a date in the format: 2025-05-01
3. Click "Search"
4. The sample data uses the date 2025-05-01 — use that to see results

### Booking a Seat
1. Click a train in the search results
2. Click "Select Seats"
3. Click any green seat on the seat map
4. Check the price shown at the bottom
5. Click "Confirm Booking"

### Viewing / Cancelling Bookings
1. Click "My Bookings" in the top navigation bar
2. Use the filter buttons to show All / Confirmed / Cancelled
3. Select a booking from the table
4. Click "Cancel Selected Booking" to cancel it

---

## Sample Data

The app comes pre-loaded with 8 trains on the date 2025-05-01:

| Train  | Route                    | Departs |
|--------|--------------------------|---------|
| IE101  | Dublin to Cork           | 07:00   |
| IE102  | Cork to Dublin           | 08:00   |
| IE103  | Dublin to Galway         | 09:00   |
| IE104  | Dublin to Belfast        | 10:00   |
| IE105  | Dublin to Limerick       | 11:00   |
| IE106  | Dublin to Waterford      | 12:00   |
| IE107  | Dublin to Sligo          | 13:00   |
| IE108  | Dublin to Cork (Evening) | 17:00   |

---

## Seat Classes and Prices

| Class    | Rows  | Price Multiplier |
|----------|-------|-----------------|
| FIRST    | 1-2   | Base x 1.5      |
| SECOND   | 3-6   | Base x 1.2      |
| STANDARD | 7+    | Base price      |

---

## The Database File

When you run the app for the first time, a file called `trainbooking.db`
is created in the same folder where you run the app from.
This is your SQLite database it stores all users, trains, seats and bookings.
You can open it with a free tool called "DB Browser for SQLite" if you want
to inspect the data directly.
