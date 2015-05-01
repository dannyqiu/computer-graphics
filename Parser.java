/*========== Parser.java ==========

Goes through a file and performs all of the actions listed.
The file follows the following format:
    Every command is a single character that takes up a line
    Any command that requires arguments must have those arguments in the second line.
    The commands are as follows:
        f: initialize the frame to the specified size
            takes 2 arguments (width, height)
        l: add a line to the edge matrix
            takes 6 arguemnts (x0, y0, z0, x1, y1, z1)
        h: add a hermite curve to the edge matrix
            takes 8 arguments (x0, y0, x1, y1, x2, y2, x3, y3)
        b: add a bezier curve to the edge matrix
            takes 8 arguments (x0, y0, x1, y1, x2, y2, x3, y3)
        c: add a circle to the edge matrix
            takes 3 arguments (cx, cy, r)
        p: adds a rectangular prism (box) to the edge matrix
            takes 6 parameters (x, y, z, width, height, depth)
        m: adds a sphere (munchkin) to the edge matrix
            takes 4 parameters (x, y, z, radius)
        d: adds a torus (doughnut) to the edge matrix
            takes 5 parameters (x, y, z, radius1, radius2)
                radius1 is the radius of the circle that makes up the torus
                radius2 is the full radius of the torus (the translation factor)
        i: set the transform matrix to the identity matrix
        s: create a scale matrix, then multiply the transform matrix by the scale matrix
            takes 3 arguments (sx, sy, sz)
        t: create a translation matrix,then multiply the transform matrix by the translation matrix
            takes 3 arguments (tx, ty, tz)
        x: create an x-axis rotation matrix, then multiply the transform matrix by the rotation matrix
            takes 1 argument (theta)
        y: create an y-axis rotation matrix, then multiply the transform matrix by the rotation matrix
            takes 1 argument (theta)
        z: create an z-axis rotation matrix, then multiply the transform matrix by the rotation matrix
            takes 1 argument (theta)
        a: apply the current transformation matrix to the edge matrix
        j: sets the drawing mode to line drawing (default)
        k: sets the drawing mode to polygon drawing
        v: views the current frame
        g: draw the lines of the edge matrix to the frame save the frame to a file
            takes 1 argument (file name)
        w: clears the edge matrix of all points
        q: end parsing

=================================*/

import java.io.*;
import java.util.*;

enum DrawingMode {
    LINE, POLYGON
}

public class Parser {

    private EdgeMatrix tfm; // Master transform matrix
    private EdgeMatrix tfmTemp; // Temporary transform matrix to multiply to the master
    private EdgeMatrix em; // Master edge matrix
    private Frame frame; // Frame used for drawing and saving
    DrawingMode drawingMode = DrawingMode.LINE; // Mode of drawing (line or polygon)

    int lineNumber; // Current line number when parsing the file
    Random r = new Random();

    public Parser() {
        frame = new Frame();
        tfm = new EdgeMatrix(4, 4);
        tfmTemp = new EdgeMatrix(4, 4);
        em = new EdgeMatrix();
        tfm.makeIdentity();
        lineNumber = 0;
    }

