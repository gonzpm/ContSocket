package ContSocket;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPContSocketServer extends Thread {

    private final int port;

    public TCPContSocketServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        int numClients = 0;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("- TCPContSocketServer: Waiting for connections on port " + port);

            while (true) {
                new ContSocketService(server.accept());
                System.out.println("- TCPContSocketServer: New client accepted. Number: " + ++numClients);
            }

        } catch (IOException e) {
            System.err.println("# TCPContSocketServer: IO error: " + e.getMessage());
        }
    }
}