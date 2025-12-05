package ContSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ContSocket {

    private String serverIP;
    private int serverPort;

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
    
    //Receives notification from Ecoembes in the format SEND_NOTIFICATION:<dumpsters>:<packages>:<tons> and updates current capacity
    public void receiveNotification(int dumpsters, int packages, float tons) {
		String request = "SEND_NOTIFICATION:" + dumpsters + ":" + packages + ":" + tons;
		String response = sendRequest(request);
		
		if (response.equals("OK")) {
			System.out.println("(ContSocket info receiving) Everything went OK");
		} else {
			System.out.println("(ContSocket info receiving) Not everything went OK :(");
		}
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
