import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
    private final ServerListener serverListener;
    private final String botKey;

    private JDA jda;

    public DiscordBot(ServerManager serverManager, String botKey, ServerListener botListener) {
        this.serverManager = serverManager;
        this.botKey = botKey;
        this.serverListener = botListener;
    }

    public void start(){
        Thread botThread = new Thread(() -> {
            try {
                jda = JDABuilder.createDefault(botKey)
                        .enableIntents(Arrays.asList(botPermission))
                        .build();
                jda.addEventListener(new BotEventListener());
            }catch (Exception e){
                Log.e("JDA dead" ,e);
            }
        });
        botThread.setName("botThread");
        botThread.start();
    }

    public void stop() {
        Log.d("Stop !!");
        jda = null;
    }

    public class BotEventListener extends ListenerAdapter {

        @Override
        public void onReady(ReadyEvent event) {
            super.onReady(event);
        }

        @Override
        public void onStatusChange(StatusChangeEvent event) {
//            Log.d(event.toString());
            if (event.getNewStatus() == JDA.Status.DISCONNECTED){
                Log.e("JDA.Status.DISCONNECTED");
                serverListener.onDestroy();
            }
            super.onStatusChange(event);
        }

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            User user = event.getAuthor();
            Message message = event.getMessage();
            MessageChannel channel = event.getChannel();

            Log.d("message : " + message);
            if (user.isBot() || !message.getContentRaw().startsWith(prefix)) return;

            ArrayList<String> args = new ArrayList<>(Arrays.asList(message.getContentRaw().split(" ")));
            String command = args.get(0).replace(prefix, "");
            String msg = args.get(1);

            try {
                ArrayList<CustomEmbedBuilder> ebs = serverManager.serverRequest(command, msg);
                for (CustomEmbedBuilder eb : ebs){
                    channel.sendMessageEmbeds(eb.build()).queue();
                }
            } catch (NoSuchElementException e) {
                channel.sendMessage("명령어 - !전각 (각인서이름) , !경매 (금액)").queue();
                Log.e(e.getMessage());
            } catch (NumberFormatException e) {
                channel.sendMessage("금액은 숫자로 입력해주세요").queue();
                Log.e(e.getMessage());
            } catch (Exception e) {
                channel.sendMessage("그런거 없어여").queue();
                Log.e(e.getMessage());
            }
        }
    }
}
