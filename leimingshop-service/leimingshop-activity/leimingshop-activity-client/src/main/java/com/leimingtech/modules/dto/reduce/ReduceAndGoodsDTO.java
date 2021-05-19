/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.reduce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 满减活动,商品,规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@ApiModel(description = "ReduceAndGoodsDTO")
public class ReduceAndGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动ID")
    private Long id;
    @ApiModelProperty(value = "满减活动名称")
    private String activityName;
    @ApiModelProperty(value = "活动范围 0平台 1店铺")
    private Integer activityScope;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;
    @ApiModelProperty(value = "活动规则 0普通满减 1每满减 2阶梯满减")
    private Integer ruleType;
    @ApiModelProperty(value = "活动开始时间")
    private Date startDate;
    @ApiModelProperty(value = "活动结束时间")
    private Date endDate;
    @ApiModelProperty(value = "审核状态 0未审核 1审核通过 2审核未通过")
    private Integer auditState;
    @ApiModelProperty(value = "优惠券是否可用 0不可用 1可用")
    private Integer couponsFlag;

    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;

    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;

    @ApiModelProperty(value = "使用条件金额")
    private BigDecimal limitAmount;
    @ApiModelProperty(value = "折扣金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty("满减活动关联商品集合")
    private List<ReduceGoodsDTO> reduceGoodsDTOList;

    @ApiModelProperty("满减规则集合")
    private List<ReduceRuleSaveDTO> reduceRuleSaveDTOList;

    @ApiModelProperty(value = "积分是否可用 0不可用 1可用")
    private Integer pointFlag;
    @ApiModelProperty(value = "余额是否可用 0不可用 1可用")
    private Integer balanceFlag;
    @ApiModelProperty(value = "秒杀活动是否可用 0不可用 1可用")
    private Integer seckillFlag;
    // TODO: 2019/12/26  本期不做
   /* @ApiModelProperty(value = "会员等级id")
    private Long memberGraderId;
    @ApiModelProperty(value = "会员等级名称")
    private String memberGraderName;

    */
}
