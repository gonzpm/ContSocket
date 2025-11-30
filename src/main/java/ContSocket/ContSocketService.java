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

    private Map<LocalDate, Double> capacity;
    private static final double TOTAL_CAPACITY = 10000.0;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ContSocketService(Socket socket) {
        initData();
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
            System.out.println(" - Received: " + request);

            @SuppressWarnings("unchecked")
            Map<String, String> jsonReq = objectMapper.readValue(request, Map.class);

            String action = jsonReq.get("action");

            if (!"GET_CAPACITY".equals(action)) {
                out.writeUTF("{\"error\":\"Invalid action\"}");
                return;
            }

            LocalDate date = LocalDate.parse(jsonReq.get("date"));

            double current = capacity.getOrDefault(date, 0.0);

            Map<String, Object> response = new HashMap<>();
            response.put("plant_name", "ContSocket");
            response.put("current_capacity", current);
            response.put("total_capacity", TOTAL_CAPACITY);

            String jsonResponse = objectMapper.writeValueAsString(response);

            out.writeUTF(jsonResponse);
            System.out.println(" - Sent: " + jsonResponse);

        } catch (Exception e) {
            System.err.println("# ContSocketService Error: " + e.getMessage());
        } finally {
            try { tcpSocket.close(); } catch (Exception ignored) {}
        }
    }

    private void initData() {
        capacity = new HashMap<>();
        Random r = new Random();
        for (int i = 0; i < 365; i++) {
            LocalDate d = LocalDate.of(2025, 1, 1).plusDays(i);
            capacity.put(d, 2000 + r.nextDouble() * 8000);
        }
    }
}
