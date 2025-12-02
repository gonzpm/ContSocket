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
    
    public float getCapacity() {
        String request = "GET_CAPACITY";
        String response = sendRequest(request);
        
        try {
            return Float.parseFloat(response);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing capacity: " + response);
            return -1;
        }
    }
    
    public boolean updateCapacity(float amount) {
        String request = "UPDATE_CAPACITY:" + amount;
        String response = sendRequest(request);
        
        return response.startsWith("OK");
    }
    
    private String sendRequest(String request) {
        try (Socket socket = new Socket(serverIP, serverPort);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            
            out.writeUTF(request);
            return in.readUTF();
            
        } catch (IOException e) {
            System.err.println("# ContSocket error: " + e.getMessage());
            return "ERROR:" + e.getMessage();
        }
    }

}
