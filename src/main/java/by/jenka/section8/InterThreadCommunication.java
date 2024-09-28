package by.jenka.section8;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InterThreadCommunication {

    public static void main(String[] args) throws InterruptedException {
        SomeClass2 sharedResource = new SomeClass2();
        Thread shouldReachSuccess = new Thread(() -> {
            try {
                sharedResource.declareSuccess();
            } catch (InterruptedException e) {
                System.out.println("hasn't been reached success");
            }
        });

        Thread shouldTerminate = new Thread(() -> {
            sharedResource.finishWork();
        });
        shouldTerminate.setName("FINISH");
        shouldReachSuccess.setName("SUCCESS");
        shouldTerminate.setDaemon(true);
        shouldReachSuccess.setDaemon(true);

        shouldReachSuccess.start();
        shouldTerminate.start();

        shouldTerminate.join();
        shouldReachSuccess.join();
    }

    public static class SomeClass2 {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        boolean isCompleted = false;

        public void declareSuccess() throws InterruptedException {
            System.out.println("[S]Acquiring lock: " + lock);
            lock.lock();
            System.out.println("[S]Acquired lock: " + lock);
            try {
                while (!isCompleted) {
                    System.out.println("[S]Awaiting condition: " + lock);
                    condition.await();
                    System.out.println("[S]Condition awaited: " + lock);
                }
            } finally {
                System.out.println("[S]unlocking: " + lock);
                lock.unlock();
                System.out.println("[S]unlocked: " + lock);
            }

            System.out.println("Success!!");
        }

        public void finishWork() {
            System.out.println("[T]Acquiring lock.: " + lock);
            lock.lock();
            System.out.println("[T]lock acquired for finish work: " + lock);
            try {
                isCompleted = true;
                System.out.println("[T]Signaling condition: " + lock);
                condition.signal();
                System.out.println("[T]Signaled for finish work: " + lock);
            } finally {
                System.out.println("[T]unlocking: " + lock);
                lock.unlock();
                System.out.println("[T]unlocked: " + lock);
            }
        }
    }
}
