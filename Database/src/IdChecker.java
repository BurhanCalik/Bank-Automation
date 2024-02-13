import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdChecker {

	public static boolean isUnique(int randomID) throws SQLException {
		try (Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root",
				"Burhansql916916")) {

			String query = "SELECT user_id FROM user_info WHERE user_id = ?";
			try (PreparedStatement preparedStatement = myConnection.prepareStatement(query)) {

				preparedStatement.setInt(1, randomID);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					return !resultSet.next();

				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
	}
}
