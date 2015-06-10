package generatorSQLScript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
public class Main
{
    public static String exec(String host,String user,String psw,int port,String command){
        String result="";
        Session session =null;
        ChannelExec openChannel =null;
        try {
          JSch jsch=new JSch();
          session = jsch.getSession(user, host, port);
          java.util.Properties config = new java.util.Properties();
          config.put("StrictHostKeyChecking", "no");
          session.setConfig(config);
          session.setPassword(psw);
          session.connect();
          openChannel = (ChannelExec) session.openChannel("exec");
          openChannel.setCommand(command);
          int exitStatus = openChannel.getExitStatus();
          System.out.println(exitStatus);
          openChannel.connect();  
                InputStream in = openChannel.getInputStream();  
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    result+= new String(buf.getBytes("gbk"),"UTF-8")+"    <br>\r\n";  
                }  
        } catch (JSchException | IOException e) {
          result+=e.getMessage();
        }finally{
          if(openChannel!=null&&!openChannel.isClosed()){
            openChannel.disconnect();
          }
          if(session!=null&&session.isConnected()){
            session.disconnect();
          }
        }
        return result;
      }
    
    
    
    
      
      
      
      public static void main(String args[]){
       // String exec = exec("183.230.40.64", "platform2", "%Lsf3rjjs2", 600, "sleep 20;ls;");
        String exec = exec("183.230.40.64", "platform2", "%Lsf3rjjs2", 600, "ping 192.168.41.114 ");
        System.out.println(exec);   
      }
    
    
    
      
    
}
