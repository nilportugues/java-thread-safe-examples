

public class ThreadSafety {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

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

    public synchronized int getCount() {
         return count;
    }

    public synchronized void increment() {
          count++;
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

