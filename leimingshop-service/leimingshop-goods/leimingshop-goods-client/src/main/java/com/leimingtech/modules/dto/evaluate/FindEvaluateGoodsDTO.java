/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.evaluate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 信誉评价表
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "FindEvaluateGoodsDTO")
public class FindEvaluateGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评价主键ID")
    private Long id;
    @ApiModelProperty(value = "订单表ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "订单商品表编号")
    private Long orderGoodsId;
    @ApiModelProperty(value = "商品表编号")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "评价分数（0-1 差评，2-3 中评，4-5 好评）")
    private Integer evaluateScores;
    @ApiModelProperty(value = "信誉评价内容")
    private String evaluateContent;
    @ApiModelProperty(value = "0表示不是 1表示是匿名评价")
    private Integer isAnonymous;
    @ApiModelProperty(value = "店铺编号")
    private Long storeId;
    @ApiModelProperty(value = "回复时间")
    private Date replyDate;

    @ApiModelProperty("店铺logo")
    private String storeLogo;
    @ApiModelProperty(value = "管理员对评价的处理备注")
    private String evaluateRemark;
    @ApiModelProperty(value = "解释内容")
    private String evaluateExplain;
    @ApiModelProperty(value = "晒单图片")
    private String evaluateImage;
    @ApiModelProperty(value = "商品规格信息")
    private String specInfo;
    @ApiModelProperty(value = "商品规格ID")
    private Long goodsSpecId;
    @ApiModelProperty(value = "回复内容")
    private String replyContent;
    @ApiModelProperty(value = "规格主图")
    private String specMainPicture;
    @ApiModelProperty(value = "评价人id")
    private Long memberId;
    @ApiModelProperty(value = "评价人名称")
    private String memberName;
    @ApiModelProperty(value = "评价人的头像")
    private String memberAvatar;
    @ApiModelProperty(value = "用户等级")
    private String memberGrade;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;


}