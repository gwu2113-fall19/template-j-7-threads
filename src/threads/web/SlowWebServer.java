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

	/** Perform a "slow" reqeust by sleeping */
	public void slowRequest()
	{
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

				// When a connection is made, get the socket.
				// The method accept() blocks until then.
				printMsg("Webserver: waiting for a connection");
				Socket soc = srv.accept();

				ReqHandler r = new ReqHandler(soc);
				Thread t = new Thread(r);
				printMsg("Starting thread");
				t.start();

			} // end-while

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] argv) {
		new SlowWebServer();
	}

	/** Inner class for handling requests. This makes it easy to update the text area. */
	class ReqHandler implements Runnable {
		private Socket socket;

		public ReqHandler(Socket s) {
			socket = s;
		}
		public void run() {
			// At this stage, the connection will have been made.
			InetAddress remoteMachine = socket.getInetAddress();
			printMsg("Accepted a connection from "
					+ remoteMachine);

			// We are going to both read and write. Start with reading.
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
				if(command.contains("favico")){
					pw.print("HTTP/1.0 200 OK\n\n");
				}
				else{
					//long start = System.currentTimeMillis();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

