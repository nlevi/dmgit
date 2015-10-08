package com.emc.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;

/**
 * Servlet implementation class Preferences
 */
@WebServlet("/EnvDetails")
public class EnvDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DatabaseUtil du;	
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
			out.println("<h1>Environment Details</h1>");
			out.println(detailsForm());
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
			// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid HTTP session");
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
			out.println("<h1>Environment Details</h1>");
			out.println(detailsForm());
			out.println("</body>");
			out.println("</html>");

		}
	}

	private String detailsForm() {
		StringBuilder sb = new StringBuilder();
		sb.append("<form action=\"NewService\" method=\"GET\" id=\"prefs\">");
		sb.append("<table border = '1' style='width:100%'>");
		sb.append("<tr>");
		sb.append("<th>#</th>");
		sb.append("<th>Service</th>");
		sb.append("<th>Host</th>");
		sb.append("<th>Port</th>");
		sb.append("<th>Docbase</th>");
		sb.append("<th>User</th>");
		sb.append("<th>Password</th>");
		sb.append("<th>Email</th>");
		sb.append("<th>Type</th>");
		sb.append("<tr>");

		Set<DocumentumService> sds = DocumentumService.getInstance().getServices();

		if (sds != null) {
			Iterator it = sds.iterator();
			int i = 0;
			String url;			
			while (it.hasNext()) {
				ds = (DocumentumService) it.next();
				sb.append("<tr>");
				sb.append("<td>" + ds.getId() + "</td>");
				sb.append("<td>" + ds.getName() + "</td>");
				sb.append("<td>" + ds.getHost() + "</td>");
				sb.append("<td>" + ds.getPort() + "</td>");
				sb.append("<td>" + ds.getDocbase() + "</td>");
				sb.append("<td>" + ds.getUser() + "</td>");
				sb.append("<td>" + ds.getPassword() + "</td>");
				sb.append("<td>" + ds.getAddress() + "</td>");
				sb.append("<td>" + ds.getType() + "</tr>");
			}
		}
		sb.append("<input type=\"submit\" value=\"Add\"/>");
		sb.append("</table>");
		sb.append("</form>");
		return sb.toString();
	}

}
