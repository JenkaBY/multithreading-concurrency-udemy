package by.jenka.section10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualThreadsDemo {
    private static final int NUMBER_OF_THREADS = 100;

    public static void main(String[] args) throws InterruptedException {
//        Runnable runnable = () -> System.out.println("Inside thread: " + Thread.currentThread());

        List<Thread> threads = new ArrayList<>(NUMBER_OF_THREADS);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {

            final Thread virtualThread = Thread.ofPlatform().unstarted(new BlockingTask());
            threads.add(virtualThread);
        }
        long start = System.currentTimeMillis();
        for (final Thread virtualThread : threads) {

            virtualThread.start();
        }

        for (final Thread virtualThread : threads) {
            virtualThread.join();
        }
        long end = System.currentTimeMillis();
        System.out.println("Total execution time: " + (end - start) + " ms");
    }


    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            System.out.println("Inside thread: " + Thread.currentThread());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            new ReentrantLock().lock();
            System.out.println("Inside thread: " + Thread.currentThread() + " after blocking call");
        }
    }
}
