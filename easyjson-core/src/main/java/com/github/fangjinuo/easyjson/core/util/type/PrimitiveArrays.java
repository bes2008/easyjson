package com.github.fangjinuo.easyjson.core.util.type;

public class PrimitiveArrays {
    public static Boolean[] wrap(boolean[] chars) {
        if (chars == null) {
            return null;
        }
        Boolean[] ret = new Boolean[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = chars[i];
        }
        return ret;
    }

    public static boolean[] unwrap(Boolean[] chars) {
        if (chars == null) {
            return null;
        }
        boolean[] ret = new boolean[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = chars[i];
        }
        return ret;
    }

    public static Character[] wrap(char[] chars) {
        if (chars == null) {
            return null;
        }
        Character[] ret = new Character[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = chars[i];
        }
        return ret;
    }

    public static char[] unwrap(Character[] chars) {
        if (chars == null) {
            return null;
        }
        char[] ret = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = chars[i];
        }
        return ret;
    }

    public static Byte[] wrap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Byte[] ret = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i];
        }
        return ret;
    }

    public static byte[] unwrap(Byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        byte[] ret = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i];
        }
        return ret;
    }

    public static Short[] wrap(short[] bytes) {
        if (bytes == null) {
            return null;
        }
        Short[] ret = new Short[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i];
        }
        return ret;
    }

    public static short[] unwrap(Short[] bytes) {
        if (bytes == null) {
            return null;
        }
        short[] ret = new short[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i];
        }
        return ret;
    }

    public static Integer[] wrap(int[] values) {
        if (values == null) {
            return null;
        }
        Integer[] ret = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static int[] unwrap(Integer[] values) {
        if (values == null) {
            return null;
        }
        int[] ret = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static Float[] wrap(float[] values) {
        if (values == null) {
            return null;
        }
        Float[] ret = new Float[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static float[] unwrap(Float[] values) {
        if (values == null) {
            return null;
        }
        float[] ret = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static Long[] wrap(long[] values) {
        if (values == null) {
            return null;
        }
        Long[] ret = new Long[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static long[] unwrap(Long[] values) {
        if (values == null) {
            return null;
        }
        long[] ret = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static Double[] wrap(double[] values) {
        if (values == null) {
            return null;
        }
        Double[] ret = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static double[] unwrap(Double[] values) {
        if (values == null) {
            return null;
        }
        double[] ret = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }
}
