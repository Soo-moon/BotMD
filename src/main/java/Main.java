

public class Main {
    private static String tag = "main";

    public static void main(String[] args) {
        try {
            Log log = new Log();
            ServerManager serverManager = new ServerManager();
            log.d("main end ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}