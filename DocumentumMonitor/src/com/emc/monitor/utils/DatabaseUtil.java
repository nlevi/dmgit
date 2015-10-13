package com.emc.monitor.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

	public static Connection conn;
    private String user;
    private String password;
    private Statement stmt;
    private String dburl;
    private String driverclass;
	
    
    public DatabaseUtil() {
    	DbProperties dbp = new DbProperties();
		this.user = dbp.getUser();
		this.password = dbp.getPwd();
		this.dburl = dbp.getJdbcUrl().concat(dbp.getDatabase());
		this.driverclass = dbp.getDriverClass();
		setConnection();
	}
    
    public void setConnection() {
        try {        	
        	Class.forName(driverclass);
            conn = DriverManager.getConnection(dburl, user, password);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
    
    public ResultSet executeSelect(String sql) {
		ResultSet rs = null;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			rs = stmt.executeQuery(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
    	
    }

	public int executeInsert(String sql) {
		
		int r = 0;
		try {
			System.out.println(conn.toString());
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			System.out.println(sql);
			r = stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
		
	}
	
	public void closeConnection() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error #3: " + ex.getMessage());
        }
    }
	
}
