/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 站内信内容表
 *
 * @author *** ***@leimingtech.com
 * @since v1.0.0 2019-08-23
 */
@Data
@ApiModel(value = "站内信内容表 ")
public class ShopMessageTextDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "短消息标题")
    private String messageTitle;
    @ApiModelProperty(value = "消息内容")
    private String messageContent;
    @ApiModelProperty(value = "信息类型(0 活动推送 1 购物车内商品降价 2 到货通知 3 退款通知 4 优惠券到期通知 5收藏的商品降价 6发货通知 7 清关失败 8 秒杀活动提醒 9 限量商品提醒 10收藏的店铺上新 11收藏的品牌上新 12 售后审核通过)")
    private Integer messageType;
    @ApiModelProperty(value = "接受者")
    private String receiver;
    @ApiModelProperty(value = "发送信息时间")
    private Date sendTime;
    @ApiModelProperty(value = "发送方式(0:站内信;1友盟;2短信)")
    private Integer sendMode;
    @ApiModelProperty(value = "接受类型 0 全部 1 指定用户")
    private Integer receiverPeople;
    @ApiModelProperty(value = "发送消息数量")
    private Integer messageCount;
    @ApiModelProperty(value = "发送者")
    private String creator;
    private String receiverType;
    /**
     * 创建时间
     */

    private Date createDate;
    /**
     * 更新人
     */

    private String updater;
    /**
     * 更新时间
     */

    private Date updateDate;
    /**
     * 删除标记（默认为0未删除，1已删除）
     */

    private Integer delFlag;
    /**
     * 乐观锁
     */

    private Long version;

}
