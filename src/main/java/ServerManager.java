import com.jcraft.jsch.*;

import java.io.*;
import java.util.Properties;

public class ServerManager {
    public Boolean isTest = System.getProperty("os.name").contains("Windows");

    private DB db;
    private final API api = new API();
    private final DiscordBot bot = new DiscordBot();
    private final Properties serverProp = new Properties();

    ServerManager() throws SftpException, IOException {
        if (isTest) {
            SSHServer sshServer = new SSHServer();
            sshServer.download(Word.propFileName);
        }

        serverProp.load(new FileInputStream(Word.propFile));

        ServiceStart();
    }

    public void ServiceStart() {
        try {
            api.create(prop("api.url"), prop("api.key") ,this);
            bot.create(prop("bot.key"),this);
            db = new DB(this,api,0,"");
            db.makeFile();

        } catch (Exception e) {
            throw new RuntimeException("server create error ", e);
        }
    }

    public String prop(String key){
        return serverProp.getProperty(key);
    }

    public API getApi(){
        return api;
    }

    public DiscordBot getBot(){return bot;}

    public DB getDB(){return db;}
}
