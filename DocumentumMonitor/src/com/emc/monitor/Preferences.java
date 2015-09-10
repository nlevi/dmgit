package com.emc.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.emc.monitor.job.XploreMonitor;
import com.emc.monitor.utils.MonitorUtils;
import com.emc.monitor.utils.DatabaseUtil;

/**
 * Servlet implementation class Preferences
 */
@WebServlet("/Preferences")
public class Preferences extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String dbuser;
	private String dbpassword;
	private String dbname;
	private String dburl;
	private DatabaseUtil du;

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
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid HTTP session");
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
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid HTTP session");
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
			out.println("<h1>Xplore</h1>");
			XploreMonitor xm = new XploreMonitor();

			try {
				xm.worker();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

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

}
