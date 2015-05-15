import java.util.*;
import java.io.*;

public class Matrix {

    private static final int DEFAULT_ROWS = 0;
    private static final int DEFAULT_COLS = 4;
    protected ArrayList<double[]> matrix;

    /**
     * Method to create a new two-dimensional matrix to store points
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

    /**
     * Turns the calling matrix into the appropriate translation matrix using
     * x, y, and z as the translation offsets
     * @param x shift in the x-coordinate
     * @param y shift in the y-coordinate
     * @param z shift in the z-coordinate
     */
    public void makeTranslate(double x, double y, double z) {
        matrix = newMatrix(0, 4);
        setMatrix(matrix);
        double[] xShift = {1.0, 0.0, 0.0, x};
        double[] yShift = {0.0, 1.0, 0.0, y};
        double[] zShift = {0.0, 0.0, 1.0, z};
        double[] identity = {0.0, 0.0, 0.0, 1.0};
        addRow(xShift);
        addRow(yShift);
        addRow(zShift);
        addRow(identity);
    }

    /**
     * Turns the calling matrix into the appropriate scale matrix using
     * x, y and z as the scale factors
     * @param x scale in the x-coordinate
     * @param y scale in the x-coordinate
     * @param z scale in the x-coordinate
     */
    public void makeScale(double x, double y, double z) {
        matrix = newMatrix(0, 4);
        setMatrix(matrix);
        double[] xScale = {x, 0.0, 0.0, 0.0};
        double[] yScale = {0.0, y, 0.0, 0.0};
        double[] zScale = {0.0, 0.0, z, 0.0};
        double[] identity = {0.0, 0.0, 0.0, 1.0};
        addRow(xScale);
        addRow(yScale);
        addRow(zScale);
        addRow(identity);
    }

    /**
     * Turns the calling matrix into the appropriate rotation matrix using
     * theta as the angle of rotation and X as the axis of rotation.
     * @param theta angle to rotate in degrees
     */
    public void makeRotX(double theta) {
        matrix = newMatrix(0, 4);
        setMatrix(matrix);
        double[] xRotate = {1.0, 0.0, 0.0, 0.0};
        double[] yRotate = {0.0, Math.cos(Math.toRadians(theta)), -Math.sin(Math.toRadians(theta)), 0.0};
        double[] zRotate = {0.0, Math.sin(Math.toRadians(theta)), Math.cos(Math.toRadians(theta)), 0.0};
        double[] identity = {0.0, 0.0, 0.0, 1.0};
        addRow(xRotate);
        addRow(yRotate);
        addRow(zRotate);
        addRow(identity);
    }

    /**
     * Turns the calling matrix into the appropriate rotation matrix using
     * theta as the angle of rotation and Y as the axis of rotation.
     * @param theta angle to rotate in degrees
     */
    public void makeRotY(double theta) {
        matrix = newMatrix(0, 4);
        setMatrix(matrix);
        double[] xRotate = {Math.cos(Math.toRadians(theta)), 0.0, -Math.sin(Math.toRadians(theta)), 0.0};
        double[] yRotate = {0.0, 1.0, 0.0, 0.0};
        double[] zRotate = {Math.sin(Math.toRadians(theta)), 0.0, Math.cos(Math.toRadians(theta)), 0.0};
        double[] identity = {0.0, 0.0, 0.0, 1.0};
        addRow(xRotate);
        addRow(yRotate);
        addRow(zRotate);
        addRow(identity);
    }

    /**
     * Turns the calling matrix into the appropriate rotation matrix using
     * theta as the angle of rotation and Z as the axis of rotation.
     * @param theta angle to rotate in degrees
     */
    public void makeRotZ(double theta) {
        matrix = newMatrix(0, 4);
        setMatrix(matrix);
        double[] xRotate = {Math.cos(Math.toRadians(theta)), -Math.sin(Math.toRadians(theta)), 0.0, 0.0};
        double[] yRotate = {Math.sin(Math.toRadians(theta)), Math.cos(Math.toRadians(theta)), 0.0, 0.0};
        double[] zRotate = {0.0, 0.0, 1.0, 0.0};
        double[] identity = {0.0, 0.0, 0.0, 1.0};
        addRow(xRotate);
        addRow(yRotate);
        addRow(zRotate);
        addRow(identity);
    }

    /**
     * Turn the calling matrix into a hermite coefficient generating matrix
     */
    public void makeHermite() {
        matrix = newMatrix(0,4);
        setMatrix(matrix);
        double xCoefficient[] = {2.0, -2.0, 1.0, 1.0};
        double yCoefficient[] = {-3.0, 3.0, -2.0, -1.0};
        double zCoefficient[] = {0.0, 0.0, 1.0, 0.0};
        double iCoefficient[] = {1.0, 0.0, 0.0, 0.0};
        addRow(xCoefficient);
        addRow(yCoefficient);
        addRow(zCoefficient);
        addRow(iCoefficient);
    }

    /**
     * Turns the calling matrix into a matrix that provides the coefficients
     * required to generate a Hermite curve given the values of the 4 parameter
     * coordinates
     */
    public void generateHermiteCoefficients(double p0, double r0, double p1, double r1) {
        Matrix parameters = new Matrix(0, 1);
        parameters.addRow(new double[] {p0});
        parameters.addRow(new double[] {p1});
        parameters.addRow(new double[] {r0});
        parameters.addRow(new double[] {r1});
        matrixMultiply(parameters);
    }

    /**
     * Turns the calling matrix into a bezier coefficient generating matrix
     */
    public void makeBezier() {
        matrix = newMatrix(0,4);
        setMatrix(matrix);
        double xCoefficient[] = {-1.0, 3.0, -3.0, 1.0};
        double yCoefficient[] = {3.0, -6.0, 3.0, 0};
        double zCoefficient[] = {-3.0, 3.0, 0.0, 0.0};
        double iCoefficient[] = {1.0, 0.0, 0.0, 0.0};
        addRow(xCoefficient);
        addRow(yCoefficient);
        addRow(zCoefficient);
        addRow(iCoefficient);
    }

    /**
     * Turns the calling matrix into a matrix that provides the coefficients
     * required to generate a Bezier curve given the values of the 4 parameter
     * coordinates
     */
    public void generateBezierCoefficients(double p0, double p1, double p2, double p3) {
        Matrix parameters = new Matrix(0, 1);
        parameters.addRow(new double[] {p0});
        parameters.addRow(new double[] {p1});
        parameters.addRow(new double[] {p2});
        parameters.addRow(new double[] {p3});
        matrixMultiply(parameters);
    }

}

class NonSquareMatrixException extends RuntimeException {}
class MatrixMultiplicationDimensionException extends RuntimeException {}
