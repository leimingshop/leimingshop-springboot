/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config.alipay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @param :
 * @Description 读取支付宝配置信息
 * @Author huangkeyuan
 * @Date 15:59 2019-12-09
 * @return
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay.pc")
@PropertySource(value = "alipay/alipay-pc-${spring.profiles.active}.properties")
public class AliPayPcConfig {
    /**
     * 商户私钥
     */
    public String APP_PRIVATE_KEY;
    /**
     * 支付宝APPID
     */
    public String APPID;

    /**
     * 应用公钥证书路径
     */
    public String APP_CERT_PATH;

    /**
     * 支付宝公钥证书文件路径
     */
    public String ALIPAY_CERT_PATH;

    /**
     * 支付宝CA根证书文件路径
     */
    public String ALIPAY_ROOT_CERT_PATH;

    /**
     * 请求网关
     */
    public String SERVERURL;

    /**
     * 支付成功的通知地址
     */
    public String ALIPAY_NOTIFY_URL;

    /**
     * 字符集
     */
    public String CHARSET;

    /**
     * 签名类型
     */
    public String SIGN_TYPE;

    /**
     * 格式
     */
    public String FORMAT;

    /**
     * pc支付完成之后的回调地址
     */
    public String RETURNURL;

    /**
     * 支付方式类型
     */
    public String PAYTYPE;


}
