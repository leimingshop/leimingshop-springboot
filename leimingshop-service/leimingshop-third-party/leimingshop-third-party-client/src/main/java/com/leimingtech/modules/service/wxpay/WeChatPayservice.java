/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.dto.wxpay.H5WXPayDTO;
import com.leimingtech.modules.dto.wxpay.WXPayResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 功能描述：
 * <微信支付>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/5/30 16:29
 **/

public interface WeChatPayservice {


    /**
     * @param params:
     * @return java.lang.String
     * @Description 查询微信支付结果
     * @Author huangkeyuan
     * @Date 20:23 2019-12-05
     */

    String orderquery(@RequestParam("params") String params) throws Exception;

    /**
     * @param code: 微信小程序获取到的code
     * @return java.lang.String
     * @Description 微信小程序根据code获取openid
     * @Author huangkeyuan
     * @Date 15:01 2019-12-27
     */

    JSONObject getOpenid(String code);

    /**
     * 获取预支付id并再次签名
     *
     * @param h5WXPayDTO
     * @return
     * @date 2020-01-02 15:34
     * @author huangkeyuan@leimingtech.com
     **/

    WXPayResponseDTO wechatPay(@RequestBody H5WXPayDTO h5WXPayDTO) throws Exception;
}
