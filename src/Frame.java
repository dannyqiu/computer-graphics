import java.util.*;
import java.io.*;

public class Frame {

    private static final int DEFAULT_DISPLAY_SIZE = 500;
    private Color[][] frame;
    int width;
    int height;
    Color frameColor;

    /**
     * Method to create the frame used for drawing
     * @param _width    width of the frame in pixels
     * @param _height   height of the frame in pixels
     * @param _color    background color of the frame
     */
    private void createFrame(int _width, int _height, Color _color) {
        width = _width;
        height = _height;
        frameColor = _color;
        frame = new Color[height][width];
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                frame[j][i] = frameColor;
            }
        }
    }

    public Frame() {
        createFrame(DEFAULT_DISPLAY_SIZE, DEFAULT_DISPLAY_SIZE, new Color());
    }

    public Frame(int width, int height) {
        createFrame(width, height, new Color());
    }

    public Frame(int width, int height, Color c) {
        createFrame(width, height, c);
    }

    /**
     * Plots a point on the frame given its coordinates. Origin is at the bottom left
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     */
    public void plot(int x, int y) {
        plot(x, y, new Color(255, 255, 255));
    }

    /**
     * Plots a point on the frame given its coordinates and color. Origin is at the bottom left
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @param c color of the point
     */
    public void plot(int x, int y, Color c) {
        int adjustedY = height - 1 - y;
        //System.out.println("Plotting... (" + x + ", " + y + ")");
        if (x >= 0 && x < width && adjustedY >= 0 && adjustedY < height) {
            frame[adjustedY][x] = c;
        }
    }

    /**
     * Plots a point on the frame given its coordinates. Origin is at the center
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     */
    public void plotCartesian(int x, int y) {
        plotCartesian(x, y, new Color(255, 255, 255));
    }

    /**
     * Plots a point on the frame given its coordinates and color. Origin is at the center
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @param c color of the point
     */
    public void plotCartesian(int x, int y, Color c) {
        int adjustedY = height/2 - y;
        //System.out.println("Plotting... (" + x + ", " + y + ")");
        if (x >= -width/2 && x < width/2 && adjustedY >= 0 && adjustedY < height) {
            frame[adjustedY][x+width/2] = c;
        }
    }

    /**
     * Goes through the given matrix and draws a line between every two
     * points using the given color. Origin is at the bottom left
     * @param matrix matrix containing the points to draw lines
     * @param c      color of the lines to be drawn
     */
    public void drawLines(Matrix matrix, Color c) {
        ArrayList<double[]> m = matrix.getMatrix();
        for (int i=0; i<matrix.getRows()-1; i+=2) { // Get every two points
            double[] p0 = m.get(i);
            double[] p1 = m.get(i+1);
            System.out.println("Drawing... " + Arrays.toString(p0) + " to " + Arrays.toString(p1));
            drawLine((int) p0[0], (int) p0[1], (int) p1[0], (int) p1[1], c);
        }
    }

    /**
     * Goes through the given matrix and draws a line between every two
     * points using the given color. Origin is at the center
     * @param matrix matrix containing the points to draw lines
     * @param c      color of the lines to be drawn
     */
    public void drawLinesCartesian(Matrix matrix, Color c) {
        ArrayList<double[]> m = matrix.getMatrix();
        for (int i=0; i<matrix.getRows()-1; i+=2) { // Get every two points
            double[] p0 = m.get(i);
            double[] p1 = m.get(i+1);
            System.out.println("Drawing Cartesian... " + Arrays.toString(p0) + " to " + Arrays.toString(p1));
            drawLineCartesian((int) p0[0], (int) p0[1], (int) p1[0], (int) p1[1], c);
        }
    }

    /**
     * Goes through the given matrix and interprets every set of 3 points as
     * the vertices of a triangle
     * @param matrix matrix containing the points to draw polygons
     * @param c      color of the polygons to be drawn
     */
    public void drawPolygons(Matrix matrix, Color c) {
        ArrayList<double[]> m = matrix.getMatrix();
        if (matrix.getRows() >= 3) {
            for (int i=0; i<matrix.getRows()-2; i+=3) {
                double[] p0 = m.get(i);
                double[] p1 = m.get(i+1);
                double[] p2 = m.get(i+2);
                if (isVisible(p0, p1, p2)) {
                    System.out.println("Drawing Polygon..." + Arrays.toString(p0) + " to " + Arrays.toString(p1) + " to " + Arrays.toString(p2));
                    drawLine((int) p0[0], (int) p0[1], (int) p1[0], (int) p1[1], c);
                    drawLine((int) p1[0], (int) p1[1], (int) p2[0], (int) p2[1], c);
                    drawLine((int) p2[0], (int) p2[1], (int) p0[0], (int) p0[1], c);
                }
            }
        }
    }

    /**
     * Returns true or false depending on whether the face is visible or not
     * when looking at the face from the front.
     * Note that the points given must be in counterclockwise order!
     * @param p0 first vertex
     * @param p1 second vertex
     * @param p2 third vertex
     */
    private boolean isVisible(double[] p0, double[] p1, double[] p2) {
        // v1 is the vector from p0 to p1
        double[] v1 = new double[] {p0[0] - p1[0], p0[1] - p1[1], p0[2] - p1[2]};
        // v2 is the vector from p0 to p2
        double[] v2 = new double[] {p0[0] - p2[0], p0[1] - p2[1], p0[2] - p2[2]};
        double[] surfaceNormal = GMath.crossProduct(v1, v2);
        double[] viewVector = new double[] {0, 0, -1};
        double cosAngle = GMath.dotProduct(surfaceNormal, viewVector) / (GMath.getMagnitude(surfaceNormal) * GMath.getMagnitude(viewVector));
        if (cosAngle < 0) { // Angle is from 90 to 270 degrees (viewable)
            return true;
        }
        else { // Angle is either less than 0 or greater than 270
            return false;
        }
    }

    /**
     * Draws a line between the points with given coordinates using the
     * given color. Origin is at the bottom left
     * @param x0 x-coordinate of the starting point
     * @param y0 y-coordinate of the starting point
     * @param x1 x-coordinate of the ending point
     * @param y1 y-coordinate of the ending point
     * @param c  color of the line to be drawn
     */
    public void drawLine(int x0, int y0, int x1, int y1, Color c) {
        plot(x0, y0, c);
        if (x0 > x1) { // Swap coordinates so our loop goes from left to right
            int temp;
            temp = x0;
            x0 = x1;
            x1 = temp;
            temp = y0;
            y0 = y1;
            y1 = temp;
        }
        double slope = (double) (y1 - y0) / (double) (x1 - x0);
        int x = x0;
        int y = y0;
        int A = 2 * (y1 - y0);
        int B = -2 * (x1 - x0);
        int d;
        if (slope > 1) { // Line is above diagonal in Quadrant I
            /**
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1/2, y0+1)
             *    = A(x0) + 1/2A + B(x0) + B + C
             *    = 0 + 1/2A + B
             */
            d = A/2 + B;
            while (y <= y1) {
                plot(x, y, c);
                if (d < 0) { // Point is to the right of the midpoint
                    x++;
                    d += A;
                }
                y++;
                d += B;
            }
        }
        else if (slope >= 0 && slope <= 1) { // Line is below diagonal in Quadrant I
            /**
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1, y0+1/2)
             *    = A(x0) + A + B(x0) + 1/2B + C
             *    = 0 + A + 1/2B
             */
            d = A + B/2;
            while (x <= x1) {
                plot(x, y, c);
                if (d > 0) { // Point is above the midpoint
                    y++;
                    d += B;
                }
                x++;
                d += A;
            }
        }
        else if (slope >= -1 && slope <= 0) { // Line is above the diagonal in Quadrant IV
            /**
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1, y0-1/2)
             *    = A(x0) + A + B(x0) - 1/2B + C
             *    = 0 + A - 1/2B
             */
            d = A - B/2;
            while (x <= x1) {
                plot(x, y, c);
                if (d < 0) { // Point is below the midpoint
                    y--;
                    d -= B;
                }
                x++;
                d += A;
            }
        }
        else if (slope < -1) { // Line is below the diagonal in Quadrant IV
            /**
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1/2, y0-1)
             *    = A(x0) + 1/2A + B(x0) - B + C
             *    = 0 + 1/2A - B
             */
            d = A/2 - B;
            while (y >= y1) {
                plot(x, y, c);
                if (d > 0) { // Point is to the right of the midpoint
                    x++;
                    d += A;
                }
                y--;
                d -= B;
            }
        }
    }

    /**
     * Draws a line between the points with given coordinates using the
     * given color. Origin is at the center
     * @param x0 x-coordinate of the starting point
     * @param y0 y-coordinate of the starting point
     * @param x1 x-coordinate of the ending point
     * @param y1 y-coordinate of the ending point
     * @param c  color of the line to be drawn
     */
    public void drawLineCartesian(int x0, int y0, int x1, int y1, Color c) {
        plotCartesian(x0, y0, c);
        if (x0 > x1) { // Swap coordinates so our loop goes from left to right
            int temp;
            temp = x0;
            x0 = x1;
            x1 = temp;
            temp = y0;
            y0 = y1;
            y1 = temp;
        }
        double slope = (double) (y1 - y0) / (double) (x1 - x0);
        int x = x0;
        int y = y0;
        int A = 2 * (y1 - y0);
        int B = -2 * (x1 - x0);
        int d;
        if (slope > 1) { // Line is above diagonal in Quadrant I
            /**
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1/2, y0+1)
             *    = A(x0) + 1/2A + B(x0) + B + C
             *    = 0 + 1/2A + B
             */
            d = A/2 + B;
            while (y <= y1) {
                plotCartesian(x, y, c);
                if (d < 0) { // Point is to the right of the midpoint
                    x++;
                    d += A;
                }
                y++;
                d += B;
            }
        }
        else if (slope >= 0 && slope <= 1) { // Line is below diagonal in Quadrant I
            /**
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1, y0+1/2)
             *    = A(x0) + A + B(x0) + 1/2B + C
             *    = 0 + A + 1/2B
             */
            d = A + B/2;
            while (x <= x1) {
                plotCartesian(x, y, c);
                if (d > 0) { // Point is above the midpoint
                    y++;
                    d += B;
                }
                x++;
                d += A;
            }
        }
        else if (slope >= -1 && slope <= 0) { // Line is above the diagonal in Quadrant IV
            /**
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1, y0-1/2)
             *    = A(x0) + A + B(x0) - 1/2B + C
             *    = 0 + A - 1/2B
             */
            d = A - B/2;
            while (x <= x1) {
                plotCartesian(x, y, c);
                if (d < 0) { // Point is below the midpoint
                    y--;
                    d -= B;
                }
                x++;
                d += A;
            }
        }
        else if (slope < -1) { // Line is below the diagonal in Quadrant IV
            /**
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1/2, y0-1)
             *    = A(x0) + 1/2A + B(x0) - B + C
             *    = 0 + 1/2A - B
             */
            d = A/2 - B;
            while (y >= y1) {
                plotCartesian(x, y, c);
                if (d > 0) { // Point is to the right of the midpoint
                    x++;
                    d += A;
                }
                y--;
                d -= B;
            }
        }
    }

    /**
     * Clears the frame and restores it back to original condition
     */
    public void clearFrame() {
        createFrame(width, height, frameColor);
    }

    /**
     * Pauses execution of the program and views the current frame
     */
    public void viewFrame() {
        String filename = "temp-" + System.currentTimeMillis() + ".ppm";
        savePpm(filename);
        try {
            ProcessBuilder pb = new ProcessBuilder("display", filename).inheritIO();
            Process p = pb.start();
            try {
                p.waitFor(); // Keeps frame in view
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            File file = new File(filename);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the frame into a PPM file which can be viewed later using a
     * program such as ImageMagick
     * @param filename  name of the file to save image to
     */
    public void savePpm(String filename) {
        String ppmHeader = "P3\n" + width + " " + height + "\n255\n";
        try {
            System.out.println("Saving image to " + filename);
            File file = new File(filename);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(ppmHeader);
            for (int j=0; j<height; j++) {
                for (int i=0; i<width; i++) {
                    writer.write(frame[j][i] + " ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the frame into an image based on the extension given. If no
     * extension is given, the output defaults to .png
     * @param filename  name of the file to save image to
     */
    public void saveImage(String filename) {
        String extension = ".ppm";
        if (filename.indexOf('.') != -1) {
            extension = filename.substring(filename.lastIndexOf('.'), filename.length());
            filename = filename.substring(0, filename.lastIndexOf('.'));
        }
        else {
            extension = ".png"; // Adds .png file extension if no file extension given
        }
        if (extension.equals(".ppm")) {
            savePpm(filename + ".ppm");
        }
        else {
            String ppmFile = filename + "_TEMP.ppm";
            savePpm(ppmFile);
            filename += extension;
            try {
                System.out.println("Converting " + ppmFile + " to " + filename);
                ProcessBuilder pb = new ProcessBuilder("convert", ppmFile, filename).inheritIO();
                Process p = pb.start(); //Runtime.getRuntime().exec("convert " + ppmFile + " " + filename);
                try {
                    p.waitFor(); // Have to wait for command to finish or else you won't get your .png files since the .ppm files will have been deleted
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                File file = new File(ppmFile);
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runs the ImageMagick `display` command to view the frame
     */
    public void display() {
        String filename = "output.ppm";
        savePpm(filename);
        try {
            Runtime.getRuntime().exec("display " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
