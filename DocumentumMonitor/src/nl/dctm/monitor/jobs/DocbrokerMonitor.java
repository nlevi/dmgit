package nl.dctm.monitor.jobs;

import static nl.dctm.monitor.utils.HttpResponseParser.getVersionFromResponse;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import nl.dctm.monitor.dao.DAOFactory;
import nl.dctm.monitor.dao.DocumentumServiceDAO;
import nl.dctm.monitor.service.DocumentumService;
import nl.dctm.monitor.utils.MailSender;

public class DocbrokerMonitor implements Job {

	private DocumentumService ds;
	private MailSender ms = new MailSender();
	final static Logger logger = Logger.getLogger(DocbrokerMonitor.class);
	
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();

		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("dkbrkr");
		String result = null;

		Iterator<DocumentumService> it = dslist.iterator();

		while (it.hasNext()) {
			ds = it.next();
			try {
				result = getStatus();
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

	private String getStatus() {
		String response = "Running";
		Socket socket = null;
		try {
			socket = new Socket(ds.getHost(), ds.getPort());
		} catch (IOException e) {
			logger.warn("Docbroker service not reachable. Service ID: " + ds.getId());
			response = "Failed";
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
}
