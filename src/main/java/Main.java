public class Main {
    private static final Boolean isTest = System.getProperty("os.name").contains("Windows");

    public static void main(String[] args) {
        try {
            Log.init();

            if (isTest) {
                SSHServer sshServer = new SSHServer();
                sshServer.download();
            }

            ServerManager serverManager = new ServerManager();
            serverManager.BotStart();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}