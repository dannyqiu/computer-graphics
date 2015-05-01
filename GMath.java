import java.util.*;
import java.io.*;

public class GMath {

    /**
     * Returns the dot product given two equal length vectors
     * @param a vector to take the dot product with
     * @param b vector to take the dot product with
     * @return double representing the dot product of the two given vectors
     */
    public static double dotProduct(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new UnequalVectorLengthsException();
        }
        double sum = 0;
        for (int i=0; i<a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

    /**
     * Returns the vector cross product given two vectors with
     * length of 3 (for each axis - x, y, z)
     * @param a vector to take the cross product with
     * @param b vector to take the cross product with
     * @return double array representing the cross product of the two given vectors
     */
    public static double[] crossProduct(double[] a, double[] b) {
        if (a.length < 3 || b.length < 3) {
            throw new VectorLengthTooShortException();
        }
        double[] cross = new double[3];
        cross[0] = a[1]*b[2] - a[2]*b[1];
        cross[1] = a[2]*b[0] - a[0]*b[2];
        cross[2] = a[0]*b[1] - a[1]*b[0];
        return cross;
    }

    /**
     * Returns the magnitude of the given vector
     * @param a vector to get the magnitude of
     * @return double representing the magnitude of the vector
     */
    public static double getMagnitude(double[] a) {
        double sum = 0;
        for (int i=0; i<a.length; i++) {
            sum += a[i] * a[i];
        }
        return sum;
    }
}

class UnequalVectorLengthsException extends RuntimeException {}
class VectorLengthTooShortException extends RuntimeException {}
