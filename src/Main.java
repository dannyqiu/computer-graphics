import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        File currentDir = new File("").getAbsoluteFile();
        System.out.println(currentDir);
        if (args.length >= 1) {
            for (int i=0; i<args.length; i++) {
                parser = new Parser();
                parser.parseFile(args[i]);
            }
        }
        else {
            System.out.println("Usage:\n\tjava Main [script-file]");
        }
    }

}
