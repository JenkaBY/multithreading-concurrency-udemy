package by.jenka.section6;


public class DataRace {

    public static void main(String[] args) throws InterruptedException {
        SharedClass sharedClass = new SharedClass();

        Thread t1 = new Thread(() -> {
            for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
                sharedClass.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
                sharedClass.checkForRaceCondition();
            }
        });
        t1.setDaemon(true);
        t2.setDaemon(true);
        t2.start();
        t1.start();
        System.out.println("all started");
        t1.join();
        t2.join();
    }

    public static class SharedClass {
        private int x = 0;
        private int y = 0;
//        Fix data race
//        private volatile int x = 0;
//        private volatile int y = 0;

        public void increment() {
            x++;
            y++;
        }

        public void checkForRaceCondition() {
            if (y > x) {
                System.out.println("y > x - Data Race is detected");
            }
        }
    }
}
