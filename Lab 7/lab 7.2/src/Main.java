import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static void main(String[] args) {
        int QUEUE_CAP = 20;

        List<BigInteger> numbers;


        numbers = generateNumbers(100, 5);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println(numbers.toString());
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
        testParallel(5, numbers, QUEUE_CAP);
        testSequential(5, numbers);

        numbers = generateNumbers(150, 10);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println(numbers.toString());
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
        testParallel(10, numbers, QUEUE_CAP);
        testSequential(10, numbers);

        numbers = generateNumbers(200, 15);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println(numbers.toString());
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
        testParallel(15, numbers, QUEUE_CAP);
        testSequential(15, numbers);

    }

    private static List<BigInteger> generateNumbers(int count, int digitSize) {
        List<BigInteger> numbers = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {

            StringBuilder result = new StringBuilder();
            Random random = new Random();

            for (int j = 0; j < digitSize; j++) {
                result.append(random.nextInt(9) + 1);
            }


            numbers.add(new BigInteger(result.toString()));
        }

        return numbers;
    }


    private static void ParallelOperations(List<BigInteger> numbers, int queueCap) {
        int N = numbers.size();

        List<ArrayBlockingQueue<BigInteger>> queues = new ArrayList<>(N - 1);

        for (int i = 0; i < N - 1; i++) {
            queues.add(new ArrayBlockingQueue<>(queueCap));
        }

        List<Thread> threads = new ArrayList<>(N - 1);

        threads.add(new Thread(new AddOperation(numbers.get(0), numbers.get(1), queues.get(0))));

        for (int i = 2; i < N; i++) {
            threads.add(new Thread(new AddOperation(numbers.get(i), queues.get(i - 2), queues.get(i - 1))));
        }

        for (int i = 0; i < N - 1; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < N - 1; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void SeqAdd(List<BigInteger> numbers) {
        BigInteger rez = BigInteger.ZERO;
        for (BigInteger number : numbers) {
            rez = rez.add(number);
        }
    }


    private static void testParallel(int digitSize, List<BigInteger> numbers, int queueCap) {
        long startTime = System.nanoTime();
        ParallelOperations(numbers, queueCap);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Parallel: DIGIT SIZE  " + digitSize + ": " + duration + " ms");

    }

    private static void testSequential(int digit_size, List<BigInteger> numbers) {
        long startTime = System.nanoTime();
        SeqAdd(numbers);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Sequential: DIGIT SIZE " + digit_size + ": " + duration + " ms");

    }


}