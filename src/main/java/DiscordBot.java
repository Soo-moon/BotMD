import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class DiscordBot {
    private static final String prefix = "!";
    private static final GatewayIntent[] botPermission = {
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.MESSAGE_CONTENT
    };

    private ServerManager serverManager;
    private boolean debug;

    public class BotListener extends ListenerAdapter{
        private API api;
        @Override
        public void onReady(ReadyEvent event) {
            super.onReady(event);
            api = serverManager.getApi();
        }

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            User user = event.getAuthor();
            Message message = event.getMessage();
            MessageChannel channel = event.getChannel();

            if (user.isBot() || !message.getContentRaw().startsWith(prefix))return;

            ArrayList<String> args = new ArrayList<>(Arrays.asList(message.getContentRaw().split(" ")));
            String command = args.get(0).replace(prefix , "");
            String msg = args.get(1);
            if (debug) System.out.println(this.getClass().getSimpleName() + "command : " + command + " msg : " + msg);

            try {
                switch (BotCommand.command(command)){
                    case MARKETS:{
                        api.searchItem(msg , channel);
                        break;
                    }
                    case GOLD:{
                        divGold(channel,msg);
                        break;
                    }
                    case AUCTIONS: break;
                }
            }catch (NoSuchElementException e ){
                channel.sendMessage("????????? - !?????? (????????????) , !?????? (??????)").queue();
                e.printStackTrace();
            }catch (Exception e){
                channel.sendMessage("????????? ?????????").queue();
                e.printStackTrace();
            }
        }
    }

    public void create(String token , ServerManager serverManager){
        this.serverManager = serverManager;
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(Arrays.asList(botPermission))
                .build();
        jda.addEventListener(new BotListener());

        debug = serverManager.isTest;
    }

    public void divGold(MessageChannel channel, String msg) {
        divGold(channel, msg,"");
    }

    public void divGold(MessageChannel channel , String gold , String name) throws RuntimeException{
        try {
            String title = name.isEmpty() ? "???????????????" : name;
            int goldInteger = Integer.parseInt(gold);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(title);
            eb.addField("4???N???", Math.round(goldInteger * 0.7215) + " gold", true);
            eb.addField("4?????????", Math.round(goldInteger * 0.64772) + " gold", true);
            eb.addBlankField(false);
            eb.addField("8???N???", Math.round(goldInteger * 0.83125) + " gold", true);
            eb.addField("8?????????", Math.round(goldInteger * 0.75568)+" gold", true);
            channel.sendMessageEmbeds(eb.build()).queue();
        }catch (Exception e){
            throw new RuntimeException();
        }
    }


}
