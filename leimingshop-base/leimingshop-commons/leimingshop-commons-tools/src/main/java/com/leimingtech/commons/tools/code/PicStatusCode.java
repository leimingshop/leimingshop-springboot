/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.code;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 图片校验错误码
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/4 18:12
 **/
public class PicStatusCode extends ServiceStatusCode {

    /**
     * 图片格式存在问题
     */
    public static final ServiceStatusCode PIC_TYPE_ERROR = new PicStatusCode("400600", "图片格式错误,支持上传格式为：jpg,jpeg,png,bmp,gif");

    /**
     * 图片不能大于5M
     */
    public static final ServiceStatusCode PIC_SIZE_ERROR = new PicStatusCode("400601", "图片不能大于5M");

    public static final ServiceStatusCode PIC_OTHER_ERROR = new PicStatusCode("500600", "上传图片未知异常");

    protected PicStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
