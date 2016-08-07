package generatorSQLScript.util;

import generatorSQLScript.mysql.bean.MysqlColumnBean;
import generatorSQLScript.mysql.bean.MysqlConBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtil
{
    public static Connection connect(String url)
    {
        
        Connection con = null;
        try
        {
            
            con = DriverManager.getConnection(url);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return con;
    }
    
    public static Connection connect(MysqlConBean conBean)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("jdbc:mysql://" + conBean.getHost() + ":" + conBean.getPort()
                + "/");
        sb.append(conBean.getDbName() + "?user=" + conBean.getUserName());
        sb.append("&password=" + conBean.getPassword());
        
        Connection con = null;
        try
        {
            con = DriverManager.getConnection(sb.toString());
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return con;
    }
    
    public static List queryAllTables(Connection con)
    {
        List<String> list = new ArrayList<String>();
        
        String sql = "show tables";
        Statement stmt;
        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next())
            {
                list.add(rs.getString(1));
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return list;
    }
    
    public static Map<String, MysqlColumnBean> queryTableColumns(
            String tableName, Connection con)
    {
        Map<String, MysqlColumnBean> list = new HashMap<String, MysqlColumnBean>();
        
        String sql = "select * from " + tableName +" where 1 = 1";
        
        try
        {
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                String colname = rsmd.getColumnName(i);
                // 获得指定列的数据类型名  
                String typeName = rsmd.getColumnTypeName(i);
                // 获得指定列的数据类型  
                int itype = rsmd.getColumnType(i);
                // 默认的列的标题  
                String columnLabel = rsmd.getColumnLabel(i);
                int size = rsmd.getColumnDisplaySize(i);
                // 某列类型的精确度(类型的长度)  
                int precision = rsmd.getPrecision(i);
                // 是否为空  
                int n = rsmd.isNullable(i);
                // 小数点后的位数  
                int scale = rsmd.getScale(i);
                boolean nullable = true;
                boolean isAutoIncre = rsmd.isAutoIncrement(i);
                switch (n)
                {
                    case 0: // '/0'
                        nullable = false;
                        break;
                    
                    case 1: // '/001'
                        nullable = true;
                        break;
                    
                    default:
                        nullable = true;
                        break;
                }
                
                MysqlColumnBean mysqlColumnBean = new MysqlColumnBean();
                mysqlColumnBean.setColumnName(colname);
                mysqlColumnBean.setTypeName(typeName);
                mysqlColumnBean.setItype(itype);
                mysqlColumnBean.setSize(size);
                mysqlColumnBean.setPrecision(precision);
                mysqlColumnBean.setScale(scale);
                mysqlColumnBean.setNullable(nullable);
                mysqlColumnBean.setAutoIncrement(isAutoIncre);
                
                list.put(colname, mysqlColumnBean);
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
         
        }
        return list;
    }
    
    public static void main(String[] args)
    {
        String url = "jdbc:mysql://192.168.205.62:3306/matdb?user=matuser&password=123456";
        Connection connection = JdbcUtil.connect(url);
        queryAllTables(connection);
    }
}
