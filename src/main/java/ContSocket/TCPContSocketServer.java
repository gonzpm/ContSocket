package ContSocket;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPContSocketServer {

    private static int numClients = 0;

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("# Usage: TCPContSocketServer [PORT]");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

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