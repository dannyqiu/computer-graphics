import java.util.*;
import java.io.*;

public class Matrix {

    private static final int DEFAULT_ROWS = 0;
    private static final int DEFAULT_COLS = 4;
    protected ArrayList<double[]> matrix;

    /**
     * Method to create a new two-dimentional matrix to store points
     * @param _rows number of rows in the matrix
     * @param _cols number of columns in the matrix
     */
    protected ArrayList<double[]> newMatrix(int _rows, int _cols) {
        ArrayList<double[]> matrix = new ArrayList<double[]>();
        for (int i=0; i<_rows; i++) {
            matrix.add(new double[_cols]);
        }
        return matrix;
    }

    /**
     * Method to set the matrix to that of the given matrix
     * @param m     matrix to set
     * @param _rows number of rows in the matrix
     * @param _cols number of columns in the matrix
     */
    public void setMatrix(ArrayList<double[]> m) {
        matrix = m;
    }

    public Matrix() {
        matrix = newMatrix(DEFAULT_ROWS, DEFAULT_COLS);
        setMatrix(matrix);
    }

    public Matrix(int _dimensions) {
        matrix = newMatrix(_dimensions, _dimensions);
    }

    public Matrix(int _rows, int _cols) {
        matrix = newMatrix(_rows, _cols);
        setMatrix(matrix);
    }

    /**
     * Turns the matrix into an identity matrix. Assumes that the matrix is a square
     */
    public void identity() {
        if (this.getRows() == this.getCols()) {
            for (int i=0; i<this.getRows(); i++) {
                for (int j=0; j<this.getCols(); j++) {
                    if (i == j) {
                        matrix.get(i)[j] = 1;
                    }
                    else {
                        matrix.get(i)[j] = 0;
                    }
                }
            }
        }
        else {
            throw new NonSquareMatrixException();
        }
    }

    /**
     * Multiplies each element in the matrix by the given value
     * @param s multiplier value for each element
     */
    public void scalarMultiply(double s) {
        for (int i=0; i<this.getRows(); i++) {
            for (int j=0; j<this.getCols(); j++) {
                matrix.get(i)[j] *= s;
            }
        }
    }

    /**
     * Multiples the calling matrix by the given matrix and stores result in
     * the calling matrix
     * @param m matrix to multiply to the calling matrix
     */
    public void matrixMultiply(Matrix m) {
        if (this.getCols() == m.getRows()) {
            ArrayList<double[]> result = newMatrix(this.getRows(), m.getCols()); // Rows is that of original matrix, Columns is that of the multiplying matrix
            for (int i=0; i<this.getRows(); i++) {
                for (int k=0; k<m.getCols(); k++) {
                    double sum = 0;
                    for (int j=0; j<this.getCols(); j++) {
                        sum += matrix.get(i)[j] * m.get(j, k);
                    }
                    result.get(i)[k] = sum;
                }
            }
            setMatrix(result);
        }
        else {
            throw new MatrixMultiplicationDimensionException();
        }
    }

    /**
     * Returns a transpose of the calling matrix
     * @return matrix transpose of the calling matrix
     */
    public Matrix transpose() {
        Matrix result = new Matrix(this.getCols(), this.getRows());
        for (int i=0; i<this.getRows(); i++) {
            for (int j=0; j<this.getCols(); j++) {
                result.matrix.get(j)[i] = matrix.get(i)[j];
            }
        }
        return result;
    }

    /**
     * Adds a row of numbers to the matrix
     * @param rowValues array of doubles to add to the matrix
     */
    public void addRow(double[] rowValues) {
        matrix.add(rowValues);
    }

    /**
     * Returns a copy of the matrix with all values intact
     * @return copy of the matrix
     */
    public Matrix copy() {
        Matrix m = new Matrix(this.getRows(), this.getCols());
        for (int i=0; i<this.getRows(); i++) {
            for (int j=0; j<this.getCols(); j++) {
                m.getMatrix().get(i)[j] = this.get(i, j);
            }
        }
        return m;
    }

    /**
     * Clears all points in the matrix
     */
    public void clear() {
        matrix.clear();
    }

    /**
     * Gets the number of this.getRows() in the matrix
     * @return number of this.getRows()
     */
    public int getRows() {
        return matrix.size();
    }

    /**
     * Gets the number of this.getRows() in the matrix
     * @return number of this.getRows()
     */
    public int getCols() {
        return matrix.get(0).length;
    }

    /**
     * Gets the value of the number at the specified row and column
     * @param row row to get number from
     * @param col column to get number from
     * @return value associated with the given row and column
     */
    public double get(int row, int col) {
        return matrix.get(row)[col];
    }

    /**
     * Returns the matrix as an ArrayList of arrays
     * @return matrix as an ArrayList of arrays
     */
    public ArrayList<double[]> getMatrix() {
        return matrix;
    }

    /**
     * Converts the matrix into human-readable format
     * @return string containing the matrix printout
     */
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("{\n");
        for (int i=0; i<this.getRows(); i++) {
            output.append(Arrays.toString(matrix.get(i)) + ",\n");
        }
        output.append("}\n");
        return output.toString();
    }

}

class NonSquareMatrixException extends RuntimeException {}
class MatrixMultiplicationDimensionException extends RuntimeException {}
