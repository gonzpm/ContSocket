package server.socket.contSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

//import server.socket.contSocket.ContSocketService;
// DONE
public class TCPContSocketServer {
	
	 private static final Logger LOGGER = Logger.getLogger(TCPContSocketServer.class.getName());
	 private static int numClients = 0;

	 public static void main(String args[]) {
	     if (args.length < 1) {
	         LOGGER.log(Level.SEVERE, " # Usage: TCPSocketSever [PORT]");
	         System.exit(1);
	     }

	     int serverPort = Integer.parseInt(args[0]);
	     startServer(serverPort);
	 }
	    
	 private static void startServer(int serverPort) {
	     try (ServerSocket tcpServerSocket = new ServerSocket(serverPort)) {
	         LOGGER.log(Level.INFO, " - TCPSocketFacebookServer: Waiting for connections '" + tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getLocalPort() + "' ...");
	         while (true) {
	             new ContSocketService(tcpServerSocket.accept());
	             LOGGER.log(Level.INFO, " - TCPSocketFacebookServer: New client connection accepted. Client Number: " + numClients++);
	         }
	     } catch (IOException e) {
	         LOGGER.log(Level.SEVERE, "# TCPSocketFacebookServer: IO error:" + e.getMessage());
	     }
     } 
}
