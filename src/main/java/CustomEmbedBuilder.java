import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class CustomEmbedBuilder extends EmbedBuilder {
    public String title;
    public int gold;

    @NotNull
    @Override
    public MessageEmbed build()
    {
        if (title == null){
            title = "경매계산기";
        }
        else {
            addField("최근거래가 : " +gold + " gold" , "",true);
        }

        setTitle(this.title);

        addField("4인N빵", Math.round(gold * 0.7215) + " gold", true);
        addField("4인선점", Math.round(gold * 0.64772) + " gold", true);
        addBlankField(false);
        addField("8인N빵", Math.round(gold * 0.83125) + " gold", true);
        addField("8인선점", Math.round(gold * 0.75568)+" gold", true);

        return super.build();
    }
}
