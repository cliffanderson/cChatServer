package com.cliff777.servershell;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Connection {

	private Socket socket;
	private PrintWriter out;
	private Receiver receiver;
	private String name;
	private int id;
	//private Sender sender;

	public Connection(Socket socket, int id) {
		this.socket = socket;
		this.id = id;

		this.receiver = new Receiver(socket, id);

		this.receiver.start();

		try {
			this.out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
		String connected = "";
		
		for(Connection connection : Server.getConnections().values())
		{
			if(connection != this)
				connected += connection.name + ", ";
		}
		
		connected = connected.substring(0, connected.length() - 2);
		
		send("Welcome to cChat! The current Date and time is " + date);
		send("Current connected clients: " + connected);
	}

	public void send(String data) {
		//println() required
		try
		{
			out.println(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setName(String name)
	{
		this.name = name;
	}

	//kills this connection if an error occurs
	public void die()
	{
		try
		{
			socket.close();
			receiver.in.close();
			out.close();
		}
		catch(Exception e) {}
		
		Server.getConnections().remove(id);
	}

}
