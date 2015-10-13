package com.emc.monitor.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtilOrigin {

	public static Connection conn;
    private static String user;
    private static String password;
    private static Statement stmt;
    private String tablename;
    private String dbname;
    private static String dburl;
	
    
    public DatabaseUtilOrigin(String user, String password, String dbname,
			String dburl) {
		this.user = user;
		this.password = password;
		this.dbname = dbname;
		this.dburl = dburl + dbname;
		setConnection();
	}
    
    public static void setConnection() {
        try {
        	Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(dburl, user, password);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
    
    public static ResultSet executeSelect(String sql) {
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

	public static int executeInsert(String sql) {
		
		int r = 0;
		try {
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
	
	public static void closeConnection() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error #3: " + ex.getMessage());
        }
    }
	
}
