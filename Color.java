public class Color {

    private int red = 0;
    private int green = 0;
    private int blue = 0 ;

    public Color() {}

    public Color(int _r, int _g, int _b) {
        red = _r;
        green = _g;
        blue = _b;
    }

    public String toString() {
        return red + " " + green + " " + blue;
    }

}
