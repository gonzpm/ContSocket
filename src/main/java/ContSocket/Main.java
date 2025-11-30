package ContSocket;

public class Main {
    public static void main(String[] args) {

        int port = 8082; 
        System.out.println("Starting ContSocket server on port " + port + "...");
        TCPContSocketServer server = new TCPContSocketServer(port);
        server.start();
    }
}