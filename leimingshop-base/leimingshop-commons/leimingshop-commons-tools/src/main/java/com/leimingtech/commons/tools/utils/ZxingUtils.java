/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import cn.hutool.extra.qrcode.QrCodeUtil;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 二维码工具类
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/7 18:25
 **/
public class ZxingUtils {


    /**
     * 内容生成二维码
     *
     * @param content: 文本内容
     * @return 文件输出流
     * @date 2020/5/7 18:27
     * @author lixiangx@leimingtech.com
     **/
    public static ByteArrayOutputStream generateQRCode(String content) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 生成指定url对应的二维码到文件，宽和高都是300像素
        QrCodeUtil.generate(content, 300, 300, "png", out);
        return out;
    }

    /**
     * 文本转换为二维码base64
     *
     * @param content: 文本内容
     * @return 文本转化二维码的base64
     * @date 2020/5/20 9:38
     * @author lixiangx@leimingtech.com
     **/
    public static String generateQrCodeBase64(String content) {
        return "data:image/png;base64," + Base64.encodeBase64String(generateQRCode(content).toByteArray());
    }

}
