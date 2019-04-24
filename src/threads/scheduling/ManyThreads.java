package threads.scheduling;

// CS2113
// Threads competing for the CPU
// Author: Tim Wood
// Written: 11/7/2011

public class ManyThreads implements Runnable {

	private int iterations;

	public ManyThreads(int i) {
		iterations = i;
	}

	public void run() {
		for (int i = 0; i < iterations; i++) {
			System.out.println(Thread.currentThread().getName() + " is running.");

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println(Thread.currentThread().getName() + " is DONE.");
	}

	public static void main(String[] args) {

		int numIterations = 10;
		int numThreads = 5;

		for (int i = 0; i < numThreads; i++) {
			Thread t = new Thread(new ManyThreads(numIterations));
			t.setName("Thread " + i);
			t.start();
		}

	}
}
