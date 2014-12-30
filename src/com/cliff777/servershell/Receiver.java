package com.cliff777.servershell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver extends Thread {
	/*
	 * Reads in data from the client and sends to all clients
	 */

	public BufferedReader in;
	private Socket socket;
	private int id;
	private String name;

	public Receiver(Socket socket, int id) {
		super("Receiver");

		this.socket = socket;
		this.id = id;

		try {
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		try
		{
			name = in.readLine();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Server.getConnections().get(id).setName(name);
		
		for(Connection connection : Server.getConnections().values())
		{
			if(!Server.getConnections().get(id).equals(connection))
			{
				connection.send(name + " has joined");
			}
		}

		while(Server.running) {
			String line = "";

			try {
				line = in.readLine();
			} catch (IOException e) {
				return;
				//e.printStackTrace();
			}

			if(line == null)
			{				
				//tell all other connection that they are leaving
				for(Connection connection : Server.getConnections().values())
				{
					if(!Server.getConnections().get(id).equals(connection))
					{
						connection.send(name + " has disconnected");
					}
				}
				
				Server.getConnections().get(id).die();
				//error in the connection, close it
				
				return;

			}

			System.out.println(name + ": " + line + " @" + socket.getInetAddress().getHostName());

			//Send the data to all clients
			for(Connection connection : Server.getConnections().values()) {
				connection.send(name + ": " + line);

			}
		}
	}
}
