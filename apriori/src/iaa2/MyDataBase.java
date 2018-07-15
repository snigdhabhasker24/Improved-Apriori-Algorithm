package iaa2;
import java.sql.*;
public class MyDataBase {
    private static Connection conn;
     static //Creating connection
     {
        try
        {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            conn=DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=Transaction1.MDB");
        }
        catch(Exception e)
        {
           System.out.println("Exception in creating connection:"+e);
        }
    }
    public static void close()//Closing connection
    {
        try {
            conn.close();
        } catch (SQLException ex){} 
    }
    public static PreparedStatement createStatement(String query) throws SQLException//Creating statement
    {        
            PreparedStatement stmt=conn.prepareStatement(query);
            return stmt;               
    }
    public static ResultSet executeQuery(String query) throws SQLException//Executing ResultSet queries
    {
                PreparedStatement stmt=createStatement(query);
                ResultSet rs=stmt.executeQuery();
                stmt.close();
                return rs;
    }
    public static int executeUpdate(String query) throws SQLException//Executing Database Queries
    {
                PreparedStatement stmt=createStatement(query);
                int n=stmt.executeUpdate();
                stmt.close();
                return n;
                
    }  
}
