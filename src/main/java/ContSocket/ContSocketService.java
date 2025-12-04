package ContSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ContSocketService extends Thread {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    private static Float current_capacity = 8000.0f;//Se inicializa una vez
    private static final double TOTAL_CAPACITY = 10000.0;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                String[] parts = request.split(":");
                int dumpsters = Integer.parseInt(parts[1]);
                int packages = Integer.parseInt(parts[2]);
                float tons = Float.parseFloat(parts[3]);
                
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
        current_capacity = 8000.0f;
    }
}
