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

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public void setRed(int r) {
        red = r;
    }

    public void setBlue(int b) {
        blue = b;
    }

    public void setGreen(int g) {
        green = g;
    }

    public static Color doubleToColor(double[] values) {
        int r = bound((int) values[0]);
        int g = bound((int) values[1]);
        int b = bound((int) values[2]);
        return new Color(r, g, b);
    }

    int BOUND_MIN = 0;
    int BOUND_MAX = 255;

    private static int bound(int value) {
        if (value > 255) {
            value = 255;
        }
        else if (value < 0) {
            value = 0;
        }
        return value;
    }

    public String toString() {
        return red + " " + green + " " + blue;
    }

}
