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

    public void printCommands() {
        Iterator<opCode> i = opcodes.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }
    }

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
     * Processes the opcodes parsed from the MdlParser and runs the specific
     * commands. Documentation can be found in the Mdl.spec file
     */
    public void process() {
        Iterator<opCode> i = opcodes.iterator();
        opCode oc;
        while (i.hasNext()) {
            oc = (opCode) i.next();
            if (oc instanceof opPush) {
                Matrix top = origins.peek();
                origins.push(top.copy());
            }
            else if (oc instanceof opPop) {
                origins.pop();
            }
            else if (oc instanceof opMove) {
                double[] values = ((opMove) oc).getValues();
                double x = values[0];
                double y = values[1];
                double z = values[2];
                Matrix temp = new Matrix();
                temp.makeTranslate(x, y, z);
                origins.peek().matrixMultiply(temp.transpose());
            }
            else if (oc instanceof opScale) {

            }
            System.out.println(oc);
        }
    }
}
