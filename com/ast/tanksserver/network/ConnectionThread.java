package com.ast.tanksserver.network;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.ast.tanksserver.users.UserHandler;

public class ConnectionThread extends WebSocketServer{
	
	private Socket client;
	private PrintWriter writer;
	private BufferedReader reader;
	private boolean connected;
	private UserHandler userHandler;
	private int port;
	private HashMap<WebSocket,String> roles;
	
	public Socket getClient() {
		return client;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public BufferedReader getReader() {
		return reader;
	}
	

	public ConnectionThread( int port ) throws UnknownHostException {		
		super( new InetSocketAddress( port ) );
		this.port = port;
		roles = new HashMap<WebSocket,String>();
	}
	
	/*
	 public ConnectionThread(Socket client) {
		this.client  = client;
		try {
			writer = new PrintWriter(client.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			connected = true;
			userHandler = UserHandler.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}*/
	
	public void sendMessage(String message){
		writer.println(message);
	}	
	 
	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		/*conn.send("Hola Visitante2");
		this.sendToAll( "new connection: " + handshake.getResourceDescriptor() );*/
		if(!roles.containsValue("ATTACK")){
			roles.put(conn,"ATTACK");
			conn.send("A");
			System.out.println("ATTACKER Joined");
		}else if(!roles.containsValue("DEFENSE")){
			roles.put(conn, "DEFENSE");
			conn.send("D");
			System.out.println("Defense Joined");
		}else{
			conn.send("E");//ESPECTATOR
			System.out.println("Espectator Joined");
		}
			
			
		System.out.println( conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		this.sendToAll( conn + " has left the room!" );		
		System.out.println( conn + "(" + roles.get(conn)+") has left the room!" );
		roles.remove(conn);
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		this.sendToAll( message,conn );
		System.out.println( conn + ": " + message );
	}

	@Override
	public void onFragment( WebSocket conn, Framedata fragment ) {
		System.out.println( "received fragment: " + fragment );
	}
	
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}
	
	public void sendToAll( String text) {
		sendToAll(text,null);
	}
	
	public void sendToAll( String text ,  WebSocket conn) {
		Collection<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket c : con ) {
				if(c != conn){
					c.send( text );
				}
			}
		}
	}

}
