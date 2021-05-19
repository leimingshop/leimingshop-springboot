/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception工具类
 */
@Slf4j
public class ExceptionUtils {

    private ExceptionUtils() {
    }

    /**
     * 获取异常信息
     *
     * @param ex 异常
     * @return 返回异常信息
     */
    public static String getErrorStackTrace(Exception ex) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw, true);
            ex.printStackTrace(pw);
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception e) {
                log.error("错误信息为" + e);
            }
            try {
                if (sw != null) {
                    sw.close();
                }
            } catch (IOException e) {
                log.error("错误信息为" + e);
            }
        }

        return sw.toString();
    }
}