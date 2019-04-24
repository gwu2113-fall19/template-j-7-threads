package threads.example;

public class ThreadTwiceExample implements Runnable {
    @Override
    public void run(){
        Thread t = Thread.currentThread();
        System.out.println(t.getName()+" is executing.");

    }
    public static void main(String args[]){
        Thread th1 = new Thread(new ThreadTwiceExample(), "th1");
        th1.start();
        th1.start();
    }
}