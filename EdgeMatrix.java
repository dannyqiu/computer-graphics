import java.util.*;
import java.io.*;

public class EdgeMatrix extends Matrix {

    private static double STEP_SIZE = 1.0/1000;
    private static double CIRCULAR_STEP_SIZE = 1.0/20;

    public EdgeMatrix() {
        super(0, 4);
    }

    public EdgeMatrix(int _rows, int _cols) {
        super(_rows, _cols);
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
     * Adds the vertices (x0, y0, z0), (x1, y1, z1), and (x2, y2, z2) to the
     * polygon matrix. They define a single triangular surface
     */
    public void addPolygon(double x0, double y0, double z0,
                           double x1, double y1, double z1,
                           double x2, double y2, double z2) {
        addPoint(x0, y0, z0);
        addPoint(x1, y1, z1);
        addPoint(x2, y2, z2);
    }

    /**
     * Generates the edges required to make the given circle and adds them to
     * the EdgeMatrix.
     * @param centerX  x-coordinate for the center
     * @param centerY  y-coordinate for the center
     * @param r        radius of the circle
     */
    public void addCircle(double centerX, double centerY, double r) {
        addCircle(centerX, centerY, r, 1.0/(r*50));
    }

    /**
     * Generates the edges required to make the given circle and adds them to
     * the EdgeMatrix. The step size can be adjusted for a smoother circle
     * @param centerX  x-coordinate for the center
     * @param centerY  y-coordinate for the center
     * @param r        radius of the circle
     * @param stepSize the size of the step to take
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
                                x2, y2, x3-x2, y3-y2, STEP_SIZE);
                break;
            case BEZIER:
                addBezierCurve(x0, y0, x1, y1, x2, y2, x3, y3, STEP_SIZE);
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
     * @param stepSize the size of the step to take
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
     * @param stepSize the size of the step to take
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
     * Adds the points for a rectangular prism to the edge matrix given
     * coordinates that specify the upper-left-front corner of the prism
     * and its dimensions
     * @param x      x-coordinate of upper-left-front corner
     * @param y      y-coordinate of upper-left-front corner
     * @param z      z-coordinate of upper-left-front corner
     * @param width  width of the rectangular prism
     * @param height height of the rectangular prism
     * @param depth  depth of the rectangular prism
     */
    public void addPrism(double x, double y, double z,
                         double width, double height, double depth) {
        double x1 = x + width;
        double y1 = y + height;
        double z1 = z - depth;
        // Front side
        addPolygon(x, y1, z,
                   x, y, z,
                   x1, y, z);
        addPolygon(x, y1, z,
                   x1, y, z,
                   x1, y1, z);
        // Back side
        addPolygon(x1, y1, z1,
                   x1, y, z1,
                   x, y, z1);
        addPolygon(x1, y1, z1,
                   x, y, z1,
                   x, y1, z1);
        // Right side
        addPolygon(x1, y1, z,
                   x1, y, z,
                   x1, y, z1);
        addPolygon(x1, y1, z,
                   x1, y, z1,
                   x1, y1, z1);
        // Left side
        addPolygon(x, y1, z1,
                   x, y, z1,
                   x, y, z);
        addPolygon(x, y1, z1,
                   x, y, z,
                   x, y1, z);
        // Top side
        addPolygon(x, y1, z1,
                   x, y1, z,
                   x1, y1, z);
        addPolygon(x, y1, z1,
                   x1, y1, z,
                   x1, y1, z1);
        // Back side
        addPolygon(x1, y, z1,
                   x1, y, z,
                   x, y, z);
        addPolygon(x1, y, z1,
                   x, y, z,
                   x, y, z1);
    }

    /**
     * Adds a sphere to the edge matrix given coordinates that specify
     * the center its radius
     * @param cx x-coordinate of the center
     * @param cy y-coordinate of the center
     * @param cz z-coordinate of the center
     * @param r radius of the sphere
     * @param stepSize the size of the step to take
     */
    public void addSphere(double cx, double cy, double cz, double r) {
        EdgeMatrix points = new EdgeMatrix();
        int index;
        int numSteps = (int) (1 / CIRCULAR_STEP_SIZE) + 1;
        int latStop = numSteps;
        int longStop = numSteps;
        points.generateSphere(cx, cy, cz, r, CIRCULAR_STEP_SIZE);
        for (int lat=0; lat<latStop; lat++) {
            for (int longt=0; longt<longStop; longt++) {
                index = lat * numSteps + longt;
                addEdge(points.get(index, 0), points.get(index, 1), points.get(index, 2),
                        points.get(index, 0), points.get(index, 1), points.get(index, 2));
            }
        }
    }

    /**
     * Generates a sphere's points in the edge matrix given coordinates
     * that specify the center and the radius
     * @param cx x-coordinate of the center
     * @param cy y-coordinate of the center
     * @param cz z-coordinate of the center
     * @param r radius of the sphere
     * @param stepSize the size of the step to take
     */
    public void generateSphere(double cx, double cy, double cz, double r, double stepSize) {
        double x, y, z;
        for (double rotation=0; rotation<1+stepSize; rotation+=stepSize) { // theta
            for (double circle=0; circle<1+stepSize; circle+=stepSize) { // phi
                x = cx + r * Math.cos(Math.PI * circle);
                y = cy + r * Math.sin(Math.PI * circle) * Math.cos(2 * Math.PI * rotation);
                z = cz + r * Math.sin(Math.PI * circle) * Math.sin(2 * Math.PI * rotation);
                addPoint(x, y, z);
            }
        }
    }

    /**
     * Adds a torus to the edge matrix given coordinates that specify
     * the center, the circle radius, and the torus radius
     * @param cx x-coordinate of the center
     * @param cy y-coordinate of the center
     * @param cz z-coordinate of the center
     * @param circleRadius radius of the circle being rotated
     * @param torusRadius  radius of the rotation of circle
     */
    public void addTorus(double cx, double cy, double cz,
                         double circleRadius, double torusRadius) {
        EdgeMatrix points = new EdgeMatrix();
        int index;
        int numSteps = (int) (1 / CIRCULAR_STEP_SIZE) + 1;
        int latStop = numSteps;
        int longStop = numSteps;
        points.generateTorus(cx, cy, cz, circleRadius, torusRadius, CIRCULAR_STEP_SIZE);
        for (int lat=0; lat<latStop; lat++) {
            for (int longt=0; longt<longStop; longt++) {
                index = lat * numSteps + longt;
                addEdge(points.get(index, 0), points.get(index, 1), points.get(index, 2),
                        points.get(index, 0), points.get(index, 1), points.get(index, 2));
            }
        }
    }

    /**
     * Generate's a torus's points in the edge matrix given coordinates
     * that specify the center, the circle radius, and the torus radius
     * @param cx x-coordinate of the center
     * @param cy y-coordinate of the center
     * @param cz z-coordinate of the center
     * @param circleRadius radius of the circle being rotated
     * @param torusRadius  radius of the rotation of circle
     * @param stepSize the size of the step to take
     */
    public void generateTorus(double cx, double cy, double cz,
                              double circleRadius, double torusRadius, double stepSize) {
        double x, y, z;
        for (double rotation=0; rotation<1+stepSize; rotation+=stepSize) { // theta
            for (double circle=0; circle<1+stepSize; circle+=stepSize) { //phi
                x = cx + Math.cos(2 * Math.PI * rotation) * (circleRadius * Math.cos(2 * Math.PI * circle) + torusRadius);
                y = cy + Math.sin(2 * Math.PI * circle) * circleRadius;
                z = cz + Math.sin(2 * Math.PI * rotation) * (circleRadius * Math.cos(2 * Math.PI * circle) + torusRadius);
                addPoint(x, y, z);
            }
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
