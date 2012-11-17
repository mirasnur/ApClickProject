package com.testproblem.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.click.Page;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.TextArea;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.EmailField;
import org.apache.click.extras.control.TelephoneField;

public class User extends Page {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Checkbox chbox;
	private Form form1 = new Form("form1");
	
	 public User() {
	        addControl(form1);
	        
	        form1.setColumns(1);

	        FieldSet fieldSet = new FieldSet("fieldSet", "Send a Message to Admin");
	        form1.add(fieldSet);
	             
	        fieldSet.add(new EmailField("email"), 5);
            	       
	        fieldSet.add(new TelephoneField("telephone"));
            	        	        
	        fieldSet.add(new TextArea("message", 39, 5), 2);

	        form1.add(new Submit("ok", " Send ", this, "onOkClicked"));
	        
	 }
	// Event Handlers ---------------------------------------------------------

	    /**
	     * Handle the form submit event.
	     */
	 public boolean onOkClicked() {
	        if (form1.isValid()) {
	        	Connection con = null;
	            PreparedStatement pst = null;
                	                        
	            try {

	                String email = form1.getFieldValue("email");
	                String telephone = form1.getFieldValue("telephone");
	                String message = form1.getFieldValue("message");
	                boolean back = false;
	                
	                con = DB.createConnection();
                    String stm = "INSERT INTO users (email, telephone, message, back) VALUES(?, ?, ?, ?)";
	                pst = con.prepareStatement(stm);
	                pst.setString(1, email);
	                pst.setString(2,telephone);
	                pst.setString(3,message);
	                pst.setBoolean(4, back);
	                pst.executeUpdate();

	            } catch (SQLException | ClassNotFoundException ex) {
	                Logger lgr = Logger.getLogger(User.class.getName());
	                lgr.log(Level.SEVERE, ex.getMessage(), ex);

	            } finally {

	                try {
	                    if (pst != null) {
	                        pst.close();
	                    }
	                    if (con != null) {
	                        con.close();
	                    }

	                } catch (SQLException ex) {
	                    Logger lgr = Logger.getLogger(User.class.getName());
	                    lgr.log(Level.SEVERE, ex.getMessage(), ex);
	                }
	            }
	            form1.clearValues();
	            String msg = "Your message has been sent !";
	            addModel("msg", msg);
	        }
	        
	       
	        return true;
	    }
	 
	 
	
}
