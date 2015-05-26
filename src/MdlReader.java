// @formatter:off
/*========== MDLReader.java ==========
  MDLReader objects minimally contain an ArrayList<opCode> containing
  the opCodes generated when an mdl file is run through the java created
  lexer/parser, as well as the associated SymTab (Symbol Table).

  The provided methods are a constructor, and methods to print out the
  entries in the symbol table and command ArrayList.
  This is the only file you need to modify in order
  to get a working mdl project (for now).

  Your job is to go through each entry in opCodes and perform
  the required action from the list below:

  push: push a new origin matrix onto the origin stack
  pop: remove the top matrix on the origin stack

  move/scale/rotate: create a transformation matrix
                     based on the provided values, then
                     multiply the current top of the
                     origins stack by it.

  box/sphere/torus: create a solid object based on the
                    provided values. Store that in a
                    temporary matrix, multiply it by the
                    current top of the origins stack, then
                    call draw_polygons.

  line: create a line based on the provided values. Store
        that in a temporary matrix, multiply it by the
        current top of the origins stack, then call draw_lines.

  save: save the current screen with the provided filename

=================================*/
// @formatter:on

import java.util.*;
import java.io.*;

import parser.*;
import parseTables.*;

public class MdlReader {

    ArrayList<opCode> opcodes;
    SymTab symbols;
    Set<String> symKeys;
    Stack<Matrix> origins;
    EdgeMatrix tmp;
    Frame frame;

    boolean isAnimation = false;
    int numFrames = 0;
    String basename = "";
    String formatString;
    Hashtable<String, Double[]> knobs = new Hashtable<String, Double[]>();

    public MdlReader(ArrayList<opCode> o, SymTab s) {
        opcodes = o;
        symbols = s;
        symKeys = s.keySet();

        tmp = new EdgeMatrix();
        frame = new Frame();
        Matrix m = new Matrix(4);
        m.identity();
        origins = new Stack<Matrix>();
        origins.push(m);
    }

