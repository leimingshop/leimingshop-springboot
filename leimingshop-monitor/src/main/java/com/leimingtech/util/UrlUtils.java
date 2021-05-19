/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util;

import org.apache.commons.lang3.StringUtils;

public class UrlUtils {

    /**
     * 获取IP地址
     *
     * @param url 例如：http://192.168.0.108/
     * @return
     **/
    public static String getIpAddress(String url) {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("从URL中获取IP地址时，URL不能为空！！");
        }
        String[] urls = url.split(":");
        if (urls.length < 2) {
            throw new RuntimeException("从URL中获取IP地址时，URL输入不合法！！");
        }
        String ipAddress = urls[1];
        ipAddress = ipAddress.replace("//", "/");
        urls = ipAddress.split("/");
        ipAddress = urls[1];
        return ipAddress;
    }
}
