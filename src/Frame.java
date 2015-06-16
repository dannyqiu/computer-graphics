import java.util.*;
import java.io.*;

public class Frame {

    private static final int DEFAULT_DISPLAY_SIZE = 500;
    private Color[][] frame;
    private double[][] zBuffer;
    int width;
    int height;
    Color DEFAULT_COLOR = new Color(0, 0, 0);
    Color frameColor;

    /**
     * Method to create the frame used for drawing
     * @param _width width of the frame in pixels
     * @param _height height of the frame in pixels
     * @param _color background color of the frame
     */
    private void createFrame(int _width, int _height, Color _color) {
        width = _width;
        height = _height;
        frameColor = _color;
        frame = new Color[height][width];
        zBuffer = new double[height][width];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                frame[j][i] = frameColor;
                zBuffer[j][i] = Double.NEGATIVE_INFINITY;
            }
        }
    }

    public Frame() {
        createFrame(DEFAULT_DISPLAY_SIZE, DEFAULT_DISPLAY_SIZE, DEFAULT_COLOR);
    }

    public Frame(int width, int height) {
        createFrame(width, height, DEFAULT_COLOR);
    }

    public Frame(int width, int height, Color c) {
        createFrame(width, height, c);
    }

    /**
     * Plots a point on the frame given its coordinates. Origin is at the bottom
     * left
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @param z z-coordinate of point
     */
    public void plot(int x, int y, double z) {
        plot(x, y, z, new Color(255, 255, 255));
    }

    /**
     * Plots a point on the frame given its coordinates and color. Origin is at
     * the bottom left
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @param z0 z-coordinate of point
     * @param c color of the point
     */
    public void plot(int x, int y, double z0, Color c) {
        int adjustedY = height - 1 - y;
        //System.out.println("Plotting... (" + x + ", " + y + ")");
        if (x >= 0 && x < width && adjustedY >= 0 && adjustedY < height) {
            //System.out.println(z0 + " " + zBuffer[adjustedY][x] + " " + (z0 > zBuffer[adjustedY][x]));
            if (z0 > zBuffer[adjustedY][x]) {
                zBuffer[adjustedY][x] = z0;
                frame[adjustedY][x] = c;
            }
        }
    }

    /**
     * Goes through the given matrix and draws a line between every two
     * points using the given color. Origin is at the bottom left
     * @param matrix matrix containing the points to draw lines
     * @param c color of the lines to be drawn
     */
    public void drawLines(Matrix matrix, Color c) {
        ArrayList<double[]> m = matrix.getMatrix();
        for (int i = 0; i < matrix.getRows() - 1; i += 2) { // Get every two points
            double[] p0 = m.get(i);
            double[] p1 = m.get(i + 1);
            //System.out.println("Drawing... " + Arrays.toString(p0) + " to " + Arrays.toString(p1));
            drawLine((int) p0[0], (int) p0[1], p0[2], (int) p1[0], (int) p1[1], p1[2], c);
        }
    }

    /**
     * Goes through the given matrix and interprets every set of 3 points as
     * the vertices of a triangle
     * @param matrix matrix containing the points to draw polygons
     * @param c color of the polygons to be drawn
     */
    public void drawPolygons(Matrix matrix, Color c) {
        ArrayList<double[]> m = matrix.getMatrix();
        if (matrix.getRows() >= 3) {
            for (int i = 0; i < matrix.getRows() - 2; i += 3) {
                double[] p0 = m.get(i);
                double[] p1 = m.get(i + 1);
                double[] p2 = m.get(i + 2);
                if (isVisible(p0, p1, p2)) {
                    //System.out.println("Drawing Polygon..." + Arrays.toString(p0) + " to " + Arrays.toString(p1) + " to " + Arrays.toString(p2));

                    /* Ambient Light Test
                    double [] IA = {120, 140, 160};
                    double [] KA = {.56, .16, .78};
                    double [] light = flatAmbientLight(IA, KA);

                    c.setRed((int)light[0]);
                    c.setGreen((int)light[1]);
                    c.setBlue((int)light[2]);
                    */

                    /* Flat Diffuse Light Test*/
                    double[] Id = { 120, 140, 160 };
                    double[] Kd = { .001, .002, .003 };
                    double[] l = { 400, 200, 300 };
                    double[] light = flatDiffuseLight(p0, p1, p2, Id, Kd, l);

                    if (light[0] >= 255) {
                        c.setRed(255);
                    }
                    else {
                        c.setRed((int) light[0]);
                    }
                    if (light[1] >= 255) {
                        c.setGreen(255);
                    }
                    else {
                        c.setGreen((int) light[1]);
                    }
                    if (light[2] >= 255) {
                        c.setBlue(255);
                    }
                    else {
                        c.setBlue((int) light[2]);
                    }

                    //System.out.println(c);
                    drawLine((int) p0[0], (int) p0[1], p0[2], (int) p1[0], (int) p1[1], p1[2], c);
                    drawLine((int) p1[0], (int) p1[1], p1[2], (int) p2[0], (int) p2[1], p2[2], c);
                    drawLine((int) p2[0], (int) p2[1], p2[2], (int) p0[0], (int) p0[1], p0[2], c);
                    scanlineConvert(p0, p1, p2, c);
                }
            }
        }
    }

    /**
     * Shades a polygon given the three vertices using the horizontal scanline
     * algorithm going from the bottom to the top.
     * @param p0 coordinate of one vertex of the polygon
     * @param p1 coordinate of one vertex of the polygon
     * @param p2 coordinate of one vertex of the polygon
     * @param c color of shading on the polygons
     */
    private void scanlineConvert(double[] p0, double[] p1, double[] p2, Color c) {
        double[] temp;
        // This is to define p0 as the lowest point and p2 as the highest point
        if (p1[1] > p2[1]) {
            temp = p1;
            p1 = p2;
            p2 = temp;
        }
        if (p0[1] > p1[1]) {
            if (p0[1] > p2[1]) {
                temp = p0;
                p0 = p1;
                p1 = p2;
                p2 = temp;
            }
            else {
                temp = p0;
                p0 = p1;
                p1 = temp;
            }
        }
        double x0 = p0[0], x1 = x0;
        int y = (int) p0[1];
        double z0 = p0[2], z1 = z0;
        double dx0 = (p2[0] - p0[0]) / ((int) p2[1] - (int) p0[1]);
        double dz0 = (p2[2] - p0[2]) / ((int) p2[1] - (int) p0[1]);
        // Draws the bottom half of the polygon
        double dx1 = (p1[0] - p0[0]) / ((int) p1[1] - (int) p0[1]);
        double dz1 = (p1[2] - p0[2]) / ((int) p1[1] - (int) p0[1]);
        int midY = (int) p1[1];
        while (y < midY) {
            x0 += dx0;
            x1 += dx1;
            y++;
            z0 += dz0;
            z1 += dz1;
            drawLine((int) x0, y, z0, (int) x1, y, z1, c);
        }
        x1 = p1[0]; // Sets the start of the top half's end to the x-coor of
                    // the middle point. This fixes a bug when the middle and
                    // bottom points have the same y-coor
        z1 = p1[2];
        // Draws the top half of the polygon
        dx1 = (p2[0] - p1[0]) / ((int) p2[1] - (int) p1[1]);
        int topY = (int) p2[1];
        while (y < topY) {
            x0 += dx0;
            x1 += dx1;
            y++;
            drawLine((int) x0, y, z0, (int) x1, y, z1, c);
        }
    }

    /**
     * Flat Ambient Lighting given by the equation I = I_a*K_a
     * @param I_a intensity of the ambient light
     * @param K_a ambient constant
     * @return ambient lighting values
     **/
    private double[] flatAmbientLight(double[] I_a, double[] K_a) {
        double[] ambient = new double[3];
        ambient[0] = I_a[0] * K_a[0];
        ambient[1] = I_a[1] * K_a[1];
        ambient[2] = I_a[2] * K_a[2];
        return ambient;
    }

    /**
     * Diffuse Lighting given be the equation, I_d = I_i*K_d(L•N), where L is
     * the light vector and N is the surface normal
     * @param p0 first vertex of the polygon, going clockwise
     * @param p1 second vertex of the polygon, going clockwise
     * @param p2 third vertex of the polygon, going clockwise
     * @param I_i intensity of the diffuse light
     * @param K_d diffuse constant
     * @param light vector corresponding to the light source
     * @return diffuse lighting values
     **/
    private double[] flatDiffuseLight(double[] p0, double[] p1, double[] p2, double[] I_i,
            double[] K_d, double[] light) {
        // Getting the normal of the surface
        double[] v1 = new double[] { p0[0] - p1[0], p0[1] - p1[1], p0[2] - p1[2] };
        double[] v2 = new double[] { p0[0] - p2[0], p0[1] - p2[1], p0[2] - p2[2] };
        double[] surfaceNormal = GMath.crossProduct(v1, v2);

        double normMag = GMath.getMagnitude(surfaceNormal);
        double normLight = GMath.getMagnitude(light);
        double[] normVector = new double[3];
        normVector[0] = surfaceNormal[0] / (normMag * normLight);
        normVector[1] = surfaceNormal[1] / (normMag * normLight);
        normVector[2] = surfaceNormal[2] / (normMag * normLight);

        double diffuseVector = GMath.dotProduct(light, normVector);

        double[] diffuse = new double[3];
        diffuse[0] = I_i[0] * K_d[0] * diffuseVector;
        diffuse[1] = I_i[1] * K_d[1] * diffuseVector;
        diffuse[2] = I_i[2] * K_d[2] * diffuseVector;

        //System.out.println(Arrays.toString(diffuse));
        return diffuse;
    }

    /**
     * Specular Lighting given by the equation I_i*K_s[(2N*(N•L)-L)•V]^n where L
     * is
     * the light vector, N is the surface normal, and V is the viewer vector.
     * @param p0 first vertex of the polygon, going clockwise
     * @param p1 second vertex of the polygon, going clockwise
     * @param p2 third vertex of the polygon, going clockwise
     * @param I_i intensity of the light source
     * @param K_s specular constant
     * @param light vector corresponding to the light source
     * @param viewer vector corresponding to the viewer
     * @return specular lighting values
     **/
    private double[] flatSpecularLight(double[] p0, double[] p1, double[] p2, double[] I_i,
            double[] K_s, double[] light, double[] viewer) {
        // Getting the normal of the surface
        double[] v1 = new double[] { p0[0] - p1[0], p0[1] - p1[1], p0[2] - p1[2] };
        double[] v2 = new double[] { p0[0] - p2[0], p0[1] - p2[1], p0[2] - p2[2] };
        double[] surfaceNormal = GMath.crossProduct(v1, v2);

        double n = 5; // This is a variable constant. A perfect reflector has n=Infinity

        double dot = GMath.dotProduct(surfaceNormal, light);
        double[] reflectVector = GMath.subtract(GMath.scale(surfaceNormal, dot * 2), light);
        double specularVector = GMath.dotProduct(reflectVector, viewer);
        specularVector = Math.pow(specularVector, n);

        double[] specular = new double[3];
        specular[0] = I_i[0] * K_s[0] * specularVector;
        specular[1] = I_i[1] * K_s[1] * specularVector;
        specular[2] = I_i[2] * K_s[2] * specularVector;

        //System.out.println(Arrays.toString(specular));
        return specular;
    }

    /**
     * Combination of the three lighting types (ambient, diffuse, specular) to
     * form the Standard Computer Graphics Lighting Equation
     * @param p0 first vertex of the polygon, going clockwise
     * @param p1 second vertex of the polygon, going clockwise
     * @param p2 third vertex of the polygon, going clockwise
     * @param I_a intensity of the ambient light
     * @param K_a ambient constant
     * @param I_i intensity of the light source
     * @param K_d diffuse constant
     * @param K_s specular constant
     * @param light vector corresponding to the light source
     * @param viewer vector corresponding to the viewer
     * @return combined lighting values
     */
    private double[] flatShading(double[] p0, double[] p1, double[] p2, double[] I_a, double[] K_a,
            double[] I_i, double[] K_d, double[] K_s, double[] light, double[] viewer) {
        double[] ambient = flatAmbientLight(I_a, K_a);
        double[] diffuse = flatDiffuseLight(p0, p1, p2, I_i, K_d, light);
        double[] specular = flatSpecularLight(p0, p1, p2, I_i, K_s, light, viewer);

        //I = Ia + Id + Is as long as they are less 255
        double[] I = new double[3];
        I[0] = ambient[0] + diffuse[0] + specular[0];
        I[1] = ambient[1] + diffuse[1] + specular[1];
        I[2] = ambient[2] + diffuse[2] + specular[2];

        return I;
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
        double[] v1 = new double[] { p0[0] - p1[0], p0[1] - p1[1], p0[2] - p1[2] };
        // v2 is the vector from p0 to p2
        double[] v2 = new double[] { p0[0] - p2[0], p0[1] - p2[1], p0[2] - p2[2] };
        double[] surfaceNormal = GMath.crossProduct(v1, v2);
        double[] viewVector = new double[] { 0, 0, -1 };
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
     * @param z0 z-coordinate of the starting point
     * @param x1 x-coordinate of the ending point
     * @param y1 y-coordinate of the ending point
     * @param z1 z-coordinate of the ending point
     * @param c color of the line to be drawn
     */
    public void drawLine(int x0, int y0, double z0, int x1, int y1, double z1, Color c) {
        plot(x0, y0, z0, c);
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
        double dz;
        int x = x0;
        int y = y0;
        double z = z0;
        int A = 2 * (y1 - y0);
        int B = -2 * (x1 - x0);
        int d;
        if (slope > 1) { // Line is above diagonal in Quadrant I
            /** @formatter:off
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1/2, y0+1)
             *    = A(x0) + 1/2A + B(x0) + B + C
             *    = 0 + 1/2A + B
             */
            dz = (double) (z1 - z0) / (double) (y1 - y0);
            d = A / 2 + B;
            while (y <= y1) {
                plot(x, y, z, c);
                if (d < 0) { // Point is to the right of the midpoint
                    x++;
                    d += A;
                }
                y++;
                d += B;
                z += dz;
            }
        }
        else if (slope >= 0 && slope <= 1) { // Line is below diagonal in Quadrant I
            /** @formatter:off
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1, y0+1/2)
             *    = A(x0) + A + B(x0) + 1/2B + C
             *    = 0 + A + 1/2B
             */
            dz = (double) (z1 - z0) / (double) (x1 - x0);
            d = A + B / 2;
            while (x <= x1) {
                plot(x, y, z, c);
                if (d > 0) { // Point is above the midpoint
                    y++;
                    d += B;
                }
                x++;
                d += A;
                z += dz;
            }
        }
        else if (slope >= -1 && slope <= 0) { // Line is above the diagonal in Quadrant IV
            /** @formatter:off
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1, y0-1/2)
             *    = A(x0) + A + B(x0) - 1/2B + C
             *    = 0 + A - 1/2B
             */
            dz = (double) (z1 - z0) / (double) (x1 - x0);
            d = A - B / 2;
            while (x <= x1) {
                plot(x, y, z, c);
                if (d < 0) { // Point is below the midpoint
                    y--;
                    d -= B;
                }
                x++;
                d += A;
                z += dz;
            }
        }
        else if (slope < -1) { // Line is below the diagonal in Quadrant IV
            /** @formatter:off
             * d0 = f(x0, y0) = A(x0) + B(x0) + C = 0
             * d1 = f(x0+1/2, y0-1)
             *    = A(x0) + 1/2A + B(x0) - B + C
             *    = 0 + 1/2A - B
             */
            dz = (double) (z1 - z0) / (double) (y1 - y0);
            d = A / 2 - B;
            while (y >= y1) {
                plot(x, y, z, c);
                if (d > 0) { // Point is to the right of the midpoint
                    x++;
                    d += A;
                }
                y--;
                d -= B;
                z += dz;
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the frame into a PPM file which can be viewed later using a
     * program such as ImageMagick
     * @param filename name of the file to save image to
     */
    public void savePpm(String filename) {
        String ppmHeader = "P3\n" + width + " " + height + "\n255\n";
        try {
            System.out.println("Saving image to " + filename);
            File file = new File(filename);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(ppmHeader);
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    writer.write(frame[j][i] + " ");
                }
                writer.write("\n");
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the frame into an image based on the extension given. If no
     * extension is given, the output defaults to .png
     * @param filename name of the file to save image to
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
            }
            catch (IOException e) {
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
            Process p = Runtime.getRuntime().exec("display " + filename);
            p.waitFor();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
