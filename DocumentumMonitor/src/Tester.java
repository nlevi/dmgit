import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.impl.docbroker.DocbrokerMap;
import com.documentum.fc.common.DfLoginInfo;
import com.emc.monitor.jobs.XcpMonitor;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;
import com.emc.monitor.utils.DbProperties;
import com.emc.monitor.utils.DocbaseSessionUtils;


public class Tester {

	public static void main(String[] args) throws Exception {
		Set<DocumentumService> sds;
		DocumentumService tempds;
		String[] url = null;
		
		DatabaseUtil du = new DatabaseUtil();
		
		sds = DocumentumService.getServicesByType("cs");
		
		Iterator it = sds.iterator();
		int i = 0;
		String tmp;
		String tmpFolder = System.getProperty("java.io.tmpdir");
		File file = new File(tmpFolder + File.separator + "dfc.properties");
		if(file.exists()) {
			file.delete();
		}
		System.out.println(tmpFolder);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		while(it.hasNext()) {
			tempds = (DocumentumService) it.next();
			String fileContent = createDfcPropertiesFileContent(tempds.getHost(),tempds.getPort(), i);
			bw.write(fileContent);
			i++;			
		}
		
		bw.close();
		
		DocbrokerMap map = (DocbrokerMap) new DfClient().getDocbrokerMap();
		System.out.println(map.getDocbrokerCount());
		for (int n = 0, limit = map.getDocbrokerCount(); n < limit; n++) {
        System.out.println("Host: " + map.getHostName(n) + " port: " + map.getPortNumber(n));
		}
		
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
		
		DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
		IDfSession session = dsu.getDocbaseSession("dctm72", "dmadmin", "dctm");
		System.out.println(session.getDocbaseId());
		
		
	}
	
	public static String createDfcPropertiesFileContent(String docbrokerHost, int docbrokerPort, int num) {
		String content = 
    			"dfc.docbroker.host[" + num + "]=" + docbrokerHost + "\r\n" +
    			"dfc.docbroker.port[" + num + "]=" + docbrokerPort + "\r\n";
		return content;		
	}

}
