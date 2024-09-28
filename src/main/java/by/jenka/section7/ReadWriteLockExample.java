package by.jenka.section7;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    private static final int HIGHEST_PRICE = 1000;

    public static void main(String[] args) throws InterruptedException {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();

        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }


        Thread writerThread = new Thread(() -> {
            while (true) {
                inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        });
        writerThread.setName("DB Writer");
        writerThread.setDaemon(true);
        writerThread.start();

        int numberOfReaderThreads = 10;
        List<Thread> readerThreads = new ArrayList<>(numberOfReaderThreads);
        for (int i = 0; i < numberOfReaderThreads; i++) {
            Thread readerThread = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });
            readerThread.setName("DB Reader " + i);
            readerThread.setDaemon(true);
            readerThreads.add(readerThread);
        }

        long startReadingTime = System.currentTimeMillis();
        for (Thread readerThread : readerThreads) {
            readerThread.start();
        }
        for(Thread readerThread : readerThreads) {
            readerThread.join();
        }
        long endReadingTime = System.currentTimeMillis();

        System.out.println(String.format("Reading took %d ms", endReadingTime - startReadingTime));
    }


    public static class InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        private ReentrantLock lock = new ReentrantLock();
        private ReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = reentrantReadWriteLock.readLock();
        private Lock writeLock = reentrantReadWriteLock.writeLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
//            lock.lock();
            readLock.lock();
            try {

                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);

                Integer toKey = priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);

                int sum = 0;
                for (int numberOfItemsForPrice : rangeOfPrices.values()) {
                    sum += numberOfItemsForPrice;
                }

                return sum;
            } finally {
//                lock.unlock();
                readLock.unlock();
            }

        }

        public void addItem(int price) {
//            lock.lock();
            writeLock.lock();
            try {

                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null) {
                    priceToCountMap.put(price, 1);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice + 1);
                }
            } finally {
//                lock.unlock();
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
            writeLock.lock();
//            lock.lock();
            try {

                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice - 1);
                }
            }
            finally {
//                lock.unlock();
                writeLock.unlock();
            }
        }
    }

}
