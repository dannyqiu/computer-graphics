import java.util.*;
import java.io.*;

public class EdgeMatrix extends Matrix {

    public EdgeMatrix() {
        super(0, 4);
    }

    public EdgeMatrix(int _rows, int _cols) {
        super(_rows, _cols);
    }

    /**
     * Adds an edge to the matrix given x, y, z coordinates for the two points
     * that define the edge line
     * @param x0 x-coordinate of starting point 
     * @param y0 y-coordinate of starting point 
     * @param z0 z-coordinate of starting point 
     * @param x1 x-coordinate of ending point 
     * @param y1 y-coordinate of ending point 
     * @param z1 z-coordinate of ending point 
     */
    public void addEdge(double x0, double y0, double z0,
                        double x1, double y1, double z1) {
        addPoint(x0, y0, z0);
        addPoint(x1, y1, z1);
    }

    /**
     * Adds a point to the matrix given x, y, z coordinates
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @param z z-coordinate of point
     */
    public void addPoint(double x, double y, double z) {
        double[] p = {x, y, z, 1.0};
        super.addRow(p);
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

}
