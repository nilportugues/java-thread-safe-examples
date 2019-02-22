

public class ThreadSafety {

    private static Counter counter = new Counter();

    public static void main(String[] args) throws InterruptedException {

        ProcessingThread pt = new ProcessingThread(counter);

        Thread t1 = new Thread(pt, "t1");
        t1.start();
        Thread t2 = new Thread(pt, "t2");
        t2.start();
        //wait for threads to finish processing
        t1.join();
        t2.join();
        System.out.println("Processing count="+pt.getCount());
    }

}

class Counter {
    private int count = 0;

   // https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html   
   // this is using "Locks". Each lock protects a different use case.  
   // It's better than using synchronized(this) as it is more fine-grained or using synchronized methods.

    private Object lock1 = new Object(); 
    private Object lock2 = new Object();

    public int getCount() {
         synchronized(lock1) {
           return count;
         }
    }

    public void increment() {
         synchronized(lock2) {
          count++;
         }
    }
}


class ProcessingThread implements Runnable{
    private Counter count;
    
    public ProcessingThread(Counter count) {
      this.count = count;
    }

    @Override
    public void run() {
        for(int i=1; i < 5; i++){
            processSomething(i);
            count.increment();
        }
    }

    public int getCount() {
        return this.count.getCount();
    }

    private void processSomething(int i) {
        // processing some job
        try {
            Thread.sleep(i*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}

