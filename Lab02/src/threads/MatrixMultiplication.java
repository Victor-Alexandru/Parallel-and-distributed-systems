package threads;

import domain.Matrix;

import java.util.List;

public class MatrixMultiplication extends MatrixOperation {
    public MatrixMultiplication(Matrix a, Matrix b, Matrix result) {
        super(a, b, result);
    }

    @Override
    public void run() {
        //here we compute the multiplication of two matrices in the result matrix
        for(int row =0;row<a.getRowsNumber();row++ )
        {
            List<Integer> currentRow = this.a.getRow(row);
            for (int col=0;col<b.getColsNumber();col++)
            {  int sum =0;

                List<Integer> currentCol = this.a.getRow(row);
                for(int i =0 ;i<currentRow.size();i++)
                {
                    int rowElem = currentRow.get(i);
                    int colElem = currentCol.get(i);
                    sum = sum + (rowElem*colElem);
                }
                result.set(row,col,sum);
            }

        }
    }
}
