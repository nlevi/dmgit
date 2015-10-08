package com.emc.monitor;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.emc.monitor.utils.UpdateDFCProperties;

/**
 * Servlet implementation class PopulateData
 */
@WebServlet("/PopulateData")
public class PopulateData extends HttpServlet {
	private static final long serialVersionUID = 1L;     
    
	public void init() throws ServletException {
		try {
			UpdateDFCProperties.createDfcProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
