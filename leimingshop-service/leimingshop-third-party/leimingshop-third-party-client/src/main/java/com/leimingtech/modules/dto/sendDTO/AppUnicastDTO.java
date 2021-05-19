/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.sendDTO;

import lombok.Data;

import java.util.Map;

/**
 * @Author tyl
 * @Date 2019/8/28 16:37
 * @Description APP 推送实体
 * @update 2020/04/20 增加注释
 **/
@Data
public class AppUnicastDTO {

    /**
     * 友盟标识，相当于用户标识，在用户登录时候App存放到header中传递给后台，后台进行保存。
     */
    private String deviceToken;
    /**
     * 友盟来源（1：安卓，2：ios）
     */
    private Integer umengSource;
    /**
     * 消息标题  存放在sys_message_template的umeng_title字段
     */
    private String title;

    /**
     * 消息内容  替换占位符后的消息内容
     */
    private String text;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 消息类型   0、1代表站内信，跳转到站内信详情 ，其他的跳转到指定页面
     */
    private String messageType;


    private Map<String, String> params;


    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单金额
     */
    private String orderMoney;

    /**
     * 售后单号
     */
    private String aftersaleSn;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品skuid
     */
    private String specId;

    /**
     * 店铺id
     */
    private String storeId;

    /**
     * 品牌id
     */
    private String brandId;

    /**
     * 售后处使用(退款通知使用：取消订单退款：0 ,售后退款：1)
     */
    private String type;

    /**
     * 消息id  messageType为0、1时跳转到站内信详情的ID
     */
    private String messageId;

}