    /**
     * Goes through the input stream referred to by in, scans it for the commands
     * as shown in the guide above, and performs the required commands
     * @param in BufferedReader referenced to the opened file
     */
    public void parseFile(BufferedReader in) {
        String line = getNextLine(in);
        double[] args;
        boolean done = false;
        try {
            while (line != null && !done) {
                lineNumber++;
                if (line.equals("")) {
                    line = getNextLine(in);
                    continue;
                }
                switch (line.charAt(0)) {
                    case '#': // Skips the comment
                        break;
                    case 'f':
                        args = parseArgs(getNextLine(in));
                        frame = new Frame((int) args[0], (int) args[1]);
                        break;
                    case 'l':
                        args = parseArgs(getNextLine(in));
                        em.addEdge(args[0], args[1], args[2], args[3], args[4], args[5]);
                        break;
                    case 'c':
                        args = parseArgs(getNextLine(in));
                        em.addCircle(args[0], args[1], args[2]);
                        break;
                    case 'h':
                        args = parseArgs(getNextLine(in));
                        em.addCurve(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], EdgeMatrix.CurveType.HERMITE);
                        break;
                    case 'b':
                        args = parseArgs(getNextLine(in));
                        em.addCurve(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], EdgeMatrix.CurveType.BEZIER);
                        break;
                    case 'p':
                        args = parseArgs(getNextLine(in));
                        em.addPrism(args[0], args[1], args[2], args[3], args[4], args[5]);
                        break;
                    case 'm':
                        args = parseArgs(getNextLine(in));
                        if (args.length == 3) { // z coordinate is not specified
                            em.addSphere(args[0], args[1], 0, args[2]);
                        }
                        else {
                            em.addSphere(args[0], args[1], args[2], args[3]);
                        }
                        break;
                    case 'd':
                        args = parseArgs(getNextLine(in));
                        if (args.length == 4) { // z coordinate is not specified
                            em.addTorus(args[0], args[1], 0, args[2], args[3]);
                        }
                        else {
                            em.addTorus(args[0], args[1], args[2], args[3], args[4]);
                        }
                        break;
                    case 'i':
                        tfm.makeIdentity();
                        break;
                    case 's':
                        args = parseArgs(getNextLine(in));
                        tfmTemp.makeScale(args[0], args[1], args[2]);
                        tfm.matrixMultiply(tfmTemp.transpose()); // We need to multiple it by the transpose because of the way our points are stored
                        break;
                    case 't':
                        args = parseArgs(getNextLine(in));
                        tfmTemp.makeTranslate(args[0], args[1], args[2]);
                        tfm.matrixMultiply(tfmTemp.transpose()); // We need to multiply it by the transpose because of the way our points are stored
                        break;
                    case 'x':
                        args = parseArgs(getNextLine(in));
                        tfmTemp.makeRotX(args[0]);
                        tfm.matrixMultiply(tfmTemp.transpose()); // We need to multiply it by the transpose because of the way our points are stored
                        break;
                    case 'y':
                        args = parseArgs(getNextLine(in));
                        tfmTemp.makeRotY(args[0]);
                        tfm.matrixMultiply(tfmTemp.transpose()); // We need to multiply it by the transpose because of the way our points are stored
                        break;
                    case 'z':
                        args = parseArgs(getNextLine(in));
                        tfmTemp.makeRotZ(args[0]);
                        tfm.matrixMultiply(tfmTemp.transpose()); // We need to multiply it by the transpose because of the way our points are stored
                        break;
                    case 'a':
                        em.matrixMultiply(tfm);
                        break;
                    case 'j':
                        drawingMode = DrawingMode.LINE;
                        break;
                    case 'k':
                        drawingMode = DrawingMode.POLYGON;
                        break;
                    case 'g':
                        String filename = stringStrip(getNextLine(in));
                        frame.clearFrame();
                        if (drawingMode == DrawingMode.POLYGON) {
                            frame.drawPolygons(em, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                        }
                        else {
                            frame.drawLines(em, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                        }
                        frame.saveImage(filename);
                        break;
                    case 'v':
                        frame.clearFrame();
                        if (drawingMode == DrawingMode.POLYGON) {
                            frame.drawPolygons(em, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                        }
                        else {
                            frame.drawLines(em, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                        }
                        frame.viewFrame();
                        break;
                    case 'q':
                        done = true;
                        break;
                    case 'w':
                        em.clear();
                        break;
                    default:
                        System.out.println("Unrecognized command on line " + lineNumber + ": " + line);
                        break;
                }
                line = getNextLine(in);
            }
        }
        catch (Exception e) {
            System.out.println("Error parsing script file on line " + lineNumber + ": " + line);
            e.printStackTrace();
        }
    }

    public String getNextLine(BufferedReader in) {
        try {
            return in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Goes through the input stream referred to by in, scans it for the commands
     * as shown in the guide above, and performs the required commands
     * @param in BufferedReader referenced to the opened file
     */
    public void parseFile(String filename) {
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            parseFile(reader);
            reader.close();
        }
        catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Could not find file \"" + filename + "\" to parse!");
        }
    }

    private double[] parseArgs(String line) {
        lineNumber++;
        line = stringStrip(line);
        String[] args = line.split(" ");
        double[] doubleArgs = new double[args.length];
        for (int i=0; i<args.length; i++) {
            doubleArgs[i] = Double.parseDouble(args[i]);
        }
        return doubleArgs;
    }

    private String stringStrip(String line) {
        line = line.replace("\n", "").replace("\r", "").trim();
        return line;
    }

}
