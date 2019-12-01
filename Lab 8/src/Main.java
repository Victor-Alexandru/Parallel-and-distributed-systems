import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        List<DGraph> graphs = new ArrayList<>();

        graphs.add(new DGraph(100));
        graphs.add(new DGraph(500));
        graphs.add(new DGraph(1000));

        System.out.println("One thread aka sequencial");
        FindCycle(graphs, 1);

        System.out.println("4 Threads aka Paralel");
        FindCycle(graphs, 4);


        System.out.println("8 Threads aka Paralel");
        FindCycle(graphs, 8);

    }

    private static void FindCycle(List<DGraph> graphs, int threadCount) throws InterruptedException {
        for (int i = 0; i < graphs.size(); i++) {
            long startTime = System.nanoTime();
            findCycle(graphs.get(i), threadCount);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("Number of nodes " + graphs.get(i).size() + ": " + duration + " ms");
        }
    }

    private static void findCycle(DGraph graph, int threadCount) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        Lock lock = new ReentrantLock();
        List<Integer> result = new ArrayList<>(graph.size());

        for (int i = 0; i < graph.size(); i++) {
            pool.execute(new FindingTask(graph, i, result, lock));
        }

        pool.shutdown();

        pool.awaitTermination(10, TimeUnit.SECONDS);
    }

}