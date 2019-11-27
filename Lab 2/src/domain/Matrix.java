package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Matrix {
    private int rows, cols;
    private List<List<Integer>> elems;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        int max = 1500;
        int min = 0;

        Random rand = new Random();

        this.elems = new ArrayList<>(rows);
        for (int i = 0; i < this.rows; i++) {
            this.elems.add(new ArrayList<>(cols));
            for (int j = 0; j < this.cols; j++) {
                this.elems.get(i).add(rand.nextInt((max - min) + 1) + min);
            }
        }
    }

    public List<Integer> getRow(int index) {
        return this.elems.get(index);
    }

    public List<Integer> getCol(int index) {
        List<Integer> col = new ArrayList<>();

        for (List<Integer> row : this.elems) {
            col.add(row.get(index));
        }

        return col;
    }

    public void setRow(int rowNo, List<Integer> row) throws Exception {
        if (row.size() != this.cols) {
            throw new Exception(" matrix limit exceed");
        }
        this.elems.set(rowNo, row);
    }

    public void setCol(int colNo, List<Integer> col) throws Exception {
        if (col.size() != this.rows) {
            throw new Exception("matrix limit exceed");
        }
        for (int i = 0; i < col.size(); i++) {
            this.elems.get(i).set(colNo, col.get(i));
        }
    }

    public int get(int row, int col) {
        return this.elems.get(row).get(col);
    }

    public void set(int row, int col, int value) {
        this.elems.get(row).set(col, value);
    }

    public int getRowsNumber() {
        return rows;
    }

    public int getColsNumber() {
        return cols;
    }

    public int index(int row, int col) {
        return row * this.cols + col;
    }

    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            ss.append(this.elems.get(i).toString()).append("\n");
        }

        return ss.toString();
    }
}
