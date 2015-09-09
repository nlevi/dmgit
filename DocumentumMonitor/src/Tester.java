import java.util.Iterator;
import java.util.Set;



import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;
import com.emc.monitor.utils.DatabaseUtilTest;
import com.emc.monitor.utils.DbProperties;


public class Tester {

	public static void main(String[] args) throws Exception {
//		Set<DocumentumService> sds;
//		DocumentumService tempds;
//		String[] url = null;
//		
//		DatabaseUtil du = new DatabaseUtil("root", "root", "myDB","jdbc:derby://localhost:1527/");
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
		
		DbProperties dbp = new DbProperties();
		System.out.println(dbp.getDatabase());
		System.out.println(dbp.getJdbcUrl());
		System.out.println(dbp.getPwd());
		System.out.println(dbp.getUser());
		System.out.println(dbp.getDriverClass());
		
		DatabaseUtilTest dut = new DatabaseUtilTest();
		System.out.println(dut.conn);
		System.out.println(dut.executeSelect("select * from mntr_env_status"));
	}

}