    /**
     * Prints all commands that have been parsed by MDL
     */
    public void printCommands() {
        Iterator<opCode> i = opcodes.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }
    }

    /**
     * Prints all systems that have been parsed by MDL
     */
    public void printSymbols() {
        Iterator<String> i = symKeys.iterator();
        System.out.println("Symbol Table:");
        while (i.hasNext()) {
            String key = (String) i.next();
            Object value = symbols.get(key);
            System.out.println(key + "=" + value);
        }
    }

    /**
     * Prints all knobs and their values for each frame
     */
    public void printKnobs() {
        Enumeration<String> knobKeys = knobs.keys();
        while (knobKeys.hasMoreElements()) {
            String knob = knobKeys.nextElement();
            Double[] values = knobs.get(knob);
            System.out.println(knob + ": " + Arrays.toString(values));
        }
    }

    /**
     * Prints the matrix values in the stack
     */
    public void printStack() {
        System.out.println(origins);
    }

    public void animationPass() throws ParseException {
        Iterator<opCode> i = opcodes.iterator();
        opCode oc;
        while (i.hasNext()) {
            oc = (opCode) i.next();
            if (oc instanceof opFrames) {
                if (numFrames != 0) {
                    System.out.println("WARNING: You are setting the number of animation frames multiple times.");
                }
                isAnimation = true;
                numFrames = ((opFrames) oc).getNum();
            }
            else if (oc instanceof opBasename) {
                if (basename.length() != 0) {
                    System.out.println("WARNING: You are setting the animation basename multiple times.");
                }
                isAnimation = true;
                basename = ((opBasename) oc).getName();
            }
            else if (oc instanceof opVary) {
                isAnimation = true;
            }
        }
        if (isAnimation) {
            if (numFrames == 0) {
                throw new ParseException(
                        "You must specify the number of frames for your animation.");
            }
            if (basename.length() == 0) {
                throw new ParseException(
                        "You must specify the basename for your animation.");
            }
            i = opcodes.iterator();
            while (i.hasNext()) {
                oc = (opCode) i.next();
                if (oc instanceof opVary) {
                    Double[] knobValues = new Double[numFrames];
                    int start = ((opVary) oc).getStartframe();
                    int end = ((opVary) oc).getEndframe();
                    double startVal = ((opVary) oc).getStartval();
                    double endVal = ((opVary) oc).getEndval();
                    double change = (endVal - startVal) / (end - start + 1);
                    for (int f = start; f <= end; f++) {
                        knobValues[f] = f * change;
                    }
                    knobs.put(((opVary) oc).getKnob(), knobValues);
                }
            }
            new File(basename).mkdir();
            int numLength = Integer.toString(numFrames).length();
            formatString = basename + "/" + basename + "-%0" + numLength + "d";
        }
        else {
            numFrames = 1;
        }
    }

    /**
     * Processes the opcodes parsed from the MdlParser and runs the specific
     * commands. Documentation can be found in the Mdl.spec file
     */
    public void process() throws ParseException {
        animationPass();
        for (int f = 0; f < numFrames; f++) {
            Iterator<opCode> i = opcodes.iterator();
            opCode oc;
            while (i.hasNext()) {
                oc = (opCode) i.next();
                //System.out.println(oc);
                if (oc instanceof opPush) {
                    Matrix top = origins.peek();
                    origins.push(top.copy());
                }
                else if (oc instanceof opPop) {
                    origins.pop();
                }
                else if (oc instanceof opMove) {
                    double[] values = ((opMove) oc).getValues();
                    double x = values[0], y = values[1], z = values[2];
                    if (((opMove) oc).getKnob() != null) {
                        double knobValue = knobs.get(((opMove) oc).getKnob())[f];
                        x *= knobValue; y *= knobValue; z *= knobValue;
                    }
                    Matrix temp = new Matrix();
                    temp.makeTranslate(x, y, z);
                    origins.peek().matrixMultiply(temp);
                }
                else if (oc instanceof opScale) {
                    double[] values = ((opScale) oc).getValues();
                    double x = values[0], y = values[1], z = values[2];
                    if (((opScale) oc).getKnob() != null) {
                        double knobValue = knobs.get(((opScale) oc).getKnob())[f];
                        x *= knobValue; y *= knobValue; z *= knobValue;
                    }
                    Matrix temp = new Matrix();
                    temp.makeScale(x, y, z);
                    origins.peek().matrixMultiply(temp);
                }
                else if (oc instanceof opRotate) {
                    char axis = ((opRotate) oc).getAxis();
                    double degrees = ((opRotate) oc).getDeg();
                    if (((opRotate) oc).getKnob() != null) {
                        degrees *= knobs.get(((opRotate) oc).getKnob())[f];
                    }
                    Matrix temp = new Matrix();
                    switch (axis) {
                        case 'x':
                            temp.makeRotX(degrees);
                            break;
                        case 'y':
                            temp.makeRotY(degrees);
                            break;
                        case 'z':
                            temp.makeRotZ(degrees);
                            break;
                    }
                    origins.peek().matrixMultiply(temp);
                }
                else if (oc instanceof opBox) {
                    double loc[] = ((opBox) oc).getP1();
                    double x = loc[0], y = loc[1], z = loc[2];
                    double dim[] = ((opBox) oc).getP2();
                    double l = dim[0], h = dim[1], d = dim[2];
                    tmp.addPrism(x, y, z, l, h, d);
                    tmp.matrixMultiply(origins.peek().transpose());
                    frame.drawPolygons(tmp, new Color());
                    tmp.clear();
                }
                else if (oc instanceof opSphere) {
                    double center[] = ((opSphere) oc).getCenter();
                    double cx = center[0], cy = center[1], cz = center[2];
                    double r = ((opSphere) oc).getR();
                    tmp.addSphere(cx, cy, cz, r);
                    tmp.matrixMultiply(origins.peek().transpose());
                    frame.drawPolygons(tmp, new Color());
                    tmp.clear();
                }
                else if (oc instanceof opTorus) {
                    double center[] = ((opTorus) oc).getCenter();
                    double cx = center[0], cy = center[1], cz = center[2];
                    double R = ((opTorus) oc).getR(), r = ((opTorus) oc).getr();
                    tmp.addTorus(cx, cy, cz, R, r);
                    tmp.matrixMultiply(origins.peek().transpose());
                    frame.drawPolygons(tmp, new Color());
                    tmp.clear();
                }
                else if (oc instanceof opLine) {
                    double[] start = ((opLine) oc).getP1();
                    double x0 = start[0], y0 = start[1], z0 = start[2];
                    double[] end = ((opLine) oc).getP2();
                    double x1 = end[0], y1 = end[1], z1 = end[2];
                    tmp.addEdge(x0, y0, z0, x1, y1, z1);
                    tmp.matrixMultiply(origins.peek());
                    frame.drawLines(tmp, new Color());
                    tmp.clear();
                }
                else if (oc instanceof opSave) {
                    String filename = ((opSave) oc).getName();
                    frame.saveImage(filename);
                }
                else if (oc instanceof opDisplay) {
                    frame.display();
                }
            }
            if (isAnimation) {
                String filename = String.format(formatString, f);
                frame.saveImage(filename);
                reset();
            }
        }
    }

    public void reset() {
        frame.clearFrame();
        tmp.clear();
        origins.clear();
        Matrix m = new Matrix(4);
        m.identity();
        origins.push(m);
    }

}
