import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class ServerManager extends ListenerAdapter {
    private static final String config = System.getProperty("user.home") + "/server/system/config.properties";

    private final Properties prop = new Properties();

    private API api;
    private DB db;
    private DiscordBot discordBot;

    ServerManager() throws IOException {
        prop.load(new FileInputStream(config));

        String apiKey = prop.getProperty("api.key");
        String botKey = prop.getProperty("bot.key");

        serverCreate(apiKey, botKey);
    }

    public void serverCreate(String apiKey, String botKey) {
        discordBot = new DiscordBot(this , botKey);
        Thread botThread = new Thread(discordBot);
        botThread.start();

        api = new API(this , apiKey);
        db = new DB(this , api);
    }

    public EmbedBuilder serverRequest(BotCommand code ,String msg){
        Log.d("server code : " +code.name());
        EmbedBuilder eb = new EmbedBuilder();

        String title;
        String name;
        int gold;

        switch (code){
            case DIV_GOLD:{
               ebBuilder(Integer.parseInt(msg) , null);
               break;
            }
            case SKILLBOOK:{
                ebBuilder(db.getSkillBook(msg) , msg);
            }
        }
        return eb;
    }

    private void ebBuilder(ArrayList<String> skillBook, String msg) {
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



    public void divGold(MessageChannel channel, String msg) {
        try {
            int gold = Integer.parseInt(msg);
            divGold(channel, gold,"");
        }catch (NumberFormatException e){
            Log.e(e.getMessage());
        }
    }

    private void divGold(MessageChannel channel, int gold, String s) {
    }

    public EmbedBuilder ebBuilder(Integer gold , String name) throws RuntimeException{
        try {
            String title;
            EmbedBuilder eb = new EmbedBuilder();
            if (name.isEmpty()){
                title = "경매계산기";
            }else {
                title = name;
                MessageEmbed.Field field = new MessageEmbed.Field("최근거래가 : " +gold + " gold" , "",true);
                eb.addField(field);
            }
            eb.setTitle(title);
            eb.addField("4인N빵", Math.round(gold * 0.7215) + " gold", true);
            eb.addField("4인선점", Math.round(gold * 0.64772) + " gold", true);
            eb.addBlankField(false);
            eb.addField("8인N빵", Math.round(gold * 0.83125) + " gold", true);
            eb.addField("8인선점", Math.round(gold * 0.75568)+" gold", true);
        }catch (Exception e){
            Log.e(e.getMessage());
        }
        return new EmbedBuilder();
    }

}
