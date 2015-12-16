package com.emc.monitor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.emc.monitor.service.DocumentumService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MailSender {
	
	final static Logger logger = Logger.getLogger(MailSender.class);
	
	public MailSender() {
		
	}

	public void sendMail(DocumentumService service) {
		
		if (service.getAddress() == null) {
			if(logger.isDebugEnabled()) {
				logger.debug("No email address is set for " + service.getId() + ":" + service.getName());
			}
			return;
		}

		Properties prop = new Properties();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream pFile = classLoader.getResourceAsStream("smtp.properties");

		try {
			prop.load(pFile);

			Session session = Session.getDefaultInstance(prop);
			System.out.println(session.getProperties());

			MimeMessage message = new MimeMessage(session);

			message.setFrom(prop.getProperty("mail.from"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(service.getAddress()));
			message.setSubject("ALERT: Service " + service.getId() + ":" + service.getName() + " is not reachable!");
						
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
			cfg.setClassForTemplateLoading(this.getClass(),"/");			
			
			Template template = cfg.getTemplate("mail_template.ftl");
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("name", service.getName());
			paramMap.put("docbase", service.getDocbase());
			paramMap.put("status", service.getStatus());
			paramMap.put("host", service.getHost());
			paramMap.put("port", service.getPort());
			paramMap.put("user", service.getUser());
			paramMap.put("email", service.getAddress());
			paramMap.put("type", service.getType());
//			paramMap.put("version", service.getVersion());
			
			Writer output = new StringWriter();
			template.process(paramMap, output);
			message.setContent(output.toString(), "text/html");
			
			Transport.send(message);
		} catch (IOException | MessagingException | TemplateException e) {
			logger.warn("Failed to send notification", e);			
		}

	}

}
