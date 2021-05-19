/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting.reward.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 基础规则设置DTO
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 18:18
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "BaseRuleSettingDTO")
public class BaseRuleSettingDTO implements Serializable {
    private static final long serialVersionUID = 13487425323829864L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "购物消费")
    private Integer shoppingConsumption;

    @ApiModelProperty(value = "订单实际支付金额低于*不送成长值/积分")
    private Integer minOrderPay;

    @ApiModelProperty(value = "单件商品最高可获得*成长值/积分")
    private Integer maxPiecesGoods;

    @ApiModelProperty(value = "评价大于*字")
    private Integer evaluationWordCount;

    @ApiModelProperty(value = "评价大于*字奖励的成长值/积分")
    private Integer evaluationWordGrowth;

    @ApiModelProperty(value = "评价图片大于*张")
    private Integer evaluationPicCount;

    @ApiModelProperty(value = "评价图片大于*张奖励的成长值/积分")
    private Integer evaluationPicGrowth;

    @ApiModelProperty(value = "商品单价低于*元评价不送成长值/积分")
    private Integer goodsPriceMinGrowth;
}
