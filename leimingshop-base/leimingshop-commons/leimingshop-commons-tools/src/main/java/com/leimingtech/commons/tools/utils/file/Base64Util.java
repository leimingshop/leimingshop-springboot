/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述：
 * <base64文件转MultipartFile>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/6/10 16:01
 **/
@Slf4j
public class Base64Util {

    private Base64Util() {
    }

    /**
     * Base64转成图片
     *
     * @param base64
     * @return
     */
    public static MultipartFile base64ToMultipart(String base64) throws IOException {

        String[] baseStrs = base64.split(",");

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = new byte[0];
        b = decoder.decodeBuffer(baseStrs[1]);

        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        return new BASE64DecodedMultipartFile(b, baseStrs[0]);
    }

    /**
     * 功能描述:
     * 〈通过文件base64流判断文件等于多少字节〉
     *
     * @param fileStr base64文件字符串
     * @author : 刘远杰
     */
    public static long fileSize(String fileStr) {
        // 1.需要计算文件流大小，如头部的data:image/png;base64,到逗号结束内容去掉。
        int begin = fileStr.indexOf(",");
        String str = fileStr.substring(begin + 1);
        //2.找到等号，把等号也去掉
        int equalIndex = str.indexOf("=");
        if (str.indexOf("=") > 0) {
            str = str.substring(0, equalIndex);
        }
        //3.原来的字符流大小，单位为字节
        long strLength = (long) str.length();
        //4.计算后得到的文件流大小，单位为字节
        long size = strLength - (strLength / 8) * 2;
        return size;
    }

    /**
     * 功能描述:
     * 〈获取base64上传的文件格式〉
     *
     * @param fileStr base64文件字符串
     * @author : 刘远杰
     */
    public static String getFileType(String fileStr) {
        // 1.获得文件格式内容
        int begin = fileStr.indexOf("/");
        int end = fileStr.indexOf(";");
        return fileStr.substring(begin + 1, end);
    }

    /**
     * 功能描述:
     * 〈base64字符串转byte[]〉
     *
     * @param base64 base64字符串
     * @author : 刘远杰
     */
    public static byte[] base642Byte(String base64) {
        return Base64.decodeBase64(base64);
    }

    /**
     * 功能描述:
     * 〈byte[]转base64〉
     *
     * @param b byte数组
     * @author : 刘远杰
     */
    public static String byte2Base64(byte[] b) {
        return Base64.encodeBase64String(b);
    }


    /**
     * 将图片base64字符串转化为byte数组
     *
     * @param base64Str: 图片对应的base64字符串
     * @return: byte数值
     * @date 2019/8/6 17:31
     * @author lixiang
     **/
    public static byte[] imgBase64ToByte(String base64Str) {

        // 获得图片大小
        String[] baseStrs = base64Str.split(",");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] base64ByteArr = new byte[0];
        try {
            base64ByteArr = decoder.decodeBuffer(baseStrs[1]);
            for (int i = 0; i < base64ByteArr.length; ++i) {
                if (base64ByteArr[i] < 0) {
                    base64ByteArr[i] += 256;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64ByteArr;
    }


    public static String getFileBase64(File file) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("转换失败", e);
                }
            }
        }
        //Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }


}
