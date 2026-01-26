import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:mariadb://localhost:3306/test_table";
        final String USER = "root";
        final String PASSWORD = "pas123";
        
        String firstName = "Javier";
        String lastName = "Reyes";
        
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
}
