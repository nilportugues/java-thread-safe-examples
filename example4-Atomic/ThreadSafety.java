import java.util.concurrent.atomic.AtomicInteger;


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

// https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html
// Using atomic classes from concurrent.atomic avoids the usage of sychronized methods or locks.
class Counter {
    private AtomicInteger count = new AtomicInteger(0);

    public int getCount() {
           return count.get();
    }

    public void increment() {
           count.incrementAndGet();
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

