/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wechat;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微信签名获取
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/3/17 16:54
 **/

public interface WxSignatureService {

    /**
     * 获取微信签名
     *
     * @param url:
     * @return 微信签名实体
     * @date 2020/3/17 17:00
     * @author lixiangx@leimingtech.com
     **/

    JSONObject getWxSinature(@RequestParam("url") String url);
}
