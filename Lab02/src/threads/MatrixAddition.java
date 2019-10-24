package threads;

import domain.Matrix;

public class MatrixAddition extends MatrixOperation {
    public MatrixAddition(Matrix a, Matrix b, Matrix result) {
        super(a, b, result);
    }

    @Override
    public void run() {
        //here we compute the addition of two matrices in the result matrix
        for (int i = 0; i < this.a.getRowsNumber(); i++) {
            for (int j = 0; j < this.a.getColsNumber(); j++) {
                this.result.set(i, j, this.a.get(i, j) + this.b.get(i, j));
            }
        }
    }
}
