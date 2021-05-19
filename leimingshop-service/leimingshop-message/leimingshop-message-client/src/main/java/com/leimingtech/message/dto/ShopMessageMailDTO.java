/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ShopMessageMailVO
 * @Description 发送站内信（mq使用）
 * @Author DY
 * @Date 2019/9/24 14:10
 * @Version 1.0
 **/
@Data
@ApiModel(value = "保存站内信（消息队列使用）")
public class ShopMessageMailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "信息类型(0 活动推送 1 购物车内商品降价 2 到货通知 3 退款通知 4 优惠券到期通知 5收藏的商品降价 6发货通知 7 清关失败 8 秒杀活动提醒 9 限量商品提醒 10收藏的店铺上新 11收藏的品牌上新 12 售后审核通过)")
    private Integer messageType;
    @ApiModelProperty(value = "请求id，接收人id")
    private Map<Map<String, Object>, List<Long>> ids;
    @ApiModelProperty(value = "请求id，图片地址")
    private Map<Long, String> imgUrls;
}
