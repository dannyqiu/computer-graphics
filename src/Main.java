import java.util.*;
import java.io.*;

import parser.*;
import parseTables.*;

public class Main {

    public static void main(String args[]) throws ParseException {
        ArrayList<opCode> a;
        SymTab s;
        MdlParser parser;
        String file;
        if (args.length == 1)
            file = args[0];
        else
            file = "test.mdl";
        try {
            parser = new MdlParser(new FileReader(file));
        }
        catch (IOException e) {
            parser = new MdlParser(System.in);
        }

        parser.start();
        a = parser.getOps();
        s = parser.getSymTab();

        MdlReader mr = new MdlReader(a, s);
        mr.process();

        //System.out.println("Opcodes:");
        //Iterator<opCode> i = a.iterator();
        //while (i.hasNext()) {
        //    System.out.println(i.next());
        //}
        //System.out.println("\n\n");
        //Set<String> kset = s.keySet();
        //Iterator<String> k = kset.iterator();
        //System.out.println("Symbol Table:");
        //while (k.hasNext()) {
        //    String key = k.next();
        //    Object value = s.get(key);
        //    System.out.println(key + "=" + value);
        //}

    }

    public static void Parse(String[] args) {
        Parser parser = new Parser();
        File currentDir = new File("").getAbsoluteFile();
        System.out.println(currentDir);
        if (args.length >= 1) {
            for (int i = 0; i < args.length; i++) {
                parser = new Parser();
                parser.parseFile(args[i]);
            }
        }
        else {
            System.out.println("Usage:\n\tjava Main [script-file]");
        }
    }

}
