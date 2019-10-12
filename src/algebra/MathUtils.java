package algebra;

class MathUtils {

    static int gcd(int x, int y) {
        while (y != 0)
            y = x % (x = y);
        return x;
    }

    static int lcm(int x, int y) {
        return x / gcd(x, y) * y;
    }
}
