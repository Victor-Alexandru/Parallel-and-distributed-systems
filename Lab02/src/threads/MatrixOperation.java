package threads;

import domain.Matrix;

import java.util.ArrayList;
import java.util.List;

public class MatrixOperation extends Thread {
    Matrix a;
    Matrix b;
    Matrix result;

    List<Integer> rows = new ArrayList<>();
    List<Integer> cols = new ArrayList<>();

    MatrixOperation(Matrix a, Matrix b, Matrix result) {
        this.a = a;
        this.b = b;
        this.result = result;
    }

    public void addPoint(int row, int col){
        rows.add(row);
        cols.add(col);
    }

}
