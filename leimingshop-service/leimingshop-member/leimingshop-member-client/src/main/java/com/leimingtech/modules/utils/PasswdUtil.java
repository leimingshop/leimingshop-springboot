/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils;

import java.util.Random;

/**
 * @ClassName PasswdUtil
 * @Description 随机生成密码工具类
 * @Author DY
 * @Date 2019/5/30 16:04
 * @Version 1.0
 **/
public class PasswdUtil {
    private static Random random;
    private static long seed;

    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    private static int uniform(int n) {
        return random.nextInt(n);
    }

    private static int uniform(int a, int b) {
        return a + uniform(b - a);
    }

    public static String getGeneratePassword() {
        int len = 8;
        char[] chArr = new char[len];
        chArr[0] = (char) ('0' + uniform(0, 10));
        chArr[1] = (char) ('A' + uniform(0, 26));
        chArr[2] = (char) ('a' + uniform(0, 26));

        char[] codes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
                'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                'y', 'z'};

        for (int i = 3; i < len; i++) {
            chArr[i] = codes[uniform(0, codes.length)];
        }

        for (int i = 0; i < len; i++) {
            int r = i + uniform(len - i);
            char temp = chArr[i];
            chArr[i] = chArr[r];
            chArr[r] = temp;
        }

        return new String(chArr);
    }

}
