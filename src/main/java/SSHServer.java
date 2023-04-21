import com.jcraft.jsch.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

public class SSHServer {
    private static final String serverIP = "34.138.201.68";
    private static final int port = 22;
    private static final String user = "md";
    private static final String Identity = System.getProperty("user.home") + "/key/gcp_mdKey.pem";

    private ChannelSftp channelSftp;
    private Session session;

    public SSHServer() {
        connect();
    }

    private void connect() {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(Identity);
            session = jsch.getSession(user, serverIP, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
        } catch (JSchException e) {
            Log.e("Session connect error : ", e);
            e.printStackTrace();
        }
    }

    public void download() {
        try {
            String dbpath = "server/system";
            downloadFile(dbpath, Paths.get(System.getProperty("user.home") + "/" + dbpath + "/"));
        } finally {
            channelSftp.disconnect();
            session.disconnect();
        }
    }


    private void downloadFile(String serverPath, Path localPath) {
        try {
            Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(serverPath);

            for (ChannelSftp.LsEntry en : entries) {
                if (en.getFilename().equals(".") || en.getFilename().equals("..")) {
                    continue;
                }
                if (!en.getFilename().equals(".") && !en.getFilename().equals("..") && en.getAttrs().isDir()) {
                    Path path = Paths.get(localPath + "/" + en.getFilename());
                    Files.createDirectories(path);
                    downloadFile(serverPath + "/" + en.getFilename(), path);
                    continue;
                }

                InputStream is = channelSftp.get(serverPath + "/" + en.getFilename());

                if (!Files.exists(localPath)) {
                    Files.createDirectories(localPath);
                }
                Path target = Paths.get(localPath + "/" + en.getFilename());
                Files.copy(is, target);
                Log.d(target.getFileName() + " : download !!");
            }
        } catch (SftpException e) {
            Log.e("Session connect error : ", e);
            e.printStackTrace();
        } catch (FileAlreadyExistsException e){
            //
        } catch (IOException e) {
            Log.e("file createDir error" + e);
            e.printStackTrace();
        }
    }
}
