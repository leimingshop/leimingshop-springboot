/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import com.leimingtech.commons.tools.code.PicStatusCode;
import com.leimingtech.commons.tools.utils.file.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片检验工具
 *
 * @ClassName:ImageCheckUtils
 * @Data:2019/5/12 11:15
 * @Author：chengqian
 * @Version 1.0
 */
@Slf4j
public class ImageCheckUtils {

    /**
     * 不同文件的头魔术数字
     */
    private static Map<String, String> FILE_TYPE_MAP;

    /**
     *
     * 静态方法加载 不同文件的头魔术数字
     * FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG
     * FILE_TYPE_MAP.put("png", "89504E47"); //PNG
     * FILE_TYPE_MAP.put("gif", "47494638"); //GIF
     * FILE_TYPE_MAP.put("tif", "49492A00"); //TIFF
     * FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap
     * FILE_TYPE_MAP.put("dwg", "41433130"); //CAD
     * FILE_TYPE_MAP.put("html", "68746D6C3E"); //HTML
     * FILE_TYPE_MAP.put("rtf", "7B5C727466"); //Rich Text Format
     * FILE_TYPE_MAP.put("xml", "3C3F786D6C");
     * FILE_TYPE_MAP.put("zip", "504B0304");
     * FILE_TYPE_MAP.put("rar", "52617221");
     * FILE_TYPE_MAP.put("psd", "38425053"); //PhotoShop
     * FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A"); //Email [thorough only]
     * FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F"); //Outlook Express
     * FILE_TYPE_MAP.put("pst", "2142444E"); //Outlook
     * FILE_TYPE_MAP.put("office", "D0CF11E0"); //office类型，包括doc、xls和ppt
     * FILE_TYPE_MAP.put("mdb", "000100005374616E64617264204A"); //MS Access
     * FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect
     * FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
     * FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
     * FILE_TYPE_MAP.put("pdf", "255044462D312E"); //Adobe Acrobat
     * FILE_TYPE_MAP.put("qdf", "AC9EBD8F"); //Quicken
     * FILE_TYPE_MAP.put("pwl", "E3828596"); //Windows Password
     * FILE_TYPE_MAP.put("wav", "57415645"); //Wave
     * FILE_TYPE_MAP.put("avi", "41564920");
     * FILE_TYPE_MAP.put("ram", "2E7261FD"); //Real Audio
     * FILE_TYPE_MAP.put("rm", "2E524D46"); //Real Media
     * FILE_TYPE_MAP.put("mpg", "000001BA"); //
     * FILE_TYPE_MAP.put("mov", "6D6F6F76"); //Quicktime
     * FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media
     * FILE_TYPE_MAP.put("mid", "4D546864"); //MIDI (mid)
     *FILE_TYPE_MAP.put("xlsx", "504b03041400");
     * FILE_TYPE_MAP.put("xls", "D0CF11E0");
     * @date 2020/5/4 18:07
     * @author lixiangx@leimingtech.com
     **/
    static {
        FILE_TYPE_MAP = new HashMap<>(10);
        FILE_TYPE_MAP.put("FFD8FF", "jpg"); //JPEG
        FILE_TYPE_MAP.put("89504E47", "png"); //PNG
        FILE_TYPE_MAP.put("47494638", "gif"); //GIF
        FILE_TYPE_MAP.put("424D", "bmp"); //Windows Bitmap
    }


    /**
     * 上传图片校验(格式、大小)
     *
     * @param base64Str 图片base64字符串
     * @return 文件类型、大小数据
     * @date 2019/8/6 17:13
     * @author lixiang
     **/
    public static Map<String, Object> isImage(String base64Str) {

        Map<String, Object> result = new HashMap<>(16);

        // 移除头部 data:image/png;base64, 的字符串
        String removeTopStr = StringUtils.substring(base64Str, StringUtils.indexOf(base64Str, ","));

        // 获取图片的魔术数字
        String imageStr = bytesToHexString(Base64.decodeBase64(removeTopStr));

        // 图片类型
        String[] imageType = new String[1];

        // 校验图片类型
        boolean checkResult = FILE_TYPE_MAP.keySet().stream().anyMatch(type -> {
            String substring = StringUtils.substring(imageStr, 0, type.length());
            if (StringUtils.equalsIgnoreCase(type, substring)) {
                imageType[0] = FILE_TYPE_MAP.get(type);
                return true;
            }
            return false;
        });

        if (!checkResult) {
            result.put("code", PicStatusCode.PIC_TYPE_ERROR.getCode());
            result.put("msg", PicStatusCode.PIC_TYPE_ERROR.getMessage());
            return result;
        }

        // 校验图片大小
        long picSize = Base64Util.fileSize(base64Str);

        if (picSize > 5242880) {
            // 上传图片不能大于5M
            result.put("code", PicStatusCode.PIC_SIZE_ERROR.getCode());
            result.put("msg", PicStatusCode.PIC_SIZE_ERROR.getMessage());
            return result;
        }

        result.put("code", "200");
        result.put("imageType", imageType[0]);
        result.put("imageSize", picSize);
        return result;
    }

    /**
     * 获得文件头部字符串（内含数字）
     *
     * @param src 图片转化的byte数组
     * @return 头部字符串（内含数字）
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
