import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test {
	public static void main(String[] args){

	final String DATABASE_URL = "jdbc:mariadb://localhost:3306/test_table";
	Connection connection = null;
	PreparedStatement pstat = null;
	String firstName = "Javier";
	String lastName = "Reyes";
	int i = 0;

	try
	{
		connection = DriverManager.getConnection(DATABASE_URL, "root", "pas123");

		pstat = connection.prepareStatement("INSERT INTO Authors (FirstName, LastName) VALUES(?,?)");
		pstat.setString(1, firstName);
		pstat.setString(2, lastName);

		// insert data into table
		i = pstat.executeUpdate();
		System.out.println(i + " record successfully added to the table.");
	}catch(
	SQLException sqlException)
	{
		sqlException.printStackTrace();
	}finally
	{
		try {
			pstat.close();
			connection.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
}
