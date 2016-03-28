package application;
import java.sql.DriverManager;
import java.sql.Statement;

public class dbConnector {
	public java.sql.Connection myConn;
    public Statement myStmt;
    public dbConnector(){
        try {
            //1. Getting a connection to database
            
        	//myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dellatest", "root", "adiram");
            myConn = DriverManager.getConnection("jdbc:mysql://192.168.92.62:3306/jyo", "user3","newpassword");
            
            //2.creating the  statement
            myStmt = myConn.createStatement();
        } catch(Exception e){
            System.err.println(e + " aaa");
        }
    }
}