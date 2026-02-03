
//Author: Javier Reyes
//Purpose: Test connection with databases, implement 	CRUD and observe it's behaviour
//
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Test {
	static final String DATABASE_URL;
	static final String USER;
	static final String PASSWORD;
	String firstName;
	String lastName;

	static Connection connection;
	static PreparedStatement pstat;
	static ResultSet resultSet;
	static ResultSetMetaData metaData;

	static {
		DATABASE_URL = "jdbc:mariadb://localhost:3306/Train_Database";
		USER = "root";
		PASSWORD = "NEW_PASSWORD";
		//firstName = "Javier";
		//lastName = "Reyes";
		connection = null;
		pstat = null;
		resultSet = null;
		metaData = null;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public static int insert(String firstName, String lastName) { // This method inserts a user into the Customer's table
		int i = 0;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
			pstat = connection.prepareStatement("INSERT INTO Customers (FirstName, LastName) VALUES(?,?)");

			pstat.setString(1, firstName);
			pstat.setString(2, lastName);

			i = pstat.executeUpdate();
			System.out.println(i + " record successfully added to the table.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstat.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i;
	}// end INSERT

	public static int update(String firstName, String lastName) {
		int i = 0;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
			pstat = connection.prepareStatement("Update Customers SET LastName=? Where FirstName=?");
			pstat.setString(1, lastName);
			pstat.setString(2, firstName);

			// Update data in the table
			i = pstat.executeUpdate();
			System.out.println(i + " record successfully updated in the dtable.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstat.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i;
	}// End of UPDATE

	public static int delete(int customerID) {
		int i = 0;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
			pstat = connection.prepareStatement("Delete From Customers Where CustomerID=?");
			pstat.setInt(1, customerID);

			// delete data from the table
			i = pstat.executeUpdate();
			System.out.println(i + "record successfully removed from the table.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstat.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return i;
		}
	}// End DELETE

	public static void query() {
		int numberOfColumns = 0;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
			pstat = connection.prepareStatement("SELECT CustomerID, FirstName, LastName FROM Customers");

			// query data in the table
			resultSet = pstat.executeQuery();

			// process query results
			metaData = resultSet.getMetaData();
			numberOfColumns = metaData.getColumnCount();
			System.out.println("Customers Table of Train Database:\n");

			for (int i = 1; i <= numberOfColumns; i++) {
				System.out.println(metaData.getColumnName(i) + "\t\n");
			}
			while (resultSet.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					System.out.println(resultSet.getObject(i) + "\t\t\n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				pstat.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} // end of QUERY
	
	public void menu(){

	}

	public static void main(String[] args) {
		//delete("Javier", "Reyes");
		delete(1);
		query();

	}// end main

}// end class
