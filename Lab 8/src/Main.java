import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        int GRAPHS_COUNT = 101;

        List<DGraph> graphs = new ArrayList<>();

        for (int i = 1; i <= GRAPHS_COUNT; i++) {
            graphs.add(new DGraph(i * 10));
        }

        System.out.println("Sequential");
        batchTesting(graphs, 1);

        System.out.println("Parallel");
        batchTesting(graphs, 4);

    }

    private static void batchTesting(List<DGraph> graphs, int threadCount) throws InterruptedException {
        for (int i = 0; i < graphs.size(); i++) {
            test(i, graphs.get(i), threadCount);
        }
    }

    private static void test(int level, DGraph graph, int threadCount) throws InterruptedException {
        long startTime = System.nanoTime();
        findHamiltonian(graph, threadCount);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        if (level == 1 || level == 50 || level == 100)
            System.out.println("Level " + level + ": " + duration + " ms");
    }

    private static void findHamiltonian(DGraph graph, int threadCount) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        Lock lock = new ReentrantLock();
        List<Integer> result = new ArrayList<>(graph.size());

        for (int i = 0; i < graph.size(); i++) {
            pool.execute(new Task(graph, i, result, lock));
        }

        pool.shutdown();

        pool.awaitTermination(10, TimeUnit.SECONDS);
    }

}