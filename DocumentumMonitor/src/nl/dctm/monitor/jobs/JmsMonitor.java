package nl.dctm.monitor.jobs;

import static nl.dctm.monitor.utils.HttpServiceUtils.*;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

import nl.dctm.monitor.dao.DAOFactory;
import nl.dctm.monitor.dao.DocumentumServiceDAO;
import nl.dctm.monitor.service.DocumentumService;
import nl.dctm.monitor.utils.DocbaseSessionUtils;
import nl.dctm.monitor.utils.MailSender;

public class JmsMonitor implements Job{

	private static final String JMS_INFO = "/DmMethods/servlet/DoMethod";	
	private DocumentumService ds;
	private MailSender ms = new MailSender();
	final static Logger logger = Logger.getLogger(JmsMonitor.class);

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("jms");
		String result = null;
		
		Iterator<DocumentumService> it = dslist.iterator();

		while (it.hasNext()) {
			ds = it.next();
			
			try {
				result = sendRequest(ds.getHost(), ds.getPort(), "http", JMS_INFO, null, null);			
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!result.equals("Failed")) {				
				ds.setStatus("Running");
			} else {				
				if(ds.getStatus() == null || !ds.getStatus().equals(result)) {
					ds.setStatus(result);
					ms.sendMail(ds);
				}					
			}
			dsdao.update(ds);
		}
	}

//	private boolean isRunning(String result) {
//		if (result.equals("Failed")) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	private String getStatus() throws Exception {
////		String jmsURL;
////		
////		IDfCollection col;
////		IDfQuery query = new DfQuery();
////		StringBuilder queryString = new StringBuilder();
////		queryString.append("select base_uri from dm_jms_config");
////		queryString.append(" where config_type in (1,2)");
////		System.out.println(queryString);
////		query.setDQL(queryString.toString());
////		DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
////		IDfSession session = null;
////		try {
////			session = dsu.getDocbaseSession(ds.getDocbase(), ds.getUser(), ds.getPassword());
////			col = query.execute(session, IDfQuery.DF_EXECREAD_QUERY);
////			while (col.next()) {
////				jmsURL = col.getString("base_uri");
////				System.out.println(jmsURL);
////			}
////		} catch (DfException e) {
////			e.printStackTrace();
////		} finally {
////			try {
////				dsu.releaseSession(session);
////			} catch (DfException e) {
////				e.printStackTrace();
////			}
////		}
//		
//		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http", JMS_INFO, null, null);
//		if (response.equals("Failed")) {
//			logger.info("CTS service is not reachable. ServiceID: " + ds.getId());
//		}
//		return response;
//	}
}
