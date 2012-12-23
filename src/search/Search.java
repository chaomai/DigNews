package search;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Search() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String queryString = request.getParameter("SearchBox");

		if (!queryString.isEmpty()) {
			Query query = new Query();
			HashMap<String, String> results = query.getMatches(queryString);

			if (results.size() == 0) {
				results.put("/DigNews", "No matching contents.");
			}

			request.setAttribute("result", results);
			request.getRequestDispatcher("result.jsp").forward(request,
					response);
		} else {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		}
	}
}
