import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        if (args.length >= 1) {
            for (int i=0; i<args.length; i++) {
                parser = new Parser();
                parser.parseFile(args[i]);
            }
        }
        else {
            try {
                File script = new File("awesome");
                BufferedWriter writer = new BufferedWriter(new FileWriter(script));
                for (int z=0; z<4; z++) {
                    for (int i=50; i<1000; i+=100) {
                        writer.write("h\n" + "0 0 " + Math.pow(i,1.1) + " " + Math.pow(i,1.2) + " 500 500 " + Math.pow(i,1.1) + " " + Math.pow(i,1.2) + "\n");
                    }
                    for (int i=50; i<1000; i+=100) {
                        writer.write("h\n" + "0 0 " + Math.pow(i,1.2) + " " + Math.pow(i,1.1) + " 500 500 " + Math.pow(i,1.2) + " " + Math.pow(i,1.1) + "\n");
                    }
                    writer.write("z\n90\na\n");
                }
                writer.write("t\n250 250 0\ns\n.5 .5 0\na\n");
                for (int i=10; i<200; i+=10) {
                    writer.write("c\n" + Math.pow(i,1.2) + " " + 250 + " " + i + "\n");
                }
                for (int i=10; i<200; i+=10) {
                    writer.write("c\n" + (500-Math.pow(i,1.2)) + " " + 250 + " " + i + "\n");
                }
                for (int i=10; i<200; i+=10) {
                    writer.write("c\n250 " + Math.pow(i,1.2) + " " + i + "\n");
                }
                for (int i=10; i<200; i+=10) {
                    writer.write("c\n250 " + (500-Math.pow(i,1.2)) + " " + i + "\n");
                }
                writer.write("g\nawesome.png\nq\n");
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
