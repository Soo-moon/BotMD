

public class Main {
    private static String tag = "main";

    public static void main(String[] args) {
        try {
            Log log = new Log();
            ServerManager serverManager = new ServerManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}