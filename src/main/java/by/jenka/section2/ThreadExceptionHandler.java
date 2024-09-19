package by.jenka.section2;

public class ThreadExceptionHandler {


    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Intentional exception");
        });

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Critical exception happened in the thread " + t.getName()
                + " the error is " + e.getMessage());
            }
        });

        thread.start();
    }
}
