package generatorSQLScript.mysql;

import generatorSQLScript.mysql.bean.MysqlColumnBean;
import generatorSQLScript.util.FileUtil;
import generatorSQLScript.util.JdbcUtil;
import generatorSQLScript.util.MysqlUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MysqlMgr
{
    
    /**
     *
     * <������ϸ����>
     * @param newUrl
     * @param oldUrl ʹoldUrl��newUrlһ��
     * 
     * @return void [��������˵��]
     * @exception throws [Υ������] [Υ��˵��]
     * @see [�ࡢ��#��������#��Ա]
     */
    public void compareDb(String newUrl, String oldUrl, String outputPath)
    {
        Connection conNew = JdbcUtil.connect(newUrl);
        Connection conOld = JdbcUtil.connect(oldUrl);
        
        List<String> newTables = JdbcUtil.queryAllTables(conNew);
        List<String> oldTables = JdbcUtil.queryAllTables(conOld);
        
        List<String> waitAddTables = new ArrayList<String>();
        //  List<String> waitDelTables = new ArrayList<String>();
        List<String> waitCompareTables = new ArrayList<String>();
        for (String newTable : newTables)
        {
            if (oldTables.contains(newTable))
            {
                waitCompareTables.add(newTable);
                oldTables.remove(newTable);
            }
            else
            {
                waitAddTables.add(newTable);
            }
            
        }
        System.out.println("waitAddTables  "+waitAddTables.size());
        System.out.println("waitCompareTables  "+waitCompareTables.size());
        System.out.println("waitRemove  "+oldTables.size());
        
        //�������sql
        for (String temp : waitAddTables)
        {
            String fileName = outputPath + "//" + temp.toUpperCase() + ".tab";
            String sql = generatorCreateTableSql(temp, conNew);
            FileUtil.writeFile(fileName, sql);
        }
        
        //�޸ı��sql
        int index = 1;
        for (String temp : waitCompareTables)
        {
            System.out.println(index++ + "  " +temp);
            
            if(compareTableIsSame(temp,conNew,conOld)){
                continue;
            }
            
            String fileName = outputPath + "//" + temp.toUpperCase() + ".sql";
            String sql = generatorEditTableSql(temp, temp, conNew, conOld);
            FileUtil.writeFile(fileName, sql);                 
          /*  if(sql!=null && !sql.trim().equals("")){
                System.out.println("  " + sql);
            }*/
        }
        
        StringBuffer sb = new StringBuffer();
        for(String temp:oldTables){
                sb.append(temp+"\n");
        }
        FileUtil.writeFile(outputPath+"//��Ҫɾ���ı�.sql", sb.toString());
        
        System.out.println("���");
        try
        {
            
            conNew.close();
            conOld.close();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * ������ӱ��sql ֻ����ӵ���
     * <������ϸ����>
     * @param tableName
     * @param conNew
     * @param conOld
     * @return [����˵��]
     * 
     * @return String [��������˵��]
     * @exception throws [Υ������] [Υ��˵��]
     * @see [�ࡢ��#��������#��Ա]
     */
    public String generatorEditTableSql(String tableName, String tableName2,
            Connection conNew, Connection oldCon)
    {
        
        Map<String, MysqlColumnBean> oldMap = JdbcUtil.queryTableColumns(tableName2,
                oldCon);
        Map<String, MysqlColumnBean> newMap = JdbcUtil.queryTableColumns(tableName,
                conNew);
        
        StringBuffer sql = new StringBuffer();
        for (String colName : newMap.keySet())
        {
            //�����ֶ�
            if (!oldMap.containsKey(colName))
            {
                sql.append("alter table " + tableName2 + " add "
                        + getColStr(newMap.get(colName)) + ";\r\n");
            }
            else
            {
                if (!oldMap.get(colName).toStr().equals(newMap.get(colName).toStr()))
                {
                    sql.append("alter table " + tableName2 + " change `"
                            + colName + "`  " + getColStr(newMap.get(colName))
                            + ";\r\n");
                }
                oldMap.remove(colName);
            }
        }
        
        
        if(oldMap.size()>0){
            sql.append("\n\n\n#�����Ƕ�����ֶ�");            
        }
        
        for(String key:oldMap.keySet()){            
            sql.append("\n\n\n#"+oldMap.get(key).getColumnName());
            
        }
        
       // System.out.println(sql.toString());
        return sql.toString();
    }
    
    /**
     * ������ӱ��sql ֻ����ӵ���
     * <������ϸ����>
     * @param tableName
     * @param conNew
     * @param conOld
     * @return [����˵��]
     * 
     * @return String [��������˵��]
     * @exception throws [Υ������] [Υ��˵��]
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean compareTableIsSame(String tableName, 
            Connection conNew, Connection oldCon)
    {
        
        String newtableSql = MysqlUtil.showCreateTable(conNew, tableName);
        String oldTableSql = MysqlUtil.showCreateTable(oldCon, tableName);
        
       if(newtableSql.equalsIgnoreCase(oldTableSql)){
           return true;
       }
        
        return false;
    }
    
    public String getColStr(MysqlColumnBean tempBean)
    {
        
        StringBuffer sb = new StringBuffer();
        sb = new StringBuffer();
        sb.append("`" + tempBean.getColumnName() + "`");
        sb.append(" " + tempBean.getTypeName());
        
        if("datetime".equalsIgnoreCase(tempBean.getTypeName())  
        		|| "date".equalsIgnoreCase(tempBean.getTypeName()) ){
        	
        } else if (tempBean.getScale() != 0 ){
            sb.append("(" + tempBean.getSize() + "," + tempBean.getScale()
                    + ")");
            
        } else {
            sb.append("(" + tempBean.getSize() + ")");
            
        }
        
        if (!tempBean.getNullable())
        {
            sb.append(" NOT NULL");
        }
        if (tempBean.getAutoIncrement())
        {
            sb.append(" AUTO_INCREMENT");
        }
        
        return sb.toString();
    }
    
    /**
     * ������ӱ��sql
     * <������ϸ����>
     * @param tableName
     * @param conNew
     * @param conOld
     * @return [����˵��]
     * 
     * @return String [��������˵��]
     * @exception throws [Υ������] [Υ��˵��]
     * @see [�ࡢ��#��������#��Ա]
     */
    public String generatorCreateTableSql(String tableName, Connection conNew)
    {
        String sql = MysqlUtil.showCreateTable(conNew, tableName);
        
        System.out.println(sql);
        return sql;
    }
    
    public static void main(String[] args)
    {
        String url = "jdbc:mysql://120.24.75.25:3306/yufex_wtms_db?user=pqy&password=pqy";
        String url2 = "jdbc:mysql://127.0.0.1:3306/yufex_wtms_db?user=test&password=1234";
//        String url = "jdbc:mysql://183.230.40.64:3306/matdb_test?user=mattest&password=HUw21z$jzs";
        Connection connection = JdbcUtil.connect(url);
        
        new MysqlMgr().compareDb(url, url2, "D:\\outputSql2");
    }
}
