package threads.web;

// File: SlowWebServer.java (Module 13)
//
// Author: Tim Wood
// Created: Nov 19, 2013
//
// A simple web server that sleeps as part of its reply.

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SlowWebServer {
	private JTextArea textArea;

	// Inner class for handling requests. It will still be able to access the outer class's GUI objects
	class RequestHandler {
		Socket socket;
		public RequestHandler(Socket socket){
			this.socket = socket;
		}

		public void handleRequest(){
			// At this stage, the connection will have been made.
			InetAddress remoteMachine = socket.getInetAddress();
			printMsg("Accepted a connection from "
					+ remoteMachine);

			// Start processing the request.
			try {
				InputStream inStream;
				inStream = socket.getInputStream();

				InputStreamReader isr = new InputStreamReader(inStream);
				BufferedReader lnr = new BufferedReader(isr);

				// For writing.
				OutputStream outStream = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(outStream);

				// First read the GET command( e.g., "GET /index.html HTTP/1.0")
				String command = lnr.readLine();

				printMsg("Got command:" + command);
				// Ignore broken or requests for bookmark icons
				if(command == null ) {
					return;
				}
				if(command.contains("favico")){
					pw.print("HTTP/1.0 404 ERROR\n\n");
				}
				// Handle a regular request
				else{
					pw.print("HTTP/1.0 200 OK\n\n");
					pw.println("<html><body><p>Starting slow request...</p>");
					pw.flush();
					slowRequest();
					pw.println("<p>All done!</p></html></body>");
				}

				// Close the streams.
				pw.close();
				lnr.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Done processing request
		}
		/** Perform a "slow" request by sleeping */
		private void slowRequest()
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



	/** Append a message to the text area and then scroll down so it is visible */
	public void printMsg(String s){
		textArea.append(Thread.currentThread().getName() + ":" + s + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public SlowWebServer(){
		// Setup the GUI
		JFrame frame = new JFrame();
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setColumns(50);
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane textScroller = new JScrollPane(textArea);
		textScroller.setPreferredSize(new Dimension(400, 200));
		frame.getContentPane().add(textScroller);
		frame.setLocation(100, 500);
		frame.setVisible(true);
		frame.setTitle("Simple Server");


		try {
			// Create a listening service for connections
			// at the designated port number.
			ServerSocket srv = new ServerSocket(8080);

			while (true) {

				// The method accept() blocks until a client connects.
				// It returns a socket specifically for the new client.
				printMsg("Webserver: waiting for a connection");
				Socket socket = srv.accept();

				RequestHandler handler = new RequestHandler(socket);
				handler.handleRequest();

			} // end-while

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] argv) {
		new SlowWebServer();
	}

}