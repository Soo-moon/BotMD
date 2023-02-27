import com.jcraft.jsch.*;

import java.io.*;

public class SSHServer {
    private static final String serverIP = "3.37.89.233";
    private static final int port = 22;
    private static final String user = "ubuntu";

    private ChannelSftp channelSftp;

    public SSHServer() {
        try {
            connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws JSchException {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(Word.rootDir + "/key/moon.pem");

            Session session = jsch.getSession(user, serverIP, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
        } catch (JSchException e) {
            throw new JSchException("Session connect error : " + e.getMessage());
        }
    }

    public void download (String name) throws SftpException, IOException {
        InputStream is = null;
        FileOutputStream fops = null;
        try {
            channelSftp.cd(Word.root);
            is = channelSftp.get(name);
            fops = new FileOutputStream(Word.rootDir + "/" + name , false);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                fops.write(bytes, 0, read);
            }
        } catch (SftpException e) {
            String msg = "sftp connect error : " + e.getMessage();
            throw new SftpException(0, msg);
        } catch (IOException e) {
            throw new IOException("server File IO error : " + e.getMessage());
        }
    }
}
