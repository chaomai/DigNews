package display;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class News {
	/**
	 * A database connection.
	 */
	private Connection conn = null;

	/**
	 * Set database connection.
	 */
	public News() {
		this.connectDb();
	}

	/**
	 * Connect to database.
	 */
	private void connectDb() {
		String url = "jdbc:mysql://localhost:3306/search_engine_data_novels";
		String username = "root";
		String password = "mc19-92mc";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException cnfex) {
			cnfex.printStackTrace();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}

	public HashMap<String, String> getNews(String category) {
		HashMap<String, String> results = new HashMap<>();

		try {
			PreparedStatement statement = conn
					.prepareStatement("select * from urllist where category = ?");
			statement.setString(1, category);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String url = resultSet.getString(2);
				String title = resultSet.getString(3);
				results.put(url, title);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (results.size() == 0) {
			results.put("/DigNews", "No matching contents.");
		}

		return results;
	}
}
