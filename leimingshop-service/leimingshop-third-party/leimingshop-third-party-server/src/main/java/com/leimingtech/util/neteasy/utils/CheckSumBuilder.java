/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.neteasy.utils;

import com.leimingtech.modules.util.UUID;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class CheckSumBuilder {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static Map<String, String> getSignParam() {
        String AppKey = "0a57a124d671059e603b6bee6c1dacbe";
        String appSecret = "65840eabdb32";
        String Nonce = UUID.next() + "";
        String CurTime = System.currentTimeMillis() / 1000 + "";
        String CheckSum = getCheckSum(appSecret, Nonce, CurTime);
        Map<String, String> map = new HashMap<>();
        map.put("AppKey", AppKey);
        map.put("Nonce", Nonce);
        map.put("CurTime", CurTime);
        map.put("CheckSum", CheckSum);
        return map;
    }

    public static void main(String[] args) {
        String appseret = "65840eabdb32";
        String nonce = "kuangweiguo";
        String currTime = System.currentTimeMillis() / 1000 + "";
        System.out.println(currTime);
        System.out.println(getCheckSum("65840eabdb32", nonce, currTime));
    }

}