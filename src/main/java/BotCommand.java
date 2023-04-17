import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public enum BotCommand {
    DIV_GOLD(Arrays.asList("ra", "ㄱㅁ", "경매"),0),
    SKILLBOOK(Arrays.asList("wr", "ㅈㄱ", "전각"),1),
    TRIPODS(Arrays.asList("xv", "ㅌㅍ", "트포"),2),
    DISCONNECT(Arrays.asList("DISCONNECT"),3);

    public final List<String> list;
    public final int code;

    BotCommand(List<String> list , int code) {
        this.list = list;
        this.code = code;
    }

    public static BotCommand command(String command) throws NoSuchElementException {
        return Arrays.stream(BotCommand.values())
                .filter(title -> title.check(command))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    private boolean check(String command) {
        return list.stream().anyMatch(list -> list.equals(command));
    }
}
