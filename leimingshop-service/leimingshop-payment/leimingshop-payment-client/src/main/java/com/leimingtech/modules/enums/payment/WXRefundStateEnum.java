/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.payment;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:微信退款状态
 * @Date: 2019/6/23 16:33
 * @Version: V1.0
 */
public enum WXRefundStateEnum {

    // 成功
    SUCCESS("SUCCESS"),
    //返回信息
    RETURN_MSG_OK("OK"),
    //返回信息
    RETURN_MSG_FAIL("FAIL"),
    //接口返回错误
    ERR_CODE_SYSTEMERROR("SYSTEMERROR"),
    //接口返回错误
    ERR_CODE_BIZERR_NEED_RETRY("BIZERR_NEED_RETRY");


    private String value;

    WXRefundStateEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
