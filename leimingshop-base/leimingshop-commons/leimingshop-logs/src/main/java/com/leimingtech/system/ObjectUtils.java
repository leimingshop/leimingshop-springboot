//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.system;

import java.util.Arrays;

public class ObjectUtils {
    public ObjectUtils() {
    }

    public static boolean nullSafeEquals(Object o1, Object o2) {
        return o1 == o2 ? true : (o1 != null && o2 != null ? (o1.equals(o2) ? true : (o1.getClass().isArray() && o2.getClass().isArray() ? arrayEquals(o1, o2) : false)) : false);
    }

    private static boolean arrayEquals(Object o1, Object o2) {
        return o1 instanceof Object[] && o2 instanceof Object[] ? Arrays.equals((Object[]) ((Object[]) o1), (Object[]) ((Object[]) o2)) : (o1 instanceof boolean[] && o2 instanceof boolean[] ? Arrays.equals((boolean[]) ((boolean[]) o1), (boolean[]) ((boolean[]) o2)) : (o1 instanceof byte[] && o2 instanceof byte[] ? Arrays.equals((byte[]) ((byte[]) o1), (byte[]) ((byte[]) o2)) : (o1 instanceof char[] && o2 instanceof char[] ? Arrays.equals((char[]) ((char[]) o1), (char[]) ((char[]) o2)) : (o1 instanceof double[] && o2 instanceof double[] ? Arrays.equals((double[]) ((double[]) o1), (double[]) ((double[]) o2)) : (o1 instanceof float[] && o2 instanceof float[] ? Arrays.equals((float[]) ((float[]) o1), (float[]) ((float[]) o2)) : (o1 instanceof int[] && o2 instanceof int[] ? Arrays.equals((int[]) ((int[]) o1), (int[]) ((int[]) o2)) : (o1 instanceof long[] && o2 instanceof long[] ? Arrays.equals((long[]) ((long[]) o1), (long[]) ((long[]) o2)) : (o1 instanceof short[] && o2 instanceof short[] ? Arrays.equals((short[]) ((short[]) o1), (short[]) ((short[]) o2)) : false))))))));
    }

    public static int nullSafeHashCode(Object obj) {
        if (obj == null) {
            return 0;
        } else {
            if (obj.getClass().isArray()) {
                if (obj instanceof Object[]) {
                    return nullSafeHashCode((Object[]) ((Object[]) obj));
                }

                if (obj instanceof boolean[]) {
                    return nullSafeHashCode((boolean[]) ((boolean[]) obj));
                }

                if (obj instanceof byte[]) {
                    return nullSafeHashCode((byte[]) ((byte[]) obj));
                }

                if (obj instanceof char[]) {
                    return nullSafeHashCode((char[]) ((char[]) obj));
                }

                if (obj instanceof double[]) {
                    return nullSafeHashCode((double[]) ((double[]) obj));
                }

                if (obj instanceof float[]) {
                    return nullSafeHashCode((float[]) ((float[]) obj));
                }

                if (obj instanceof int[]) {
                    return nullSafeHashCode((int[]) ((int[]) obj));
                }

                if (obj instanceof long[]) {
                    return nullSafeHashCode((long[]) ((long[]) obj));
                }

                if (obj instanceof short[]) {
                    return nullSafeHashCode((short[]) ((short[]) obj));
                }
            }

            return obj.hashCode();
        }
    }
}
