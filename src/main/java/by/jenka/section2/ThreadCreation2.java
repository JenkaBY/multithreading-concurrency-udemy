package by.jenka.section2;

public class ThreadCreation2 {


    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("Hello from : '" + Thread.currentThread().getName() + "'");
        });

        thread.setName("new-worker-thread");
        thread.start();

        new NewThread().start();
    }

    private static class NewThread extends Thread {

        @Override
        public void run() {
            System.out.println("Hello from : '" + this.getName() + "'");
        }
    }
}
