import DTO.Market.MarketItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ServerManager {
    private final ServerListener serverListener = new ServerListener() {
        @Override
        public void onDestroy() {
            Log.e("FATAL onDestory !!!!");
            bot.stop();
            bot.start();
        }
    };

    private DB db;
    private DiscordBot bot;

    ServerManager() {
        try {
            db = new DB();

            String apiKey = db.getConfig("Api.key");
            API api = new API(apiKey);
            db.setApi(api);

            String botKey = db.getConfig("Bot.Key");
            bot = new DiscordBot(this, botKey, serverListener);

            Log.d("Init Success");
        } catch (Exception e) {
            Log.e(e.getMessage() + "Create Fail ..");
            e.printStackTrace();
        }
    }

    public void BotStart() {
        bot.start();
    }

    public ArrayList<CustomEmbedBuilder> serverRequest(String code, String msg) throws NumberFormatException, NoSuchElementException, InterruptedException {
        ArrayList<CustomEmbedBuilder> ebs = new ArrayList<>();

        switch (BotCommand.command(code)) {
            case DIV_GOLD: {
                CustomEmbedBuilder eb = new CustomEmbedBuilder();
                eb.gold = Integer.parseInt(msg);
                ebs.add(eb);
                break;
            }

            case SKILLBOOK: {
                for (MarketItem marketItem : db.getSkillBook(msg)) {
                    CustomEmbedBuilder eb = new CustomEmbedBuilder();
                    eb.title = marketItem.name;
                    eb.gold = marketItem.recentPrice;
                    ebs.add(eb);
                }

                break;
            }

            case DISCONNECT:{
                Log.d("DISCONNECT .. ");
                bot.stop();
                throw new InterruptedException();
            }
            default:
                throw new NoSuchElementException();
        }

        return ebs;
    }

}
