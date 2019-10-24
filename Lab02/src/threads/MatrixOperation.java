package threads;

import domain.Matrix;

public class MatrixOperation extends Thread {
    Matrix a;
    Matrix b;
    Matrix result;

    MatrixOperation(Matrix a, Matrix b, Matrix result) {
        this.a = a;
        this.b = b;
        this.result = result;
    }
}
