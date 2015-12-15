import static com.emc.monitor.dao.DAOUtils.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.documentum.fc.common.DfException;
import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DAOUtils;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.UpdateDFCProperties;

public class Tester {

	public static void main(String[] args) throws IOException, MessagingException {
		// Set<DocumentumService> sds;
		// DocumentumService tempds;
		// String[] url = null;
		//
		// DatabaseUtil du = new DatabaseUtil();
		//
		// sds = DocumentumService.getInstance().getServicesByType("cs");
		//
		// Iterator it = sds.iterator();
		// int i = 0;
		// String tmp;
		// String tmpFolder = System.getProperty("java.io.tmpdir");
		// File file = new File(tmpFolder + File.separator + "dfc.properties");
		// if(file.exists()) {
		// file.delete();
		// }
		// System.out.println(tmpFolder);
		// BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		// while(it.hasNext()) {
		// tempds = (DocumentumService) it.next();
		// String fileContent =
		// createDfcPropertiesFileContent(tempds.getHost(),tempds.getPort(), i);
		// bw.write(fileContent);
		// i++;
		// }
		//
		// bw.close();
		//
		// DocbrokerMap map = (DocbrokerMap) new DfClient().getDocbrokerMap();
		// System.out.println(map.getDocbrokerCount());
		// for (int n = 0, limit = map.getDocbrokerCount(); n < limit; n++) {
		// System.out.println("Host: " + map.getHostName(n) + " port: " +
		// map.getPortNumber(n));
		// }

		// DbProperties dbp = new DbProperties();
		// System.out.println(dbp.getDatabase());
		// System.out.println(dbp.getJdbcUrl());
		// System.out.println(dbp.getPwd());
		// System.out.println(dbp.getUser());
		// System.out.println(dbp.getDriverClass());
		//
		// DatabaseUtil dut = new DatabaseUtil();
		// System.out.println(dut.conn);
		// ResultSet rs = dut.executeSelect("select * from mntr_env_status");
		// while(rs.next()) {
		// System.out.println("OK");
		// }

		// XcpMonitor xm = new XcpMonitor();
		// xm.execute();

		// IFtAdminService adminService =
		// FtAdminFactory.getAdminService("10.76.251.42", 9300, "dctmdctm");
		//
		// String ver = adminService.getVersion();
		// List<String> xstatus = adminService.getStatus();
		// Iterator it = xstatus.iterator();
		// String status;
		// while (it.hasNext()) {
		// status = (String) it.next();
		// System.out.println(status);
		// }
		// System.out.println(ver);
		//

		// IDfSessionManager sessionManager = (new
		// DfClientX()).getLocalClient().newSessionManager();
		// sessionManager.setIdentity("dctm72", new DfLoginInfo("dmadmin",
		// "dctm"));
		// IDfSession session = null;
		// String result = "";
		// try {
		// session = sessionManager.getSession("dctm72.dctm72");
		// } catch (DfException de) {
		// System.out.println("Something went wrong");
		// result = "Failed";
		// } finally {
		// if (result == "") {
		// System.out.println("Docbase name: " + session.getDocbaseName());
		// System.out.println("DBMS name: " + session.getDBMSName());
		// System.out.println("Connected CS version: " +
		// session.getServerVersion());
		// System.out.println("Connected CS version: " +
		// session.getServerVersion().replaceAll("[^0-9&&[^\\.]]", ""));
		// sessionManager.release(session);
		// }
		// }

		// DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
		// IDfSession session = dsu.getDocbaseSession("dctm72", "dmadmin",
		// "dctm");
		// System.out.println(session.getDocbaseId());
		//
		//
		// }
		//
		// public static String createDfcPropertiesFileContent(String
		// docbrokerHost, int docbrokerPort, int num) {
		// String content =
		// "dfc.docbroker.host[" + num + "]=" + docbrokerHost + "\r\n" +
		// "dfc.docbroker.port[" + num + "]=" + docbrokerPort + "\r\n";
		// return content;
		// }

		// Socket socket = new Socket("10.76.251.83", 1489);
		// socket.close();
		// }

		// HttpURLConnection con;
		//
		//
		// String jmsURL;
		//
		// IDfCollection col = null;
		// IDfQuery query = new DfQuery();
		// StringBuilder queryString = new StringBuilder();
		// queryString.append("select base_uri from dm_jms_config");
		// queryString.append(" where config_type in (1,2)");
		// System.out.println(queryString);
		// query.setDQL(queryString.toString());
		// DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
		// IDfSession session = null;
		// try {
		// session = dsu.getDocbaseSession("cs71", "dmadmin", "dctm");
		// col = query.execute(session, IDfQuery.DF_EXECREAD_QUERY);
		// while (col.next()) {
		// jmsURL = col.getString("base_uri");
		// System.out.println(jmsURL);
		// }
		//
		// } catch (DfException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// col.close();
		// dsu.releaseSession(session);
		// } catch (DfException e) {
		// e.printStackTrace();
		// }
		// }

		// DAOFactory daofactory = DAOFactory.getInstance();
		//
		// DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		//
		// DocumentumService ds = dsdao.getServiceById(1);
		// Iterator<DocumentumService> it = allds.iterator();

		// JSONArray json = new JSONArray();
		// json.put(allds);
		// Map<String, Object> map;
		// while (it.hasNext()) {
		// ds = it.next();
		// map = new HashMap<String, Object>();
		// map.put("id", ds.getId());
		// map.put("name", ds.getName());
		// map.put("version", ds.getVersion());
		// map.put("status", ds.getStatus());
		// map.put("lastUpdate", ds.getLastUpdate());
		//
		// json.put(map);
		//
		// }

		// json.accumulate("services", allds);

		// JSONObject json = new JSONObject();
		// json.put("name", ds.getName());
		// json.put("docbase", ds.getDocbase());
		// json.put("host", ds.getHost());
		// json.put("port", ds.getPort());
		// json.put("user", ds.getUser());
		// json.put("password", ds.getPassword());
		// json.put("email", ds.getAddress());
		// json.put("type", ds.getType());

		// System.out.println("List of users successfully queried: " + allds);
		// System.out.println("Thus, amount of users in database is: " +
		// allds.size());
		// System.out.println(json.toString());

//		String html = "<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'><html><head><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'><title>xMS-Agent 1.2.0030.0447</title></head><body style='margin-left:20px;'><font size='2' face='verdana'>    <div><b>xMS-Agent version: </b>1.2.0030.0447</div><br /><div><b>xMS-Agent health:</b><div class='valid'>Ok</div></div><br /><table class='statustable'><tr><td colspan='2' align='center'><b>Status information</b></td></tr><tr><td>xMS Server Home:</td><td>/opt/tcserver/72/xms2/webapps/xms-agent/WEB-INF</td></tr><tr><td>Vix Library path:</td><td>/opt/tcserver/72/xms2/webapps/xms-agent/WEB-INF/lib/vix/linux64</td></tr><tr><td>Environment name [mode]:</td><td>Dev22 [Development]</td></tr></table></font></body></html>";

//		List<String> versions = new ArrayList<>();
//		Document doc = Jsoup.parse(html);
//		Elements modules = doc.select("li");
//		String tmp;
//		if (!modules.isEmpty()) {
//			for (Element module : modules) {
//
//				tmp = module.text().replaceAll("[^0-9&&[^\\.]]", "");
//				if (tmp.length() == 13) {
//					versions.add(tmp);
//				}
//			}
			
//			String version = null;
//			List<String> versions = new ArrayList<>();
//			
//			Document doc;
//			doc = Jsoup.parse(html);
//			
//			Elements modules = doc.select("div");
//			for (Element module : modules) {
//				String tmp = module.text().replaceAll("[^0-9&&[^\\.]]", "");
//				if (tmp.length() == 13) {
//					versions.add(tmp);
//				}
//			}		
			
			
//			version = versions.get(0);
//			System.out.println(version);
			

//			for (int i = 0; i < versions.size() - 1; i++) {
//				for (int j = i + 1; j < versions.size(); j++) {
//					if(Objects.equals(versions.get(i), versions.get(j))) {
//						
//					}
//
//				}
//			}
//		try{
//		DAOFactory daofactory = DAOFactory.getInstance();
//		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
//		String stmt = "create table mntr_env_details (service_id integer not null generated always as identity (start with 1, increment by 1), service_name varchar(255) not null, docbase varchar(255), service_host varchar(255) not null, service_port integer not null, service_type varchar(128) not null, service_status varchar(32), service_version varchar(255), last_update timestamp, service_user varchar(255), password varchar(255), address varchar(255))";
//		
//		dsdao.execQuery(stmt);
//		} catch (SQLException ex) {
//			if(tableExists(ex)) {
//				System.out.println("Table already exsists.");
//			}
//		}
		
//		String to = "dmadmin@dctm71";
//		Properties prop = new Properties();
//		
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		InputStream pFile = classLoader.getResourceAsStream("mail.properties");
//		
//		FileReader f = new FileReader(new File(("d:/monitor/dmgit/DocumentumMonitor/src/smtp.properties")));
//				
//		prop.load(f);
//		
//		Session session = Session.getDefaultInstance(prop);
//		System.out.println(session.getProperties());
//		
//		MimeMessage message = new MimeMessage(session);
//		
//		message.setFrom(prop.getProperty("mail.from"));
//		message.addRecipient(Message.RecipientType.TO,new InternetAddress("dmadmin@dctm71"));
//		message.setSubject("Mail from MonitorApp");		
//		message.setText("This is email from Monitor Application");
//		
//		Transport.send(message);
		
		try {
			UpdateDFCProperties.createDfcProperties();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}
//}
