package com.emc.monitor;

import com.emc.monitor.utils.DatabaseUtil;
import com.emc.monitor.utils.MonitorUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MonitorUI
 */

public class MonitorUI extends HttpServlet {
	// private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	// private static final long serialVersionUID = -7681214561115135621L;
	private String params;
	private DatabaseUtil du;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.setContentType("text/html;charset=UTF-8");
		HttpSession s = request.getSession();
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
			// out.println("<meta http-equiv='Content-Style-Type'
			// content='text/css'>");
			// out.println("<link rel='stylesheet' type='text/css'
			// href='bootstrap.css'>");
			out.println("<title>Documentum Environment Monitor</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>" + du.executeSelect("select count(*) from mntr_env_details").toString() + "</h1>");
			out.println("<h1>" + du + "</h1>");
			out.println("<table border = '1'>");
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
			out.println(preferencesForm());
			out.println("</body>");
			out.println("</html>");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		HttpSession s = request.getSession();

		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>iStore registration</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h3> User host: " + request.getRemoteHost() + ":" + request.getRemotePort() + "</h3>");
			out.println(params);
			out.println("</body>");
			out.println("</html>");
		}
	}

	private String preferencesForm() {
		StringBuilder sb = new StringBuilder();
		sb.append("<form action=\"Preferences\" method=\"POST\">");
		sb.append("<input type=\"submit\" value=\"Modify Environment Details\"/>");
		sb.append("</form>");
		return sb.toString();
	}

}
