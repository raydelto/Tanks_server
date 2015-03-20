/**
 * @author Raydelto
 */
package com.ast.tanksserver.network;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
	private ArrayList<ConnectionThread> connections;
	private ServerSocket server;
	private boolean enabled;
	private static Server instance;
	
	public static synchronized Server getInstance(){
		return instance == null ? instance = new Server() : instance;
	}
	
	private Server(){
		connections = new ArrayList<ConnectionThread>();
		enabled = true;
	}
	
	
	
	public ArrayList<ConnectionThread> getConnections() {
		return connections;
	}

	public void listen(int port){
		try {
			ConnectionThread connectionThread = null;
			System.out.println("Receiving connections on port " + port);
			server = new ServerSocket(port);
			Socket client = null;
			while(enabled){
				client = server.accept();
				connectionThread = new ConnectionThread(client);
				connectionThread.start();
			}
			
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}