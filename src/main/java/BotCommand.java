import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public enum BotCommand{
    AUCTIONS(Arrays.asList("xv","ㅌㅍ","트포")),
    MARKETS(Arrays.asList("wr","ㅈㄱ","전각")),
    GOLD(Arrays.asList("ra","ㄱㅁ","경매"));


    private final List<String> list;


    BotCommand(List<String> list) {
        this.list = list;
    }

    public static BotCommand command(String code) throws NoSuchElementException {
        return Arrays.stream(BotCommand.values())
                .filter(title -> title.check(code))
                .findAny()
                .orElseThrow();
    }

    private boolean check(String code){
        return list.stream().anyMatch(list -> list.equals(code));
    }
}
