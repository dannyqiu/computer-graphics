public class Main {

    static final int XRES = 500;
    static final int YRES = 500;

    public static void main(String[] args) {
        Parser parser = new Parser();
        if (args.length <= 1) {
            parser.parseFile("script");
        }
        else {
            for (int i=0; i<args.length; i++) {
                parser = new Parser();
                parser.parseFile(args[i]);
            }
        }
    }

}
