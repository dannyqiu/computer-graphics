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

    public static Color doubletoColor(double[] values) {
        int r = (values[0] > 255) ? 255 : (int) values[0];
        int g = (values[1] > 255) ? 255 : (int) values[1];
        int b = (values[2] > 255) ? 255 : (int) values[2];
        return new Color(r, g, b);
    }

    public String toString() {
        return red + " " + green + " " + blue;
    }

}
