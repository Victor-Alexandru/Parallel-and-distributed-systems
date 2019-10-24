import domain.Matrix;
import threads.MatrixAddition;
import threads.MatrixMultiplication;
import threads.MatrixOperation;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;


public class Main {

    private static Matrix operation(String sign, Matrix a, Matrix b, int threadCount) throws Exception {
        int rowCount = min(a.getRowsNumber(), b.getRowsNumber());
        int colCount = min(a.getColsNumber(), b.getColsNumber());

        Matrix result = new Matrix(rowCount, colCount);

        List<MatrixOperation> threads = new ArrayList<>();

        switch (sign) {
            case "+":
                for (int i = 0; i < threadCount; i++) {
                    threads.add(new MatrixAddition(a, b, result));
                }
                break;
            case "*":
                for (int i = 0; i < threadCount; i++) {
                    threads.add(new MatrixMultiplication(a, b, result));
                }
                break;

        }

        for (int i = 0; i < threadCount; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads.get(i).join();
        }
        return result;
    }


    public static void main(String[] args) throws Exception {

        Matrix a = new Matrix(500, 500);
        Matrix b = new Matrix(500, 500);

        float start = System.nanoTime() / 1000000;
        operation("+", a, b, 4);
        float end = System.nanoTime() / 1000000;
        System.out.println("Addition:" + (end - start) / 1000 + " seconds, " + 4 + " threads");


        start = System.nanoTime() / 1000000;
        operation("+", a, b, 8);
        end = System.nanoTime() / 1000000;
        System.out.println("Addition:" + (end - start) / 1000 + " seconds, " + 8 + " threads");

        start = System.nanoTime() / 1000000;
        operation("+", a, b, 16);
        end = System.nanoTime() / 1000000;
        System.out.println("Addition: " + (end - start) / 1000 + " seconds, " + 16 + " threads");


        start = System.nanoTime() / 1000000;
        operation("*", a, b, 4);
        end = System.nanoTime() / 1000000;
        System.out.println(" Multiplication:" + (end - start) / 1000 + " seconds, " + 4 + " threads");


        start = System.nanoTime() / 1000000;
        operation("*", a, b, 8);
        end = System.nanoTime() / 1000000;
        System.out.println(" Multiplication:" + (end - start) / 1000 + " seconds, " + 8 + " threads");

        start = System.nanoTime() / 1000000;
        operation("*", a, b, 16);
        end = System.nanoTime() / 1000000;
        System.out.println(" Multiplication: " + (end - start) / 1000 + " seconds, " + 16 + " threads");


    }
}
