import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static int[][] victorOne = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };
    private static int[][] victorTwo = {
            {4, 231, 5},
            {2432, 6, 321},
            {5, 124, 1}
    };
    private static int[][] victorThree = {
            {2, 9, 32131},
            {0, 32131, 321},
            {3, 0, 3213}
    };
    private static int[][] firstComputed = new int[victorOne[0].length][victorTwo.length];
    private static int[][] secondCOmputed = new int[victorOne[0].length][victorTwo.length];

    private static final Lock lock = new ReentrantLock();

    private static final Condition rowDone = lock.newCondition();

    private static boolean isRowCompleted(int[][] mat, int row) {
        for (int i = 0; i < mat.length; i++) {
            if (mat[row][i] == 0) return false;
        }
        return true;
    }


    static class FirstProductThread extends Thread {
        int row;

        FirstProductThread(int row) {
            this.row = row;
        }

        public void run() {
            lock.lock();

            for (int j = 0; j < victorTwo[row].length; j++) { // bColumn
                for (int k = 0; k < victorOne[row].length; k++) { // aColumn
                    firstComputed[row][j] += victorOne[row][k] * victorTwo[k][j];
                }
            }
            rowDone.signal();
            lock.unlock();
        }
    }

    static class SecondProductThread extends Thread {
        int row;

        SecondProductThread(int row) {
            this.row = row;
        }

        public void run() {
            lock.lock();

            try {
                while (!isRowCompleted(firstComputed, row)) {
                    rowDone.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < firstComputed[row].length; j++) { // bColumn
                for (int k = 0; k < victorThree[row].length; k++) { // cColumn
                    secondCOmputed[row][j] += firstComputed[row][k] * victorThree[k][j];
                }
            }
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        //start timer
        long startTime;
        long stopTime;
        long elapsedTime;

        startTime = System.currentTimeMillis();
        //Create all threads for product
        ExecutorService executorServiceAB = Executors.newFixedThreadPool(6);
        for (int rows = 0; rows < victorOne.length; rows++) {
            FirstProductThread thr = new FirstProductThread(rows);
            executorServiceAB.submit(thr);
        }


        ExecutorService executorServiceABC = Executors.newFixedThreadPool(6);
        for (int rows = 0; rows < victorOne.length; rows++) {
            SecondProductThread thr = new SecondProductThread(rows);
            executorServiceABC.submit(thr);
        }


        if (!executorServiceAB.awaitTermination(10, TimeUnit.MILLISECONDS))
            executorServiceAB.shutdown();
        if (!executorServiceABC.awaitTermination(10, TimeUnit.MILLISECONDS))
            executorServiceABC.shutdown();


        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Time : " + elapsedTime);


        System.out.println("First mutiplication");
        for (int i = 0; i < firstComputed.length; i++) {
            for (int j = 0; j < firstComputed[0].length; j++) {
                System.out.print(firstComputed[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Second multiplication ");
        for (int i = 0; i < secondCOmputed.length; i++) {
            for (int j = 0; j < secondCOmputed[0].length; j++) {
                System.out.print(secondCOmputed[i][j] + " ");
            }
            System.out.println();
        }
    }


}