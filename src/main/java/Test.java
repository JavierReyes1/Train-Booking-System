import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test {
	final static String DATABASE_URL = "jdbc:mariadb://localhost:3306/test_table";
	final static String USER = "root";
	final static String PASSWORD = "pas123";

	static String firstName = "Javier";
	static String lastName = "Reyes";

	public static void insert() {
		try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
				PreparedStatement pstat = connection.prepareStatement(
						"INSERT INTO Authors (FirstName, LastName) VALUES(?,?)")) {

			pstat.setString(1, firstName);
			pstat.setString(2, lastName);

			int rowsAffected = pstat.executeUpdate();
			System.out.println(rowsAffected + " record successfully added to the table.");

		} catch (SQLException e) {
			System.err.println("Database error occurred:");
			e.printStackTrace();
		}
	}

	public static void 

	public static void main(String[] args) {
	}// end main
}// end class
