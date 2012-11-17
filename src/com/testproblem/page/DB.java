package com.testproblem.page;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DB {
	static String url = "jdbc:postgresql:testdb";
    static String user = "postgres";
    static String password = "miras";
	
	public static Connection createConnection() throws SQLException, ClassNotFoundException{
		Class.forName("org.postgresql.Driver");
		
		return DriverManager.getConnection(url, user, password);
	}
    
	public static void closeConnection(Connection con, ResultSet rs, PreparedStatement pst){
		
		
		 try {
             if (rs != null) {
                 rs.close();
             }
             
             if (pst != null) {
                 pst.close();
             }
             if (con != null) {
                 con.close();
             }

         } catch (SQLException ex) {
             Logger lgr = Logger.getLogger(Admin.class.getName());
             lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
	}
}
