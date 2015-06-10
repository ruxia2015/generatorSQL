package generatorSQLScript.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;





public class MysqlUtil
{

    public static String showCreateTable(Connection con,String tableName){    
        
        String rst =null;
        String sql = "show create table "+tableName;
        Statement stmt;
        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next())
            {
                System.out.println(rs.getString(1));
                rst = rs.getString(2);
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return rst;

    }
    
    
    
    
    
    public static String showCommentColumn(){
        
        
        return null;
    }
    

}
