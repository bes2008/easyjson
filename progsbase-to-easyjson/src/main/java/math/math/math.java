package math.math;

import references.references.NumberReference;

import static java.lang.Math.*;


public class math {
    public static double Negate(double x) {

        return -x;
    }

    public static double Positive(double x) {

        return +x;
    }

    public static double Factorial(double x) {
        double i, f;

        f = 1d;

        for (i = 2d; i <= x; i = i + 1d) {
            f = f * i;
        }

        return f;
    }

    public static double Round(double x) {

        return floor(x + 0.5);
    }

    public static double BankersRound(double x) {
        double r;

        if (Absolute(x - Truncate(x)) == 0.5) {
            if (!DivisibleBy(Round(x), 2d)) {
                r = Round(x) - 1d;
            } else {
                r = Round(x);
            }
        } else {
            r = Round(x);
        }

        return r;
    }

    public static double Ceil(double x) {

        return ceil(x);
    }

    public static double Floor(double x) {

        return floor(x);
    }

    public static double Truncate(double x) {
        double t;

        if (x >= 0d) {
            t = floor(x);
        } else {
            t = ceil(x);
        }

        return t;
    }

    public static double Absolute(double x) {

        return abs(x);
    }

    public static double Logarithm(double x) {

        return log10(x);
    }

    public static double NaturalLogarithm(double x) {

        return log(x);
    }

    public static double Sin(double x) {

        return sin(x);
    }

    public static double Cos(double x) {

        return cos(x);
    }

    public static double Tan(double x) {

        return tan(x);
    }

    public static double Asin(double x) {

        return asin(x);
    }

    public static double Acos(double x) {

        return acos(x);
    }

    public static double Atan(double x) {

        return atan(x);
    }

    public static double Atan2(double y, double x) {
        double a;

        a = 0d;

        if (x > 0d) {
            a = Atan(y / x);
        } else if (x < 0d && y >= 0d) {
            a = Atan(y / x) + PI;
        } else if (x < 0d && y < 0d) {
            a = Atan(y / x) - PI;
        } else if (x == 0d && y > 0d) {
            a = PI / 2d;
        } else if (x == 0d && y < 0d) {
            a = -PI / 2d;
        }


        return a;
    }

    public static double Squareroot(double x) {

        return sqrt(x);
    }

    public static double Exp(double x) {

        return exp(x);
    }

    public static boolean DivisibleBy(double a, double b) {

        return ((a % b) == 0d);
    }

    public static double Combinations(double n, double k) {

        return Factorial(n) / (Factorial(n - k) * Factorial(k));
    }

    public static boolean EpsilonCompareApproximateDigits(double a, double b, double digits) {
        double ad, bd, d, epsilon;
        boolean ret;

        if (a < 0d && b < 0d || a > 0d && b > 0d) {
            if (a < 0d && b < 0d) {
                a = -a;
                b = -b;
            }
            ad = log10(a);
            bd = log10(b);
            d = max(ad, bd);
            epsilon = pow(10d, d - digits);
            ret = abs(a - b) > epsilon;
        } else {
            ret = false;
        }

        return ret;
    }

    public static boolean EpsilonCompare(double a, double b, double epsilon) {

        return abs(a - b) < epsilon;
    }

    public static double GreatestCommonDivisor(double a, double b) {
        double t;

        for (; b != 0d; ) {
            t = b;
            b = a % b;
            a = t;
        }

        return a;
    }

    public static boolean IsInteger(double a) {

        return (a - floor(a)) == 0d;
    }

    public static boolean GreatestCommonDivisorWithCheck(double a, double b, NumberReference gcdReference) {
        boolean success;
        double gcd;

        if (IsInteger(a) && IsInteger(b)) {
            gcd = GreatestCommonDivisor(a, b);
            gcdReference.numberValue = gcd;
            success = true;
        } else {
            success = false;
        }

        return success;
    }

    public static double LeastCommonMultiple(double a, double b) {
        double lcm;

        if (a > 0d && b > 0d) {
            lcm = abs(a * b) / GreatestCommonDivisor(a, b);
        } else {
            lcm = 0d;
        }

        return lcm;
    }

    public static double Sign(double a) {
        double s;

        if (a > 0d) {
            s = 1d;
        } else if (a < 0d) {
            s = -1d;
        } else {
            s = 0d;
        }


        return s;
    }

    public static double Max(double a, double b) {

        return max(a, b);
    }

    public static double Min(double a, double b) {

        return min(a, b);
    }

    public static double Power(double a, double b) {

        return pow(a, b);
    }

    public static void delete(Object object) {
        // Java has garbage collection.
    }
}
