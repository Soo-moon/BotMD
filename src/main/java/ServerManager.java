import com.jcraft.jsch.*;

import java.io.*;
import java.util.Properties;

public class ServerManager {
    public Boolean isTest = System.getProperty("os.name").contains("Windows");

    private static final String config = "config.properties";
    private final Properties prop = new Properties();

    private Log log = new Log();
    private API api;
    private final DB db = new DB(this);;
    private final DiscordBot bot = new DiscordBot();

    ServerManager() throws SftpException, IOException {
        if (isTest) {
            SSHServer sshServer = new SSHServer();
            sshServer.download(config);
        }
        prop.load(new FileInputStream(Server.propFile));

        ServiceStart();
        log.d("start !! ");
    }

    public void ServiceStart() {
        try {
            api = new API(this , prop("api.url"), prop("api.key"));
            bot.create(prop("bot.key"),this);
            db.create();
        } catch (Exception e) {
            throw new RuntimeException("server create error ", e);
        }
    }

    public String prop(String key){
        return prop.getProperty(key);
    }

    public API getApi(){
        return api;
    }

    public DiscordBot getBot(){return bot;}

    public DB getDB(){return db;}


}
