/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统地址前缀
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/24 14:23
 **/
@Component
public class AddressPrefixProperties {

    /**
     * 系统地址域名
     */
    public static String adddressPrefix;

    @Value("${system.address.prefix}")
    public void setLocalPath(String prefix) {
        adddressPrefix = prefix;
    }

}
