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
     * Generates the edges required to make the given circle and adds them to
     * the EdgeMatrix.
     * @param centerX  x-coordinate for the center
     * @param centerY  y-coordinate for the center
     * @param r        radius of the circle
     */
    public void addCircle(double centerX, double centerY, double r) {
        addCircle(centerX, centerY, r, 1.0/5000);
    }

    /**
     * Generates the edges required to make the given circle and adds them to
     * the EdgeMatrix. The step size can be adjusted for a smoother circle
     * @param centerX  x-coordinate for the center
     * @param centerY  y-coordinate for the center
     * @param r        radius of the circle
     * @param stepSize size of the step to take
     */
    public void addCircle(double centerX, double centerY, double r, double stepSize) {
        double x0 = centerX + r;
        double y0 = centerY;
        double x1, y1;
        double t;
        for (t=0; t<1; t+=stepSize) {
            x1 = centerX + Math.cos(2 * Math.PI * t) * r;
            y1 = centerY + Math.sin(2 * Math.PI * t) * r;
            addEdge(x0, y0, 0, x1, y1, 0);
            x0 = x1;
            y0 = y1;
        }
    }

    public static enum CurveType {
        HERMITE, BEZIER
    }

    /**
     * Generates the edges required to create a curve and adds them to the
     * edge matrix
     * @param type enum for the type of curve
     */
    public void addCurve(double x0, double y0, double x1, double y1,
                         double x2, double y2, double x3, double y3, CurveType type) {
        switch (type) {
            case HERMITE:
                addHermiteCurve(x0, y0, x1-x0, y1-y0,
                                x2, y2, x3-x2, y3-y2, 1.0/5000);
                break;
            case BEZIER:
                addBezierCurve(x0, y0, x1, y1, x2, y2, x3, y3, 1.0/5000);
                break;
        }
    }

    /**
     * Generates the edges required to create a hermite curve and adds them
     * to the edge matrix
     * @param x0 the starting point x-coordinate
     * @param y0 the starting point y-coordinate
     * @param dx0 the x rate of change at the starting point
     * @param dy0 the y rate of change at the starting point
     * @param x1 the final point x-coordinate
     * @param y1 the final point y-coordinate
     * @param dx1 the x rate of change at the final point
     * @param dy1 the y rate of change at the final point
     */
    public void addHermiteCurve(double x0, double y0, double dx0, double dy0,
                                double x1, double y1, double dx1, double dy1, double stepSize) {
        EdgeMatrix coefficientsX = new EdgeMatrix();
        coefficientsX.makeHermite();
        coefficientsX.generateHermiteCoefficients(x0, dx0, x1, dx1);
        double cXA = coefficientsX.get(0, 0);
        double cXB = coefficientsX.get(1, 0);
        double cXC = coefficientsX.get(2, 0);
        double cXD = coefficientsX.get(3, 0);
        EdgeMatrix coefficientsY = new EdgeMatrix();
        coefficientsY.makeHermite();
        coefficientsY.generateHermiteCoefficients(y0, dy0, y1, dy1);
        double cYA = coefficientsY.get(0, 0);
        double cYB = coefficientsY.get(1, 0);
        double cYC = coefficientsY.get(2, 0);
        double cYD = coefficientsY.get(3, 0);
        double prevX = x0;
        double prevY = y0;
        double x, y;
        double t;
        for (t=0; t<1; t+=stepSize) {
            double tSquared = t * t;
            double tCubed = t * tSquared;
            x = cXA * tCubed + cXB * tSquared + cXC * t + cXD;
            y = cYA * tCubed + cYB * tSquared + cYC * t + cYD;
            addEdge(prevX, prevY, 0, x, y , 0);
            prevX = x;
            prevY = y;
        }
    }

    /**
     * Generates the edges required to create a bezier curve and adds them
     * to the edge matrix
     * @param x0 the starting point x-coordinate
     * @param y0 the starting point y-coordinate
     * @param x1 the first point of influence x-coordinate
     * @param y1 the first point of influence y-coordinate
     * @param x2 the second point of influence x-coordinate
     * @param y2 the second point of influence y-coordinate
     * @param x3 the final point x-coordinate
     * @param y3 the final point y-coordinate
     */
    public void addBezierCurve(double x0, double y0, double x1, double y1,
                               double x2, double y2, double x3, double y3, double stepSize) {
        EdgeMatrix coefficientsX = new EdgeMatrix();
        coefficientsX.makeBezier();
        coefficientsX.generateBezierCoefficients(x0, x1, x2, x3);
        double cXA = coefficientsX.get(0, 0);
        double cXB = coefficientsX.get(1, 0);
        double cXC = coefficientsX.get(2, 0);
        double cXD = coefficientsX.get(3, 0);
        EdgeMatrix coefficientsY = new EdgeMatrix();
        coefficientsY.makeBezier();
        coefficientsY.generateBezierCoefficients(y0, y1, y2, y3);
        double cYA = coefficientsY.get(0, 0);
        double cYB = coefficientsY.get(1, 0);
        double cYC = coefficientsY.get(2, 0);
        double cYD = coefficientsY.get(3, 0);
        double prevX = x0;
        double prevY = y0;
        double x, y;
        double t;
        for (t=0; t<1; t+=stepSize) {
            double tSquared = t * t;
            double tCubed = t * tSquared;
            x = cXA * tCubed + cXB * tSquared + cXC * t + cXD;
            y = cYA * tCubed + cYB * tSquared + cYC * t + cYD;
            addEdge(prevX, prevY, 0, x, y , 0);
            prevX = x;
            prevY = y;
        }
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
     * Turns the caling matrix into a bezier coefficient generating matrix
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
