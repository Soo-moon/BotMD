import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public enum BotCommand {
    TRIPODS(Arrays.asList("xv", "ㅌㅍ", "트포"),0),
    SKILLBOOK(Arrays.asList("wr", "ㅈㄱ", "전각"),10),
    DIV_GOLD(Arrays.asList("ra", "ㄱㅁ", "경매"),1);

    private final List<String> list;
    private final int code;

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

    public static int code(BotCommand botCommand){
        return botCommand.code;
    }

    private boolean check(String command) {
        return list.stream().anyMatch(list -> list.equals(command));
    }
}
