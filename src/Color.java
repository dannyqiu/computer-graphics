import java.util.*;

public class Color {

    Random r = new Random();

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    public Color() {
        red = r.nextInt(255);
        green = r.nextInt(255);
        blue = r.nextInt(255);
    }

    public Color(int _r, int _g, int _b) {
        red = _r;
        green = _g;
        blue = _b;
    }

    public String toString() {
        return red + " " + green + " " + blue;
    }

}
