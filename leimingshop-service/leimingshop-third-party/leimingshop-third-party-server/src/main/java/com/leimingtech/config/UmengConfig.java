/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zhangtai
 * @date 2020/4/14 13:44
 * @Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "umeng")
@PropertySource(value = "umeng/umeng-${spring.profiles.active}.properties")
public class UmengConfig {

    public String ANDROID_APPKEY;

    public String ANDROID_APP_MASTER_SECRET;

    public String ANDROID_MESSAGE_SECRET;

    public String IOS_APPKEY;

    public String IOS_APP_MASTER_SECRET;
    // http服务的请求地址
    public String HTTP_HOST;
    // https服务的请求地址
    public String HTTPS_HOST;
    // 0为使用http请求,1为使用https请求
    public Integer HTTP_FLAG;
    // 文件路径
    public String UPLOAD_PATH;
    // 消息发送路径
    public String POST_PATH;
}
