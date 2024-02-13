import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseOperations {

	private Connection dbConnection;

	public DatabaseOperations() {
		try {
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "Burhansql916916");
		} catch (Exception e) {
			System.out.println("dbError: " + e.getMessage());
		}
	}

	public void registerUser(int id, String userName, String surname, int pasword, double balance) throws SQLException {

		String insertQuery = "INSERT INTO user_info (user_id, user_password, user_nameSurname, user_balance) VALUES (?,?,?,?)";

		PreparedStatement registerStatement = dbConnection.prepareStatement(insertQuery);
		registerStatement.setInt(1, id);
		registerStatement.setString(3, userName + " " + surname);
		registerStatement.setInt(2, pasword);
		registerStatement.setDouble(4, balance);

		registerStatement.executeUpdate();
		System.out.println("Register Successful!");
	}

}
