/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.evaluate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 信誉评价表
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "EvaluateGoodsDTO")
public class EvaluateGoodsDTO implements Serializable {
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
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;
    @ApiModelProperty(value = "商品价格")
    private BigDecimal goodsPrice;
    @ApiModelProperty(value = "评价分数（1-5分）")
    private Integer evaluateScores;
    @ApiModelProperty(value = "信誉评价内容")
    private String evaluateContent;
    @ApiModelProperty(value = "0表示不是 1表示是匿名评价")
    private Integer isAnonymous;
    @ApiModelProperty(value = "店铺编号")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "店铺头像")
    private String storeLogo;
    @ApiModelProperty(value = "评价人编号")
    private Long memberId;
    @ApiModelProperty(value = "评价人名称")
    private String memberName;
    @ApiModelProperty(value = "评价人的头像")
    private String memberAvatar;
    @ApiModelProperty(value = "评价信息的状态 0为正常 1为禁止显示")
    private Integer evaluateState;
    @ApiModelProperty(value = "读取状态 默认0未读 1已读")
    private Integer readState;
    @ApiModelProperty(value = "管理员对评价的处理备注")
    private String evaluateRemark;
    @ApiModelProperty(value = "解释内容")
    private String evaluateExplain;
    @ApiModelProperty(value = "晒单图片")
    private String evaluateImage;
    @ApiModelProperty(value = "商品规格信息")
    private String specInfo;
    @ApiModelProperty(value = "点赞数")
    private Long likeCount;
    @ApiModelProperty(value = "商品规格ID")
    private Long goodsSpecId;
    @ApiModelProperty(value = "浏览数")
    private Long browseNum;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "回复内容")
    private String replyContent;
    @ApiModelProperty(value = "回复时间")
    private Date replyDate;
    @ApiModelProperty(value = "评价人昵称")
    private String nickName;
    @ApiModelProperty(value = "规格主图")
    private String specMainPicture;
    @ApiModelProperty("当前商品评价数量")
    private Integer num;
    @ApiModelProperty(value = "用户等级")
    private String memberGrade;
    @ApiModelProperty(value = "违规删除0未删除1 已删除")
    private Integer illegalDel;
}
