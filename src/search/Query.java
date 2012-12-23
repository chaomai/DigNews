package search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

public class Query {
	/**
	 * A database connection.
	 */
	private Connection conn = null;

	/**
	 * Set database connection.
	 */
	public Query() {
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

	/**
	 * Get results with title and url.
	 * 
	 * @param query
	 * @return
	 */
	public HashMap<String, String> getMatches(String query) {
		HashMap<String, String> resultUrls = new HashMap<>();

		ArrayList<String> words = getWords(query);
		ArrayList<String> cons = getConjunctions(query);

		Iterator<String> wordsIterator = words.iterator();
		Iterator<String> consIterator = cons.iterator();

		String word = wordsIterator.next();
		resultUrls = getWordUrls(word);

		while (wordsIterator.hasNext()) {
			String conjunction = consIterator.next();
			String word1 = wordsIterator.next();

			if (conjunction.equals("and")) {
				resultUrls = getUrlsIntersection(resultUrls, getWordUrls(word1));
			} else {
				resultUrls = getUrlsUnion(resultUrls, getWordUrls(word1));
			}
		}

		return resultUrls;
	}

	/**
	 * Get urls Union.
	 * 
	 * @param urls1
	 * @param urls2
	 * @return
	 */
	private HashMap<String, String> getUrlsUnion(HashMap<String, String> urls1,
			HashMap<String, String> urls2) {
		HashMap<String, String> resultMap = urls1;

		Iterator<String> urlsIterator = urls2.keySet().iterator();

		while (urlsIterator.hasNext()) {
			String url = (String) urlsIterator.next();
			if (!resultMap.containsKey(url)) {
				resultMap.put(url, urls2.get(url));
			}
		}

		return resultMap;
	}

	/**
	 * Get urls Intersection.
	 * 
	 * @param urls1
	 * @param urls2
	 * @return
	 */
	private HashMap<String, String> getUrlsIntersection(
			HashMap<String, String> urls1, HashMap<String, String> urls2) {
		Set<Entry<String, String>> urlsSet1 = urls1.entrySet();
		Set<Entry<String, String>> urlsSet2 = urls2.entrySet();

		if (urlsSet1.isEmpty()) {
			return convertSetToHashMap(urlsSet2);
		} else if (urlsSet2.isEmpty()) {
			return convertSetToHashMap(urlsSet1);
		} else {
			urlsSet1.retainAll(urlsSet2);
			return convertSetToHashMap(urlsSet1);
		}
	}

	/**
	 * Convert Set to HashMap.
	 * 
	 * @param set
	 * @return
	 */
	private HashMap<String, String> convertSetToHashMap(
			Set<Entry<String, String>> set) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (Entry<String, String> entry : set) {
			map.put(entry.getKey(), entry.getValue());
		}

		return map;
	}

	/**
	 * Get tokens from query string.
	 * 
	 * @param query
	 * @return
	 */
	private ArrayList<String> getTokens(String query) {
		ArrayList<String> tokens = ProcessDocs.stemming(ProcessDocs
				.tokenize(query));
		return tokens;
	}

	/**
	 * Check whether a word is conjunction("and"/"or").
	 * 
	 * @param word
	 * @return
	 */
	private boolean isConjunction(String word) {
		return word.equals("and") || word.equals("or");
	}

	/**
	 * Get query words.
	 * 
	 * @param query
	 * @return
	 */
	private ArrayList<String> getWords(String query) {
		ArrayList<String> tokens = getTokens(query);
		ArrayList<String> conjunctions = new ArrayList<>();

		Iterator<String> tokensIterator = tokens.iterator();

		while (tokensIterator.hasNext()) {
			String word = tokensIterator.next();

			if (!isConjunction(word)) {
				conjunctions.add(word);
			}
		}

		return conjunctions;
	}

	/**
	 * Get "and"/"or".
	 * 
	 * @param query
	 * @return
	 */
	private ArrayList<String> getConjunctions(String query) {
		ArrayList<String> tokens = getTokens(query);
		ArrayList<String> conjunctions = new ArrayList<>();

		Iterator<String> tokensIterator = tokens.iterator();

		String word = tokensIterator.next();

		while (tokensIterator.hasNext()) {
			word = tokensIterator.next();
			if (isConjunction(word)) {
				conjunctions.add(word);
				word = tokensIterator.next();
			} else {
				conjunctions.add("and");
			}
		}

		return conjunctions;
	}

	/**
	 * Get url that contents have word.
	 * 
	 * @param word
	 * @return
	 */
	private HashMap<String, String> getWordUrls(String word) {
		HashMap<String, String> resultUrls = new HashMap<>();
		int id = this.getWordId(word);

		try {
			PreparedStatement statement = conn
					.prepareStatement("select location from wordlocation where wordid = ?");
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String wordsPosString = resultSet.getString(1);

				StringTokenizer delimiterTokenizer = new StringTokenizer(
						wordsPosString, " ");

				while (delimiterTokenizer.hasMoreElements()) {
					int urlid = Integer
							.parseInt(delimiterTokenizer.nextToken());
					String url = getUrlById(urlid);
					String title = getTitleById(urlid);

					if (!resultUrls.containsKey(url)) {
						resultUrls.put(url, title);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultUrls;
	}

	/**
	 * Get id from wordlist table.
	 * 
	 * @param value
	 * @return
	 */
	private int getWordId(String value) {
		int id = 0;
		try {
			PreparedStatement statement = conn
					.prepareStatement("select id from wordlist where word = ?");
			statement.setString(1, value);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

	/**
	 * Get url from urllist by id.
	 * 
	 * @param id
	 * @return
	 */
	private String getUrlById(int id) {
		String url = null;
		try {
			PreparedStatement statement = conn
					.prepareStatement("select url from urllist where id = ?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				url = resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return url;
	}

	/**
	 * Get title from urllist by id.
	 * 
	 * @param id
	 * @return
	 */
	private String getTitleById(int id) {
		String title = null;
		try {
			PreparedStatement statement = conn
					.prepareStatement("select title from urllist where id = ?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				title = resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return title;
	}
}
