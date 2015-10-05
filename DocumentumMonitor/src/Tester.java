import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.emc.documentum.core.fulltext.client.admin.api.FtAdminFactory;
import com.emc.documentum.core.fulltext.client.admin.api.IFtAdminService;
import com.emc.documentum.core.fulltext.client.admin.api.interfaces.IFtAdminSystem;
import com.emc.monitor.job.XcpMonitor;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;
import com.emc.monitor.utils.DbProperties;


public class Tester {

	public static void main(String[] args) throws Exception {
//		Set<DocumentumService> sds;
//		DocumentumService tempds;
//		String[] url = null;
//		
		//DatabaseUtil du = new DatabaseUtil();
//		
//		sds = DocumentumService.getServicesByType("xplore");
//		
//		Iterator it = sds.iterator();
//		int i = 0;
//		String tmp;
//		while(it.hasNext()) {
//			tempds = (DocumentumService) it.next();
//			i++;			
//			tmp = "http://" + tempds.getHost() + ":" + tempds.getPort() + "/dsearch";
//			url[i] = tmp;
//			System.out.println(url[i]);
//			
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
//		System.out.println(dut.executeSelect("select * from mntr_env_status"));
//		dut.closeConnection();
		
		
//		XcpMonitor xm = new XcpMonitor();
//		xm.execute();
		
		IFtAdminService adminService = FtAdminFactory.getAdminService("10.76.251.42", 9300, "dctmdctm");
		
		String ver = adminService.getVersion();
		List<String> xstatus = adminService.getStatus();
		Iterator it = xstatus.iterator();
		String status;
		while (it.hasNext()) {
			status = (String) it.next();
			System.out.println(status);
		}
		System.out.println(ver);
		
	}

}
