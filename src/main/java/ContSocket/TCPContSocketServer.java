package ContSocket;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class TCPContSocketServer implements CommandLineRunner {

	@Value("${contsocket.port}")    
	private int port;

	@Value("${contsocket.host}")    
	private String host;

    @Override
    public void run(String... args) {
        new Thread(() -> startServer()).start();
    }

    private void startServer() {
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
