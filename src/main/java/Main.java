public class Main {
    private static final Boolean isTest = System.getProperty("os.name").contains("Windows");

    public static void main(String[] args) {
        try {
            Log.d("boot");
            if (isTest) {
                SSHServer sshServer = new SSHServer();
                sshServer.download();
            }

            Log.e("test error " , new RuntimeException("testtest "));

            ServerManager serverManager = new ServerManager();
            serverManager.BotStart();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}