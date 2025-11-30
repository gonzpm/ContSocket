package server.socket.contSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContSocketService extends Thread{
	
	// TODO Cambiar por numero de containers o lo que hayais decidido
	private static final int NUMBEROFUSERS = 100;
	
	
    private static final Logger LOGGER = Logger.getLogger(ContSocketService.class.getName());

    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    // TODO aqui habrá numero de dumpsters o la info que vayamos a manejar
    private Map<String, String> facebookusers;

    public ContSocketService(Socket socket) {
    	// AVISO :Iniciar los valores de nuestro método, no los que están ahora comentados abajo
    	initUsers();
        try {
            tcpSocket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            start();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "# EchoService - TCPConnection IO error:" + e.getMessage());
        }
    }
    
    @Override
    public void run() {
        try {
            String data = in.readUTF();
            LOGGER.log(Level.INFO, "   - EchoService - Received data from '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");
            
            // AVISO : error porq tenemos que meter nuestro método, no sus dos que están comentados ahora
            boolean response;
            String[] args = data.split(";");
            if (args.length == 1) {
                response = checkUser(args[0]);
            } else {
                response = checkPassword(args[0], args[1]);
            }

            out.writeUTF(Boolean.toString(response));
            LOGGER.log(Level.INFO, "   - EchoService - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
        } catch (EOFException e) {
            LOGGER.log(Level.SEVERE, "   # EchoService - TCPConnection EOF error" + e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "   # EchoService - TCPConnection IO error:" + e.getMessage());
        } finally {
            try {
                tcpSocket.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "   # EchoService - TCPConnection IO error:" + e.getMessage());
            }
        }
    }
//   AVISO :  DEL DE STRAVA ---> HAY QUE METER NUESTRO MÉTODO Y INICIALIZARLO CON ESTOS COMO EJEMPLO  
//    
//    private boolean checkUser(String userEmail){
//        return facebookusers.containsKey(userEmail);
//    }
//
//    private boolean checkPassword(String userEmail, String userPassword){
//        if (facebookusers.containsKey(userEmail)) {
//            return userPassword.equals(facebookusers.get(userEmail));
//        }
//        return false;
//    }
//    
//	private void initUsers() {
//		facebookusers = new HashMap<String, String>();
//		facebookusers.put("diego.merino@opendeusto.es", org.apache.commons.codec.digest.DigestUtils.sha1Hex("123"));
//		facebookusers.put("miguel.acha@opendeusto.es", org.apache.commons.codec.digest.DigestUtils.sha1Hex("123"));
//		
//        for (int i = 1; i < NUMBEROFUSERS + 1; i++) {
//            String email = "user" + i + "@gmail.com";
//            String password =  org.apache.commons.codec.digest.DigestUtils.sha1Hex("123");
//            facebookusers.put(email, password);
//        }
//	}
	
}
