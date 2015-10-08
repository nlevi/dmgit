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

import com.documentum.fc.common.DfException;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;
import com.emc.monitor.utils.UpdateDFCProperties;

/**
 * Servlet implementation class NewService
 */
@WebServlet("/NewService")
public class NewService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DatabaseUtil du;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

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

		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Documentum Environment Monitor</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Environment Details</h1>");
			out.println(newServiceForm());
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

		DocumentumService ds = new DocumentumService();
		
		ds.setAddress(request.getParameter("address"));
		ds.setDocbase(request.getParameter("docbase"));
		ds.setPassword(request.getParameter("password"));
		ds.setUser(request.getParameter("user"));
		ds.setHost(request.getParameter("host"));
		ds.setPort(Integer.parseInt(request.getParameter("port")));
		ds.setType(request.getParameter("type"));
		ds.setName(request.getParameter("name"));
		ds.save();
		
		if(request.getParameter("type") == "cs") {
			try {
				UpdateDFCProperties.update(request.getParameter("host"), Integer.parseInt(request.getParameter("port")));
			} catch (NumberFormatException | DfException e) {
				e.printStackTrace();
			}
		}
		
		response.sendRedirect("EnvDetails");
	}

	private String newServiceForm() {
		StringBuilder sb = new StringBuilder();
		sb.append("<form action=\"NewService\" method=\"POST\" id=\"prefs\">");
		sb.append("<table border = '1' style='width:100%'>");
		sb.append("<tr>");		
		sb.append("<th>Service</th>");
		sb.append("<th>Host</th>");
		sb.append("<th>Port</th>");
		sb.append("<th>Docbase</th>");
		sb.append("<th>User</th>");
		sb.append("<th>Password</th>");
		sb.append("<th>Email</th>");
		sb.append("<th>Type</th>");
		sb.append("<tr>");
		sb.append("<tr>");		
		sb.append("<td>" + "<input type='text' name='name' form='prefs' value=''></td>");
		sb.append("<td>" + "<input type='text' name='host' form='prefs' value=''></td>");
		sb.append("<td>" + "<input type='number' name='port' form='prefs' value=''></td>");
		sb.append("<td>" + "<input type='text' name='docbase' form='prefs' value=''></td>");
		sb.append("<td>" + "<input type='text' name='user' form='prefs' value=''</td>");
		sb.append("<td>" + "<input type='password' name='password' form='prefs' value=''></td>");
		sb.append("<td>" + "<input type='email' name='address' form='prefs' value=''></td>");
		sb.append("<td>" + "<select name='type' form='prefs'>" 
				+ "<option value='dsearch'>Dsearch</option>"
				+ "<option value='indexagent'>IndexAgent</option>"
				+ "<option value='xcp'>xCP Application</option>" 
				+ "<option value='cs'>Content Server</option>"
				+ "<option value='cts'>CTS</option>" 
				+ "<option value='d2'>D2</option>"
				+ "<option value='bpm'>BPM</option>"
				+ "<option value='jms'>JMS</option>"
				+ "<option value='bps'>BPS</option>");
		sb.append("</tr>");

		sb.append("<input type=\"submit\" value=\"Save\"/>");
		sb.append("</table>");
		sb.append("</form>");
		return sb.toString();
	}

}
