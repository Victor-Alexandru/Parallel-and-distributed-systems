import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Operations {

    public static Polinom baseMultiplication(Polinom p1, Polinom p2) {
        int sizeOfResultCoefficientList = p1.getDegree() + p2.getDegree() + 1;
        List<Integer> resultTerms = new ArrayList<>();
        for (int i = 0; i < sizeOfResultCoefficientList; i++) {
            resultTerms.add(0);
        }
        for (int i = 0; i < p1.getTerms().size(); i++) {
            for (int j = 0; j < p2.getTerms().size(); j++) {
                int index = i + j;
                int value = p1.getTerms().get(i) * p2.getTerms().get(j);
                resultTerms.set(index, resultTerms.get(index) + value);
            }
        }
        return new Polinom(resultTerms);
    }

    public static Polinom baseMultiplicationParallel(Polinom p1, Polinom p2, int nrOfThreads) throws
            InterruptedException {
        //initialize result polynomial with coefficients = 0
        int sizeOfResultCoefficientList = p1.getDegree() + p2.getDegree() + 1;
        List<Integer> coefficients = IntStream.of(new int[sizeOfResultCoefficientList]).boxed().collect(Collectors
                .toList());
        Polinom result = new Polinom(coefficients);

        //calculate the coefficients

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nrOfThreads);
        int step = result.getLength() / nrOfThreads;

        int end;
        for (int i = 0; i < result.getLength(); i += step) {
            end = i + step;
            MultiplicationTask task = new MultiplicationTask(i, end, p1, p2, result);
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(50, TimeUnit.SECONDS);

        return result;
    }

    public static Polinom kParallel(Polinom p1, Polinom p2, int currentDepth)
            throws ExecutionException, InterruptedException {
        if (currentDepth > 4) {
            return KaratsubaSequentialForm(p1, p2);
        }

        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return KaratsubaSequentialForm(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polinom lowP1 = new Polinom(p1.getTerms().subList(0, len));
        Polinom highP1 = new Polinom(p1.getTerms().subList(len, p1.getLength()));
        Polinom lowP2 = new Polinom(p2.getTerms().subList(0, len));
        Polinom highP2 = new Polinom(p2.getTerms().subList(len, p2.getLength()));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Callable<Polinom> task1 = () -> kParallel(lowP1, lowP2, currentDepth + 1);
        Callable<Polinom> task2 = () -> kParallel(Operations.add(lowP1, highP1), Operations
                .add(lowP2, highP2), currentDepth + 1);
        Callable<Polinom> task3 = () -> kParallel(highP1, highP2, currentDepth);

        Future<Polinom> f1 = executor.submit(task1);
        Future<Polinom> f2 = executor.submit(task2);
        Future<Polinom> f3 = executor.submit(task3);

        executor.shutdown();

        Polinom z1 = f1.get();
        Polinom z2 = f2.get();
        Polinom z3 = f3.get();

        executor.awaitTermination(60, TimeUnit.SECONDS);

        //calculate the final result
        Polinom r1 = completePolynom(z3, 2 * len);
        Polinom r2 = completePolynom(subtract(subtract(z2, z3), z1), len);
        Polinom  result = add(add(r1, r2), z1);
        return result;
    }


    private static void addRemainingCoefficients(Polinom p1, Polinom p2, int minDegree, int maxDegree,
                                                 List<Integer> coefficients) {
        if (minDegree != maxDegree) {
            if (maxDegree == p1.getDegree()) {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(p1.getTerms().get(i));
                }
            } else {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(p2.getTerms().get(i));
                }
            }
        }
    }

    public static Polinom add(Polinom p1, Polinom p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        //Add the 2 polynomials
        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(p1.getTerms().get(i) + p2.getTerms().get(i));
        }

        addRemainingCoefficients(p1, p2, minDegree, maxDegree, coefficients);

        return new Polinom(coefficients);
    }


    public static Polinom KaratsubaSequentialForm(Polinom p1, Polinom p2) {
        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return baseMultiplication(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polinom lowP1 = new Polinom(p1.getTerms().subList(0, len));
        Polinom highP1 = new Polinom(p1.getTerms().subList(len, p1.getLength()));
        Polinom lowP2 = new Polinom(p2.getTerms().subList(0, len));
        Polinom highP2 = new Polinom(p2.getTerms().subList(len, p2.getLength()));

        Polinom z1 = KaratsubaSequentialForm(lowP1, lowP2);
        Polinom z2 = KaratsubaSequentialForm(add(lowP1, highP1), add(lowP2, highP2));
        Polinom z3 = KaratsubaSequentialForm(highP1, highP2);

        //calculate the final result with the algorithm
        Polinom r1 = completePolynom(z3, 2 * len);
        Polinom r2 = completePolynom(subtract(subtract(z2, z3), z1), len);
        return add(add(r1, r2), z1);
    }

    public static Polinom subtract(Polinom p1, Polinom p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> terms = new ArrayList<>(maxDegree + 1);

        //Subtract the 2 polynomials
        for (int i = 0; i <= minDegree; i++) {
            terms.add(p1.getTerms().get(i) - p2.getTerms().get(i));
        }

        addRemainingCoefficients(p1, p2, minDegree, maxDegree, terms);

        //remove coefficients starting from biggest power if coefficient is 0

        int i = terms.size() - 1;
        while (terms.get(i) == 0 && i > 0) {
            terms.remove(i);
            i--;
        }

        return new Polinom(terms);
    }

    public static Polinom completePolynom(Polinom p, int offset) {
        List<Integer> terms = new ArrayList<>();
        for (int i = 0; i < offset; i++) {
            terms.add(0);
        }
        for (int i = 0; i < p.getLength(); i++) {
            terms.add(p.getTerms().get(i));
        }
        return new Polinom(terms);
    }


}
