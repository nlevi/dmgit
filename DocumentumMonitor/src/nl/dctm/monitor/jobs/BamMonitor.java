package nl.dctm.monitor.jobs;

import static nl.dctm.monitor.utils.HttpResponseParser.getVersionFromResponse;
import static nl.dctm.monitor.utils.HttpServiceUtils.sendRequest;

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

public class BamMonitor implements Job {

	private DocumentumService ds;
	private static final String BAM_INFO = "/bam-server/admin?action=version";
	private static final String[] versionTags = {"major","minor","build-number"};
	private MailSender ms = new MailSender();
	final static Logger logger = Logger.getLogger(BamMonitor.class);

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("bam");
		String result = null;
		
		Iterator<DocumentumService> it = dslist.iterator();
		while (it.hasNext()) {
			ds = it.next();

			try {
				result = sendRequest(ds.getHost(), ds.getPort(), "http", BAM_INFO, ds.getUser(), ds.getPassword());				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!result.equals("Failed")) {
				ds.setVersion(getVersionFromResponse(result,versionTags));
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
}
