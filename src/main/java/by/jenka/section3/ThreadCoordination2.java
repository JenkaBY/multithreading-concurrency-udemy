package by.jenka.section3;

import java.math.BigInteger;

public class ThreadCoordination2 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting the program");
        Thread thread = new Thread(new LongComputationTask(new BigInteger("20000"), new BigInteger("1000000")));

        thread.start();
        Thread.sleep(1000);
//
        thread.setDaemon(true);
        thread.interrupt();
        System.out.println("Ending the program");
    }

    private static class LongComputationTask implements Runnable {
        private final BigInteger base;
        private final BigInteger pow;

        public LongComputationTask(BigInteger base, BigInteger pow) {
            this.base = base;
            this.pow = pow;
        }

        @Override
        public void run() {
            System.out.println(base + "**" + pow + "=" + pow(base, pow));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Terminated calculation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
