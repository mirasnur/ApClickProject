package com.testproblem.page;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.click.ActionListener;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.Table;
import org.apache.click.control.TextArea;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.FieldColumn;



public class Admin extends Page {

    private static final long serialVersionUID = 1L;
 
    private Table table = new Table("table");
    private ActionLink readLink = new ActionLink("r", this, "onReadClick");
    private ActionLink unreadLink = new ActionLink("u", this, "onUnreadClick");
    public String status; 
          
    
   public class Row {
        
    	private int Id;
    	private String email;
        private String telephone;
        private String message;
        private boolean back;
        
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public boolean getBack() {
			return back;
		}
		public void setBack(boolean back) {
			this.back = back;
		}
		public int getId() {
			return Id;
		}
		public void setId(int id) {
			Id = id;
		}
		
		
		
    }
    

    // Constructor ------------------------------------------------------------

    public Admin() {
        // Add controls
    	
        addControl(table);
        addControl(readLink);
        addControl(unreadLink);
        // Setup table
        table.setClass(Table.CLASS_ITS);
        table.setPageSize(5);
        table.setShowBanner(true);
        table.setSortable(true);
        
        table.addColumn(new Column("id"));
        
        table.addColumn(new Column("email"));

        table.addColumn(new Column("telephone"));
        
        
        TextArea msg = new TextArea();
        msg.setReadonly(true);
        
        FieldColumn fcolumn = new FieldColumn("message",msg);
        table.addColumn(fcolumn);

        Column column = new Column("Read/Unread");
        column.setSortable(false);
        column.setDecorator(new Decorator() {
            public String render(Object row, Context context) {
                Row user = (Row) row;
                String id = String.valueOf(user.getId());
                                          
                if(user.getBack()) status = "READ"; else status = "UNREAD";
                
                readLink.setValue(id);
                unreadLink.setValue(id);
                
                return status + " " + readLink.toString() + " | " + unreadLink.toString();
            }

        });
        table.addColumn(column);

             
        table.setDataProvider(new DataProvider<Row>() {

            private static final long serialVersionUID = 1L;

            public List<Row> getData() {
                return getRows();
            }
        });
        
        table.getControlLink().setActionListener(new ActionListener() {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean onAction(Control source) {
                
                table.saveState(getContext()); 
                return true;
            }
        });

        // Restore the table sort and paging state from the session between requests
        table.restoreState(getContext()); 
     
        
        
    }

     // Public Methods ---------------------------------------------------------
    public boolean onReadClick() {
    	 
    	Integer id = readLink.getValueInteger();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                      
       
        
        
        try {
            
            con = DB.createConnection();
                      
            String stm = "UPDATE users SET back = ? WHERE id = ?";
            pst = con.prepareStatement(stm);
            pst.setBoolean(1,true);
            pst.setInt(2,id);
            pst.executeUpdate();
            
            
                        
        } catch (SQLException | ClassNotFoundException ex) {
                Logger lgr = Logger.getLogger(Admin.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

        	DB.closeConnection(con,rs,pst);
        }
        
        return true;
    }
   
    
    public boolean onUnreadClick() {
    	 
    	Integer id = unreadLink.getValueInteger();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                      
       
        
        try {
            
        	con = DB.createConnection();
                      
            String stm = "UPDATE users SET back = ? WHERE id = ?";
            pst = con.prepareStatement(stm);
            pst.setBoolean(1,false);
            pst.setInt(2,id);
            pst.executeUpdate();
            
            
                        
        } catch (SQLException | ClassNotFoundException ex) {
                Logger lgr = Logger.getLogger(Admin.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

        	DB.closeConnection(con,rs,pst);
        }
       
        return true;
    } 
	


    
    
     public List<Row> getRows() {
    	
    	 Connection con = null;
         PreparedStatement pst = null;
         ResultSet rs = null;
         Row row;
         List<Row> rowlist = Collections.synchronizedList( new ArrayList<Row>());
                  
         try {
             
        	 con = DB.createConnection(); 
             pst = con.prepareStatement("SELECT * FROM users ORDER BY id DESC");
             rs = pst.executeQuery();
             
             
             
             while (rs.next()) {
                 row = new Row();
                 row.setId(rs.getInt("id"));
            	 row.setEmail(rs.getString("email"));
                 row.setTelephone(rs.getString("telephone"));
                 row.setMessage(rs.getString("message"));
                 row.setBack(rs.getBoolean("back"));
                 rowlist.add(row);
             }

         } catch (SQLException | ClassNotFoundException ex) {
                 Logger lgr = Logger.getLogger(Admin.class.getName());
                 lgr.log(Level.SEVERE, ex.getMessage(), ex);

         } finally {

        	 DB.closeConnection(con,rs,pst);
         }
         return rowlist;
    }

    
}