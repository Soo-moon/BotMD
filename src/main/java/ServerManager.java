import java.io.*;
import java.util.Properties;

public class ServerManager {
    public Boolean isTest = System.getProperty("os.name").contains("Windows");

    private static final String rootDIR = System.getProperty("user.home") + "/server/system/";
    private static final String config = "config.properties";

    private final Log log = new Log();
    private final Properties prop = new Properties();

    private API api;
    private DB db;
    private DiscordBot discordBot;

    ServerManager() throws IOException {
        if (isTest) {
            SSHServer sshServer = new SSHServer();
            sshServer.download(config);
        }
        prop.load(new FileInputStream(rootDIR + config));

        String apiKey = prop.getProperty("api.key");
        String botKey = prop.getProperty("bot.key");

        serverCreate(apiKey, botKey);
        log.d("start !! ");
    }

    public void serverCreate(String apiKey, String botKey) {
        discordBot = new DiscordBot(this , botKey);
        api = new API(this , apiKey);
        db = new DB(this , api);
    }

    public String getProperty(String key){
        return prop.getProperty(key);
    }

    public API getApi() {
        return api;
    }

    public DiscordBot getBot() {
        return discordBot;
    }

    public DB getDB() {
        return db;
    }

}
