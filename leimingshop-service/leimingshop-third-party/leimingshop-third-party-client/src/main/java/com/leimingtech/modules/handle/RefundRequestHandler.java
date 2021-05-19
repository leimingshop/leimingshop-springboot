/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.handle;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:退款响应
 * @Date: 2019/6/22 16:26
 * @Version: V1.0
 */
@Slf4j
public class RefundRequestHandler extends RequestHandler {

    private String charset;

    /**
     * 密钥
     */
    private String key;
    /**
     * debug信息
     */
    private String debugInfo;
    private String last_errcode;
    private String Token;
    private String appid;
    private String partnerkey;
    private String appsecret;

    public RefundRequestHandler(String enc) {
        super(enc);
    }


    /**
     * 初始化函数。
     */
    @Override
    public void init(String app_id, String app_secret, String partner_key) {
        this.last_errcode = "0";
        this.Token = "token_";
        this.debugInfo = "";
        this.appid = app_id;
        this.partnerkey = partner_key;
        this.appsecret = app_secret;
        this.key = partner_key;
        super.setKey(partner_key);
    }
}
