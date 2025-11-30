package ContSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ContSocketService extends Thread {

    private static final String DELIMITER = "#";
    private static final int MAX_CAPACITY = 10000;

    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    private Map<LocalDate, Double> capacity;

    public ContSocketService(Socket socket) {
        initCapacities();
        try {
            this.tcpSocket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.err.println("# ContSocketService - TCPConnection IO error:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String data = this.in.readUTF();
            System.out.println(" - ContSocketService - Received: '" + data + "'");

            String response = process(data);

            this.out.writeUTF(response);
            System.out.println(" - ContSocketService - Sent: '" + response + "'");

        } catch (EOFException e) {
            System.err.println("# ContSocketService - EOF error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("# ContSocketService - IO error: " + e.getMessage());
        } finally {
            try {
                tcpSocket.close();
            } catch (IOException e) {
                System.err.println("# ContSocketService - Closing error: " + e.getMessage());
            }
        }
    }

    private String process(String data) {
        try {
            String[] parts = data.split(DELIMITER);

            if (parts.length != 2 || !parts[0].equals("CHECK"))
                return "ERR";

            LocalDate date = LocalDate.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE);
            boolean exists = capacity.containsKey(date);

            return "OK" + DELIMITER + exists;

        } catch (Exception e) {
            System.err.println("# ContSocketService - Error processing request: " + e.getMessage());
            return "ERR";
        }
    }

    private void initCapacities() {
        capacity = new HashMap<>();
        Random random = new Random();

        capacity.put(LocalDate.of(2025, 1, 1), 8080.0);
        capacity.put(LocalDate.of(2025, 1, 2), 5200.0);

        for (int i = 0; i < MAX_CAPACITY; i++) {
            LocalDate date = LocalDate.of(2025, 1, 3).plusDays(i);
            double cap = 1000 + random.nextDouble() * 8000;
            capacity.put(date, cap);
        }
    }
}
