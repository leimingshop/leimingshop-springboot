/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * 功能描述：
 * <createSHA1Sign创建签名SHA1,getSha1()Sha1签名>
 *
 * @author 刘远杰
 * @Date 2019/5/23 11:07
 * Version 7.0
 **/
@Slf4j
public class Sha1Util {
    private Sha1Util() {
    }
    //private static Logger log=Logger.getLogger(Sha1Util.class);

    public static String getNonceStr() {
        Random random = null;
        try {
            random = SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (NoSuchAlgorithmException e) {
            log.error("错误信息为" + e);
        }
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    // 创建签名SHA1
    public static String createSHA1Sign(SortedMap<String, String> signParams) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String>> es = signParams.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            sb.append(k + "=" + v + "&");
            // 要采用URLENCODER的原始值！
        }
        String params = sb.substring(0, sb.lastIndexOf("&"));
        log.info("sha1 sb:" + params);
        return getSha1(params);
    }

    // Sha1签名
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            log.error("错误信息为" + e);
            return null;
        }
    }
}
