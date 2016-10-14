package generatorSQLScript.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil
{
    
    public static void writeFile(String filePath, String content)
    {
        if (content == null || content.equals(""))
        {
            return;
        }
        
        File file = new File(filePath);
        try
        {
            if (!file.exists())
            {                
                File pf = file.getParentFile();
                if (!pf.exists())
                {
                    pf.mkdirs();
                }
                file.createNewFile();
                
            }
            
            System.out.println("   文件内容是 ==>" + content);
            
            FileWriter fileWritter = new FileWriter(file, false);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(content);
            bufferWritter.flush();
            bufferWritter.close();
            fileWritter.close();
            
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
