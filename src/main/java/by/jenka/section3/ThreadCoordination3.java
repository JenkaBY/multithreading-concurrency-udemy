package by.jenka.section3;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ThreadCoordination3 {

    public static void main(String[] args) {

        List<Long> inputNumbers = Arrays.asList(0L, 3435L, 35435L, 2324L);
        inputNumbers.stream().map(FactorialCalculationThread::new)
                .peek(Thread::start)
                .peek(t -> {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } )
                .forEach(thread -> {
                    if (thread.isFinished()) {
                        System.out.println("Factorial of " + thread.inputNumber + " is " + thread.result);
                    } else {
                        System.out.println("The calculation for " + thread.inputNumber + " is still running" );



                    }
                });

    }

    private static class FactorialCalculationThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialCalculationThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial(long inputNumber) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = inputNumber; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }
}
