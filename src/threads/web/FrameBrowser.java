package threads.web;

//File: FrameBrowser.java (Threading)
//
//Author: Timothy Wood
//Created: Nov 19, 2013
//
//Downloads a threads.web page and prints it to a textArea area.

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.*;

public class FrameBrowser extends JFrame {
	private JTextArea textArea;

	/** Append a message to the text area and then scroll down so it is visible */
	public void printMsg(String s){
		textArea.append(s + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}


	public void loadPage() {
		try {
			String host = "localhost";
			int port = 8080;
			String file = "/";

			// Open a socket to a known webserver.
			long start = System.currentTimeMillis();
			Socket sock = new Socket(host, port);
			InetAddress remoteMachine = sock.getInetAddress();
			printMsg("Connection made to " + remoteMachine);

			// Now create the output and input streams. Start with output.
			PrintWriter pw = new PrintWriter(sock.getOutputStream());

			// Next: input.
			InputStreamReader isr = new InputStreamReader(sock.getInputStream());
			BufferedReader lnr = new BufferedReader(isr);

			// Ask for the file
			// A valid HTTP request is: "GET filename HTTP/1.0" followed by a blank line.
			pw.println("GET " + file + " HTTP/1.0");
			pw.println();
			pw.flush();

			// Read in the reply one line at a time and print it to the screen
			String s = lnr.readLine();
			while (s != null) {
				printMsg("\n" + s);
				s = lnr.readLine();
			}

			// Close the streams.
			pw.close();
			lnr.close();
			sock.close();
			printMsg("\nTotal Time: " + (System.currentTimeMillis() - start));
		} catch (IOException e) {
			printMsg(e.getLocalizedMessage() + "\n");
		}

	}

	public static void main(String[] argv) {
		new FrameBrowser();
	}

	public FrameBrowser() {

		JFrame frame = new JFrame();
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setColumns(50);
		this.setSize(400, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane textScroller = new JScrollPane(textArea);
		textScroller.setPreferredSize(new Dimension(400, 200));
		JButton reload = new JButton("Reload");
		this.getContentPane().add(reload, BorderLayout.SOUTH);
		// example of an anonymous inner class
		reload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadPage();
			}
		});

		this.getContentPane().add(textScroller);
		this.setLocation(100, 500);
		this.setVisible(true);


	}
}