package nl.dctm.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import nl.dctm.monitor.dao.DAOFactory;
import nl.dctm.monitor.dao.DocumentumServiceDAO;
import nl.dctm.monitor.service.DocumentumService;

public class Monitor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// private static final long serialVersionUID = -7681214561115135621L;
	private String params;

	protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.setContentType("text/html;charset=UTF-8");
		HttpSession s = request.getSession();

		response.setContentType("application/json");
		try (PrintWriter out = response.getWriter()) {
			DAOFactory daofactory = DAOFactory.getInstance();
			DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
			DocumentumService ds;
			if (request.getParameter("action").equals("find")) {
				ds = dsdao.getServiceById(Integer.parseInt(request.getParameter("id")));
				JSONObject json = new JSONObject();
				json.put("name", ds.getName());
				json.put("docbase", ds.getDocbase());
				json.put("host", ds.getHost());
				json.put("port", ds.getPort());
				json.put("user", ds.getUser());
				json.put("password", ds.getPassword());
				json.put("email", ds.getAddress());
				json.put("type", ds.getType());
				json.put("status", ds.getStatus());
				json.put("version", ds.getVersion());
				out.print(json.toString());
			} else {
				List<DocumentumService> dslist = dsdao.getAllServices();
				Iterator<DocumentumService> it = dslist.iterator();

				JSONArray jsonArray = new JSONArray();
				Map<String, Object> map;
				while (it.hasNext()) {
					ds = it.next();
					map = new HashMap<String, Object>();
					map.put("id", ds.getId());
					map.put("name", ds.getName());
					map.put("version", ds.getVersion());
					map.put("status", ds.getStatus());
					map.put("lastUpdate", ds.getLastUpdate());

					jsonArray.put(map);

				}
				out.print(jsonArray.toString());
			}
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processGetRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");

		HttpSession s = request.getSession();

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}

		JSONObject jrequest = new JSONObject(sb.toString());

		DocumentumService ds = new DocumentumService();
		ds.setName(jrequest.getString("name"));
		ds.setDocbase(jrequest.getString("docbase"));
		ds.setHost(jrequest.getString("host"));
		ds.setPort(jrequest.getInt("port"));
		ds.setUser(jrequest.getString("user"));
		ds.setPassword(jrequest.getString("password"));
		ds.setAddress(jrequest.getString("email"));
		ds.setType(jrequest.getString("type"));

		DAOFactory daofactory = DAOFactory.getInstance();

		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		dsdao.create(ds);

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DAOFactory daofactory = DAOFactory.getInstance();

		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		dsdao.delete(Integer.parseInt(request.getParameter("id")));
	}
}
