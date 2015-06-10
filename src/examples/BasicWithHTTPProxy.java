package examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.HTTPProxyData;
import ch.ethz.ssh2.ServerHostKeyVerifier;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class BasicWithHTTPProxy
{
    public static void main(String[] args)
    {
        String hostname = "192.168.41.5";
        String username = "platform";
        String password = "%Lsf3rjjs2";
        
        String proxyHost = "183.230.40.64";
        int proxyPort = 600; // default port used by squid
        String proxyUsername = "platform2";
        String proxyPassword = "%Lsf3rjjs2";
        
        try
        {
            /* Create a connection instance */
            
            Connection conn = new Connection(hostname);
            
            /* We want to connect through a HTTP proxy */
            
            //			conn.setProxyData(new HTTPProxyData(proxyHost, proxyPort));
            
            // if the proxy requires basic authentication:
            // conn.setProxyData(new HTTPProxyData(proxyHost, proxyPort, proxyUsername, proxyPassword));
            
            HTTPProxyData httpProxyData = new HTTPProxyData(proxyHost,
                    proxyPort, proxyUsername, proxyPassword);
            conn.setProxyData(httpProxyData);
            
            /* Now connect (through the proxy) */
            
            conn.connect(new ServerHostKeyVerifier(){

                @Override
                public boolean verifyServerHostKey(String arg0, int arg1,
                        String arg2, byte[] arg3) throws Exception
                {
                    // TODO Auto-generated method stub
                    return true;
                }});
            
            /* Authenticate.
             * If you get an IOException saying something like
             * "Authentication method password not supported by the server at this stage."
             * then please check the FAQ.
             */
            
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            
            /* Create a session */
            
            Session sess = conn.openSession();
            
            sess.execCommand("uname -a && date && uptime && who");
            
            System.out.println("Here is some information about the remote host:");
            
            /* 
             * This basic example does not handle stderr, which is sometimes dangerous
             * (please read the FAQ).
             */
            
            InputStream stdout = new StreamGobbler(sess.getStdout());
            
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(stdout));
            
            while (true)
            {
                String line = br.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }
            
            /* Show exit status, if available (otherwise "null") */
            
            System.out.println("ExitCode: " + sess.getExitStatus());
            
            /* Close this session */
            
            sess.close();
            
            /* Close the connection */
            
            conn.close();
            
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            System.exit(2);
        }
    }
}
