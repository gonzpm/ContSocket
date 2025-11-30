package ContSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ContSocket {

    private String serverIP;
    private int serverPort;
    private static final String DELIMITER = "#";

    public ContSocket(String ip, int port) {
        this.serverIP = ip;
        this.serverPort = port;
    }

    public boolean checkDate(String date) {

        String request = "CHECK" + DELIMITER + date;
        String response = null;

        try (Socket socket = new Socket(serverIP, serverPort);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            out.writeUTF(request);

            response = in.readUTF();

            String[] parts = response.split(DELIMITER);

            if (!parts[0].equals("OK"))
                return false;

            return Boolean.parseBoolean(parts[1]);

        } catch (IOException e) {
            System.err.println("# ContSocketClient error: " + e.getMessage());
        }

        return false;
    }
}
