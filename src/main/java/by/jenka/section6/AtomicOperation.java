package by.jenka.section6;

import java.util.Random;

public class AtomicOperation {

    public static void main(String[] args) throws InterruptedException {
        Metrics metrics = new Metrics();
        BusinessLogic logic = new BusinessLogic(metrics);
        BusinessLogic logic2 = new BusinessLogic(metrics);
        MetricsPrinter printer = new MetricsPrinter(metrics);

        logic.start();
        logic2.start();
        printer.start();

//        logic.join();
//        logic2.join();
//        printer.join();
    }

    public static class MetricsPrinter extends Thread {

        private Metrics metrics;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
                System.out.println("Current Average : " + metrics.getAverage());
            }
        }
    }
    public static class BusinessLogic extends  Thread {
        private Metrics metrics;

        private Random random  = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                }
                long endTime = System.currentTimeMillis();
                metrics.addSample(endTime - startTime);
            }
        }

    }
    public static class Metrics {
        private long count = 0;
        private volatile double average = 0.0;

        public void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        public double getAverage() {
            return average;
        }
    }
}
