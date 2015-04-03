/**
 * @author Raydelto
 */
package com.ast.tanksserver;

import java.net.UnknownHostException;

import com.ast.tanksserver.network.ConnectionThread;

public class Launcher {
	public static void main(String[] args) throws Exception{
		 //Server.getInstance().listen(3000);
		
		ConnectionThread connection;
		try {
			connection = new ConnectionThread( 8083 );
			connection.start();
			System.out.println( "ChatServer started on port: " + connection.getPort() );

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
