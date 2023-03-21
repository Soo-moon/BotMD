import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

    private final ServerManager serverManager;
    private final Log log = new Log();

    public DiscordBot(ServerManager serverManager , String botKey) {
        this.serverManager = serverManager;

        JDA jda = JDABuilder.createDefault(botKey)
                .enableIntents(Arrays.asList(botPermission))
                .build();
        jda.addEventListener(new BotListener());
    }

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
                    case AUCTIONS: {
                        api.request_tripod(msg);
                        break;
                    }
                }
            }catch (NoSuchElementException e ){
                channel.sendMessage("명령어 - !전각 (전각이름) , !경매 (금액)").queue();
                log.e(e.getMessage());
            }catch (NumberFormatException e){
                channel.sendMessage("금액은 숫자로 입력해주세요").queue();
                log.e(e.getMessage());
            }
            catch (Exception e){
                channel.sendMessage("그런거 없어여").queue();
                log.e(e.getMessage());
            }
        }
    }

    public void divGold(MessageChannel channel, String msg) {
        try {
            int gold = Integer.parseInt(msg);
            divGold(channel, gold,"");
        }catch (NumberFormatException e){
            log.e(e.getMessage());
        }
    }

    public void divGold(MessageChannel channel , Integer gold , String name) throws RuntimeException{
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
            channel.sendMessageEmbeds(eb.build()).queue();
        }catch (Exception e){
            log.e(e.getMessage());
        }
    }
}
