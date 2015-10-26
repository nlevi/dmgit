import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.impl.docbroker.DocbrokerMap;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.jobs.XcpMonitor;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;
import com.emc.monitor.utils.DbProperties;
import com.emc.monitor.utils.DocbaseSessionUtils;


public class Tester {

	public static void main(String[] args) throws Exception {
//		Set<DocumentumService> sds;
//		DocumentumService tempds;
//		String[] url = null;
//		
//		DatabaseUtil du = new DatabaseUtil();
//		
//		sds = DocumentumService.getInstance().getServicesByType("cs");
//		
//		Iterator it = sds.iterator();
//		int i = 0;
//		String tmp;
//		String tmpFolder = System.getProperty("java.io.tmpdir");
//		File file = new File(tmpFolder + File.separator + "dfc.properties");
//		if(file.exists()) {
//			file.delete();
//		}
//		System.out.println(tmpFolder);
//		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
//		while(it.hasNext()) {
//			tempds = (DocumentumService) it.next();
//			String fileContent = createDfcPropertiesFileContent(tempds.getHost(),tempds.getPort(), i);
//			bw.write(fileContent);
//			i++;			
//		}
//		
//		bw.close();
//		
//		DocbrokerMap map = (DocbrokerMap) new DfClient().getDocbrokerMap();
//		System.out.println(map.getDocbrokerCount());
//		for (int n = 0, limit = map.getDocbrokerCount(); n < limit; n++) {
//        System.out.println("Host: " + map.getHostName(n) + " port: " + map.getPortNumber(n));
//		}
		
//		DbProperties dbp = new DbProperties();
//		System.out.println(dbp.getDatabase());
//		System.out.println(dbp.getJdbcUrl());
//		System.out.println(dbp.getPwd());
//		System.out.println(dbp.getUser());
//		System.out.println(dbp.getDriverClass());
//		
//		DatabaseUtil dut = new DatabaseUtil();
//		System.out.println(dut.conn);
//		ResultSet rs = dut.executeSelect("select * from mntr_env_status");
//		while(rs.next()) {
//			System.out.println("OK");
//		}
		
		
//		XcpMonitor xm = new XcpMonitor();
//		xm.execute();
		
//		IFtAdminService adminService = FtAdminFactory.getAdminService("10.76.251.42", 9300, "dctmdctm");
//		
//		String ver = adminService.getVersion();
//		List<String> xstatus = adminService.getStatus();
//		Iterator it = xstatus.iterator();
//		String status;
//		while (it.hasNext()) {
//			status = (String) it.next();
//			System.out.println(status);
//		}
//		System.out.println(ver);
//		
		
//		IDfSessionManager sessionManager = (new DfClientX()).getLocalClient().newSessionManager();
//        sessionManager.setIdentity("dctm72", new DfLoginInfo("dmadmin","dctm"));
//        IDfSession session = sessionManager.getSession("dctm72");
//        System.out.println(session.getDocbaseName());
//        session.get
		
//		DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
//		IDfSession session = dsu.getDocbaseSession("dctm72", "dmadmin", "dctm");
//		System.out.println(session.getDocbaseId());
//		
//		
//	}
//	
//	public static String createDfcPropertiesFileContent(String docbrokerHost, int docbrokerPort, int num) {
//		String content = 
//    			"dfc.docbroker.host[" + num + "]=" + docbrokerHost + "\r\n" +
//    			"dfc.docbroker.port[" + num + "]=" + docbrokerPort + "\r\n";
//		return content;		
//	}
		
//		Socket socket = new Socket("10.76.251.83", 1489);
//		socket.close();
//	}
		
//	HttpURLConnection con;
//	
//	
//	String jmsURL;
//	
//	IDfCollection col = null;
//	IDfQuery query = new DfQuery();
//	StringBuilder queryString = new StringBuilder();
//	queryString.append("select base_uri from dm_jms_config");
//	queryString.append(" where config_type in (1,2)");
//	System.out.println(queryString);
//	query.setDQL(queryString.toString());
//	DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
//	IDfSession session = null;	
//	try {
//		session = dsu.getDocbaseSession("cs71", "dmadmin", "dctm");
//		col = query.execute(session, IDfQuery.DF_EXECREAD_QUERY);
//		while (col.next()) {
//			jmsURL = col.getString("base_uri");
//			System.out.println(jmsURL);
//		}	
//		
//	} catch (DfException e) {
//		e.printStackTrace();
//	} finally {
//		try {
//			col.close();
//			dsu.releaseSession(session);
//		} catch (DfException e) {
//			e.printStackTrace();
//		}
//	}
		
//		DAOFactory daofactory = DAOFactory.getInstance();
//		
//		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
//		
//		Set<DocumentumService> allds = dsdao.getAllServices();
//		System.out.println("List of users successfully queried: " + allds);
//        System.out.println("Thus, amount of users in database is: " + allds.size());
	
		
	}
	}
