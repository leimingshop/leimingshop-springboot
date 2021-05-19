/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.umeng;


import org.springframework.web.bind.annotation.RequestParam;

/**
 * 站内信内容表
 *
 * @author *** ***@leimingtech.com
 * @since v1.0.0 2019-08-23
 */

public interface ShopMessageTextService {

    /**
     * mq保存站内信
     *
     * @param jsonString 消息内容
     */

    void saveMsg(@RequestParam("jsonString") String jsonString);


}
