package threads.dograce;

//File: Race3.java 
//
//Author: Rahul Simha
//Created: Nov 17, 1998
//Modified: Nov 10, 2000.
//
//A simple simulation with threads.

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import random.UniformRandom;

class NewFrame3 extends JFrame {

	// For drawing the race results.
	JPanel drawingArea;

	public NewFrame3() {
		// Frame properties.
		this.setTitle("Dog3 Race");
		this.setResizable(true);
		this.setSize(600, 200);

		Container cPane = this.getContentPane();
		// cPane.setLayout (new BorderLayout());

		// Quit button.
		JPanel p = new JPanel();
		JButton quitb = new JButton("QUIT");
		quitb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				System.exit(0);
			}
		});
		p.add(quitb);

		// Pressing "start" calls race()
		JButton startb = new JButton("START");
		startb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				race();
			}
		});
		p.add(startb);

		// Now add the panel to the frame.
		cPane.add(p, BorderLayout.SOUTH);

		// A JPanel to draw the results.
		drawingArea = new JPanel();
		cPane.add(drawingArea, BorderLayout.CENTER);

		this.setVisible(true);
	}

	void race() {
		Dimension D = drawingArea.getSize();
		drawingArea.getGraphics().clearRect(0, 0, D.width, D.height);


		// Finish-line is at the right end of the canvas.
		int finishLine = D.width;

		// Create two dog instances with different ID's.
		Dog3 d1 = new Dog3(1, drawingArea, this);
		Dog3 d2 = new Dog3(2, drawingArea, this);

		// Create a Thread instance for each dog.
		// Note: the class Dog3 must implement the
		// Runnable interface.
		Thread dThread1 = new Thread(d1);
		Thread dThread2 = new Thread(d2);

		// Start running the threads.
		// ("start" is a method in Thread).
		dThread1.start();
		dThread2.start();
	}
	
    boolean raceOver;

    // The same method is called to check whether the race  
    // is complete or to indicate that it is complete.  

    public boolean raceFinished (boolean set)
    {
        boolean returnVal = false;
        if (!set)
            returnVal = raceOver;
        else {
            raceOver = true;
            returnVal = raceOver;
        }
        return returnVal;
    }

}

class Dog3 implements Runnable {
	
	NewFrame3 f;  // To find out whether the race is over. 
	public int position = 20; // Starting position.
	int ID; // An ID.
	JPanel drawingArea; // The panel on which to draw.

	public Dog3(int ID, JPanel drawingArea, NewFrame3 f) {
		this.ID = ID;
		this.drawingArea = drawingArea;

		// Draw ID on panel.
		Graphics g = drawingArea.getGraphics();
		g.drawString("" + ID, 5, 20 * ID + 8);
		
		//Save the frame reference
		this.f = f;
	}

	public void move() {
		// Move a random amount.
		int newPosition = position + (int) UniformRandom.uniform(50, 100);

		// Draw new position.
		Graphics g = drawingArea.getGraphics();
		int size = newPosition - position;
		g.fillRect(position, 20 * ID, size, 10);
		position = newPosition;
	}

	// Must implement this method to implement
	// the Runnable interface.
	public void run() {
		// Compute the finish line distance.
		int finishLine = drawingArea.getSize().width;

		// While not complete...
		while (position < finishLine) {
			// Sleep
			try {
				Thread.sleep((int) UniformRandom.uniform(300, 600));
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			// Check whether race is over. 
	   	    if (f.raceFinished (false)) break;

		    // Move if race is still on. 
		    move ();
        }
        if (position >= finishLine)
            f.raceFinished (true);
	}

}

public class Race3 {
	public static void main(String[] argv) {
		NewFrame3 nf = new NewFrame3();
	}
}