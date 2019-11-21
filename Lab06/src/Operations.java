import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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


    public static Polinom multiplicationKaratsubaSequentialForm(Polinom p1, Polinom p2) {
        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return baseMultiplication(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polinom lowP1 = new Polinom(p1.getTerms().subList(0, len));
        Polinom highP1 = new Polinom(p1.getTerms().subList(len, p1.getLength()));
        Polinom lowP2 = new Polinom(p2.getTerms().subList(0, len));
        Polinom highP2 = new Polinom(p2.getTerms().subList(len, p2.getLength()));

        Polinom z1 = multiplicationKaratsubaSequentialForm(lowP1, lowP2);
        Polinom z2 = multiplicationKaratsubaSequentialForm(add(lowP1, highP1), add(lowP2, highP2));
        Polinom z3 = multiplicationKaratsubaSequentialForm(highP1, highP2);

        //calculate the final result
        Polinom r1 = shift(z3, 2 * len);
        Polinom r2 = shift(subtract(subtract(z2, z3), z1), len);
        Polinom result = add(add(r1, r2), z1);
        return result;
    }

    public static Polynomial shift(Polynomial p, int offset) {
        List<Integer> coefficients = new ArrayList<>();
        for (int i = 0; i < offset; i++) {
            coefficients.add(0);
        }
        for (int i = 0; i < p.getLength(); i++) {
            coefficients.add(p.getCoefficients().get(i));
        }
        return new Polynomial(coefficients);
    }


}
