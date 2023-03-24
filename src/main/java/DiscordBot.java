import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class DiscordBot{
    private static final String prefix = "!";
    private static final GatewayIntent[] botPermission = {
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.MESSAGE_CONTENT
    };
    private final ServerManager serverManager;
    private final String botKey;

    private JDA jda;


    public DiscordBot(ServerManager serverManager, String botKey) {
        this.serverManager = serverManager;
        this.botKey = botKey;
    }

    public void start(){
        Thread botThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jda = JDABuilder.createDefault(botKey)
                            .enableIntents(Arrays.asList(botPermission))
                            .setAutoReconnect(true)
                            .build();

                    jda.addEventListener(serverManager);
                }catch (Exception e){
                    Log.e("JDA dead" ,e);
                }
            }
        });
    }

    public class BotListener extends ListenerAdapter {
        private API api;

        @Override
        public void onReady(ReadyEvent event) {
            super.onReady(event);
            api = serverManager.getApi();
        }

        @Override
        public void onStatusChange(StatusChangeEvent event) {
            Log.d(event.toString());
            if (event.getNewStatus() == JDA.Status.DISCONNECTED){
                Log.e("");
            }
            super.onStatusChange(event);
        }

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            User user = event.getAuthor();
            Message message = event.getMessage();
            MessageChannel channel = event.getChannel();

            if (user.isBot() || !message.getContentRaw().startsWith(prefix)) return;

            ArrayList<String> args = new ArrayList<>(Arrays.asList(message.getContentRaw().split(" ")));
            String command = args.get(0).replace(prefix, "");
            String msg = args.get(1);

            try {
                BotCommand botCommand = BotCommand.command(command);
                switch (botCommand) {
                    case SKILLBOOK:
                        api.searchItem(msg, channel);
                        break;
                    case DIV_GOLD:
                        divGold(channel, 1, msg);
                        break;
                    case TRIPODS:
                        api.request_tripod(msg);
                        break;
                }
                //ref
                EmbedBuilder eb = serverManager.serverRequest(botCommand, msg);
                channel.sendMessageEmbeds(eb.build()).queue();
            } catch (NoSuchElementException e) {
                channel.sendMessage("명령어 - !전각 (전각이름) , !경매 (금액)").queue();
                log.e(e.getMessage());
            } catch (NumberFormatException e) {
                channel.sendMessage("금액은 숫자로 입력해주세요").queue();
                log.e(e.getMessage());
            } catch (Exception e) {
                channel.sendMessage("그런거 없어여").queue();
                log.e(e.getMessage());
            }
        }
    }

    public void divGold(MessageChannel channel, Integer recentPrice, String msg) {
    }
}
