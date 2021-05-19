/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lishuo
 * @Date: 14/7/2020 16:27
 * @email: lishuo@leimingtech.com
 * @Description: 根据魔术数字 判断是不是excel文件
 */
public class ExcelCheckUtil {
    /**
     * excel文件的头魔术数字
     */
    private static Map<String, String> FILE_TYPE_MAP;

    static {
        FILE_TYPE_MAP = new HashMap<>(10);
        FILE_TYPE_MAP.put("D0CF11E0", "xlsx"); //JPEG
        FILE_TYPE_MAP.put("504B03041400", "xlx");
    }

    public static boolean isExcel(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[100];
        int read = inputStream.read(bytes);
        StringBuilder stringBuilder = new StringBuilder();
        String tmp = null;
        for (byte aByte : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & aByte);
            // 每个字节8为，转为16进制标志，2个16进制位
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            stringBuilder.append(tmp);
        }
        if (stringBuilder.toString().toUpperCase().startsWith("D0CF11E0") || stringBuilder.toString().toUpperCase().startsWith("504B03041400")) {
            return true;
        } else {
            return false;
        }
    }
}