package by.jenka.section2;

public class ThreadCreation {


    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("We are in thread : '" + Thread.currentThread().getName() + "'");
            System.out.println("Current thread priority : " + Thread.currentThread().getPriority());
        });

        thread.setName("new-worker-thread");

        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("We are in thread : '" + Thread.currentThread().getName() + "' before starting thread");
        thread.start();
        System.out.println("We are in thread : '" + Thread.currentThread().getName() + "'after starting thread");

        Thread.sleep(10000);
    }
}
