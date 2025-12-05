package ContSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ContSocketService extends Thread {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    private static Float current_capacity = 10000.0f;//Se inicializa una vez

    public ContSocketService(Socket socket) {
        try {
            tcpSocket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            start();
        } catch (IOException e) {
            System.err.println("# ContSocketService: IO error - " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String request = in.readUTF();
            System.out.println(" - ContSocketService received: " + request);

            String response = processRequest(request);
           
            out.writeUTF(response);
            
            System.out.println(" - ContSocketService sent: " + current_capacity);

        } catch (Exception e) {
            System.err.println("# ContSocketService Error: " + e.getMessage());
        } finally {
            try { tcpSocket.close(); } catch (Exception ignored) {}
        }
    }

    private String processRequest(String request) {
    	if(request.equals("GET_CAPACITY")) {
    		return String.valueOf(current_capacity);
    	} else if (request.startsWith("SEND_NOTIFICATION:")) {
            try {
                //ContSocket gets the notification in the format SEND_NOTIFICATION:<dumpsters>:<packages>:<tons> from Ecoembes and updates their current capacity
           	 	String[] parts = request.split(":");
           	 	if (parts.length != 4) {
					return "ERROR:Invalid format";
				}
           	 	int dumpsters = Integer.parseInt(parts[1]);
           	 	int packages = Integer.parseInt(parts[2]);
           	 	float tons = Float.parseFloat(parts[3]);
           	 	System.out.println(" - ContSocketService processing notification: dumpsters=" + dumpsters + ", packages=" + packages + ", tons=" + tons);
           	 	current_capacity -= tons;
           	 	System.out.println(" - ContSocketService updated capacity: " + current_capacity);
           	 	
           	 	return "OK";
                
            } catch (Exception e) {
                return "ERROR:Invalid format";
            }
            
        } else {
            return "ERROR:Unknown command";
        }
    }
    
    public static float getCurrentCapacity() {
        return current_capacity;
    }
    
    public static void resetCapacity() {
        current_capacity = 10000.0f;
    }
}
