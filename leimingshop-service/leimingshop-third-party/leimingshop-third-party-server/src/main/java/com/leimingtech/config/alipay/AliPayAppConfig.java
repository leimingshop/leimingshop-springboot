/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config.alipay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zhangtai
 * @date 2020/4/2 15:54
 * @Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay.app")
@PropertySource(value = "alipay/alipay-app-${spring.profiles.active}.properties")
public class AliPayAppConfig {

    // 支付宝网关(固定)
    public String URL;

    // APP_Id
    public String APP_ID;

    // 应用私钥
    public String APP_PRIVATE_KEY;

    // 请求格式，固定值json
    public String FORMAT;

    // 字符集
    public String CHARSET;

    // 签名类型
    public String SIGN_TYPE;

    // 应用公钥证书路径
    public String CERT_PATH;

    // 支付宝公钥证书路径
    public String ALIPAY_PUBLIC_CERT_PATH;

    // 支付宝根证书路径
    public String ROOT_CERT_PATH;

    // 销售产品码，商家和支付宝签约的产品码。wap支付：QUICK_WAP_WAY; app支付：QUICK_MSECURITY_PAY
    public String PRODUCT_CODE;
    // 支付成功的通知地址
    public String ALIPAY_NOTIFY_URL;
}
