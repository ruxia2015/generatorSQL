package generatorSQLScript.mysql.bean;

public class MysqlColumnBean
{
    private String columnName;
    
    private String typeName;
    
    private Integer itype;
    
    private Integer size;
    
    private Integer precision;
    
    private Integer scale;
    
    private Boolean nullable;
    
    private Boolean autoIncrement;
    
    private String comment;
    private String defaultValue;
    private String key;
    private String type;
    
    public String toStr()
    {
        
        StringBuffer rs = new StringBuffer();
        rs.append("columnName=" + columnName + "\n");
        rs.append("typeName=" + typeName + "\n");
        rs.append("itype=" + itype + "\n");
        rs.append("size=" + size + "\n");
        
        rs.append("precision=" + precision + "\n");
        rs.append("scale=" + scale + "\n");
        rs.append("nullable=" + nullable + "\n");
        rs.append("autoIncrement=" + autoIncrement + "\n");
        
        return rs.toString();
    }
    
    public Boolean getAutoIncrement()
    {
        return autoIncrement;
    }
    
    public void setAutoIncrement(Boolean autoIncrement)
    {
        this.autoIncrement = autoIncrement;
    }
    
    public Integer getItype()
    {
        return itype;
    }
    
    public void setItype(Integer itype)
    {
        this.itype = itype;
    }
    
    public Integer getSize()
    {
        return size;
    }
    
    public void setSize(Integer size)
    {
        this.size = size;
    }
    
    public Integer getPrecision()
    {
        return precision;
    }
    
    public void setPrecision(Integer precision)
    {
        this.precision = precision;
    }
    
    public Integer getScale()
    {
        return scale;
    }
    
    public void setScale(Integer scale)
    {
        this.scale = scale;
    }
    
    public Boolean getNullable()
    {
        return nullable;
    }
    
    public void setNullable(Boolean nullable)
    {
        this.nullable = nullable;
    }
    
    public String getColumnName()
    {
        return columnName;
    }
    
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
    
}
