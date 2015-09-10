package com.emc.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.emc.monitor.job.XploreMonitor;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.MonitorUtils;
import com.emc.monitor.utils.DatabaseUtil;

/**
 * Servlet implementation class Preferences
 */
@WebServlet("/Preferences")
public class Preferences extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DatabaseUtil du;
	private Set<DocumentumService> sds;
	private DocumentumService ds;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		HttpSession s = request.getSession(false);
		if (s == null) {
			s = request.getSession();
			// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid
			// HTTP session");
		}
		ServletContext sc = getServletContext();
		du = (DatabaseUtil) sc.getAttribute("dbcon");
		if (du == null) {
			du = new DatabaseUtil();
			sc.setAttribute("dbcon", du);
		}
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Documentum Environment Monitor</title>");
			out.println("</head>");
			out.println("<body>");

			out.println("<table border = '1' style='width:100%'>");
			out.println("<tr>");
			out.println("<th>Service</th>");
			out.println("<th>Status</th>");
			out.println("<th>Version</th>");
			out.println("<th>Last Update</th>");
			out.println("<tr>");
			ResultSet rs = MonitorUtils.getStatus();
			try {
				while (rs.next()) {
					out.println("<tr>");
					out.println("<td>" + rs.getString(1) + "</td>");
					out.println("<td>" + rs.getString(2) + "</td>");
					out.println("<td>" + rs.getString(3) + "</td>");
					out.println("<td>" + rs.getDate(4) + "</td>");
					out.println("</tr>");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			out.println("</table>");
			out.println("</body>");
			out.println("</html>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		HttpSession s = request.getSession(false);
		if (s == null) {
			s = request.getSession();
			// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid
			// HTTP session");
		}

		ServletContext sc = getServletContext();
		du = (DatabaseUtil) sc.getAttribute("dbcon");
		if (du == null) {
			du = new DatabaseUtil();
			sc.setAttribute("dbcon", du);
		}
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Documentum Environment Monitor</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Environment Monitor Preferences</h1>");
			out.println("<form id='prefs'>");
			out.println("<table border = '1' style='width:100%'>");
			out.println("<tr>");
			out.println("<th>Service</th>");
			out.println("<th>Status</th>");
			out.println("<th>Version</th>");
			out.println("<th>Last Update</th>");
			out.println("<tr>");

			sds = DocumentumService.getServices();

			if (sds != null) {
				Iterator it = sds.iterator();
				int i = 0;
				String url;
				while (it.hasNext()) {
					ds = (DocumentumService) it.next();
					out.println("<tr>");
					out.println("<td>" + "<input type='text' name='name' form='prefs' value='" + ds.getName() + "'></td>");
					out.println("<td>" + "<input type='text' name='host' form='prefs' value='" + ds.getHost() + "'></td>");
					out.println("<td>" + "<input type='number' name='port' form='prefs' value='" + ds.getPort() + "'></td>");
					out.println("<td>" + "<input type='text' name='docbase' form='prefs' value='" + ds.getDocbase() + "'></td>");
					out.println("<td>" + "<input type='text' name='user' form='prefs' value='" + ds.getUser() + "'</td>");
					out.println("<td>" + "<input type='password' name='password' form='prefs' value='" + ds.getPassword()
							+ "'></td>");
					out.println(
							"<td>" + "<input type='email' name='address' form='prefs' value='" + ds.getAddress() + "'></td>");
					out.println("<td>" + "<select name='type' form='prefs'>"
							+ "<option selected value='" + ds.getType() + "'>" + ds.getType() + "</option>"
							+ "<option value='xplore'>xplore</option>"
							+ "<option value='xcp'>xcp</option>"
							+ "<option value='cs'>cs</option>"
							+ "<option value='cts'>cts</option>"
							+ "<option value='d2'>d2</option>"
							);
					out.println("</tr>");
				}
			}

			out.println("</table>");
			out.println("</body>");
			out.println("</html>");

		}
	}

}
