/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public abstract class AbstractBuilder {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 构造回复微信消息
     *
     * @param content   消息内容
     * @param wxMessage 参数
     * @param service   微信公众号API的Service
     * @return XML消息
     * @author xuzhch
     * @date 2020年09月18日
     */
    public abstract WxMpXmlOutMessage build(String content,
                                            WxMpXmlMessage wxMessage, WxMpService service);
}
