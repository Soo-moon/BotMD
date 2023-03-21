import com.jcraft.jsch.*;

import java.io.*;

public class SSHServer {
    private static final String serverIP = "3.37.89.233";
    private static final int port = 22;
    private static final String user = "ubuntu";
    private static final String rootDIR = System.getProperty("user.home") + "/server/system/";

    private final Log log = new Log();
    private ChannelSftp channelSftp;
    private Session session;

    public SSHServer() {
        connect();
    }

    private void connect() {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(rootDIR + "key/moon.pem");

            session = jsch.getSession(user, serverIP, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
        } catch (JSchException e) {
            log.e("Session connect error : ", e);
        }
    }

    public void download(String name){
        InputStream is = null;
        FileOutputStream fops = null;
        try {
            channelSftp.cd("server/system");
            is = channelSftp.get(name);
            fops = new FileOutputStream(rootDIR + name, false);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                fops.write(bytes, 0, read);
            }
        } catch (SftpException e) {
            String msg = "sftp connect error : " + e.getMessage();
            log.e(msg , e);
        } catch (IOException e) {
            log.e("server File IO error : " , e);
        }
        finally {
            channelSftp.disconnect();
            session.disconnect();
        }
    }
}
