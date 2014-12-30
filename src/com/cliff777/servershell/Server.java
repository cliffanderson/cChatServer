package com.cliff777.servershell;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
	
	private final int PORT = 12345;
	
	private ServerSocket server = null;
	
	public static boolean running = true;
	private static Map<Integer, Connection> connections = new HashMap<Integer, Connection>();
	
	public static synchronized Map<Integer, Connection> getConnections()
	{
		return connections;
	}
	
	public static void main(String[] args) {
		new Server();
	}
	
	public Server() {
		try {
			this.server = new ServerSocket(this.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Now listening on port " + this.PORT);
		
		Socket socket = null;
		Connection connection = null;
		
		int currentId = 0;
		
		while(running) {
			try {
				socket = this.server.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			connection = new Connection(socket, currentId);
			connections.put(currentId++, connection);
			
			System.out.println("Accepted connection from: " + socket.getInetAddress().getCanonicalHostName());
		}
	}

}
