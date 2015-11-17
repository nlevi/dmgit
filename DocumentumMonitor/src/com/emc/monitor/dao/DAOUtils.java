package com.emc.monitor.dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.emc.monitor.service.DocumentumService;

public final class DAOUtils {

    private DAOUtils() {
        
    }

    public static PreparedStatement preparedStmt
        (Connection con, String sql, boolean returnGeneratedKeys, Object... values)
            throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement(sql,
            returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        if(values != null) {
        	setStmtParams(stmt, values);
        }
        return stmt;
    }

    public static void setStmtParams(PreparedStatement stmt, Object... values)
        throws SQLException
    {
        for (int i = 0; i < values.length; i++) {
            stmt.setObject(i + 1, values[i]);
        }
    }
    
    public static Timestamp convertToTimestamp(java.util.Date date) {
     return (date != null) ? new Timestamp(date.getTime()) : null;
    }
    
    public static DocumentumService mapResults(ResultSet resultSet) throws SQLException {
    	DocumentumService dctmService = new DocumentumService();    	
    	dctmService.setServiceId(resultSet.getInt("service_id"));    	
    	dctmService.setAddress(resultSet.getString("address"));    	
    	dctmService.setDocbase(resultSet.getString("docbase"));
    	dctmService.setPassword(resultSet.getString("password"));
    	dctmService.setUser(resultSet.getString("service_user"));
    	dctmService.setPort(resultSet.getInt("service_port"));
    	dctmService.setHost(resultSet.getString("service_host"));
    	dctmService.setType(resultSet.getString("service_type"));
    	dctmService.setName(resultSet.getString("service_name"));
    	dctmService.setStatus(resultSet.getString("service_status"));
    	dctmService.setVersion(resultSet.getString("service_version"));
    	dctmService.setLastUpdate(resultSet.getTimestamp("last_update"));
        return dctmService;
    }
    
    public static boolean tableExists(SQLException ex) {
    	boolean exists;
    	if(ex.getSQLState().equals("X0Y32")) {
    		exists = true;
    	} else {
    		exists = false;
    	}
    	
    	return exists;
    }

}
