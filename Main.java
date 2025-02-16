public class Main {
    public static void main(String[] args) {
        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            Server.main(new String[0]);
        });

        // Start the client in another thread
        Thread clientThread = new Thread(() -> {
            Client.main(new String[0]);
        });

        serverThread.start();
        clientThread.start();
    }
}
