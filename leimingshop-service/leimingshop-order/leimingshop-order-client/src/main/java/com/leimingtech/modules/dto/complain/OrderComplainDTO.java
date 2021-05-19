/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.complain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 订单投诉表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "订单投诉表")
public class OrderComplainDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "会员账号")
    private String account;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "投诉类型1:服务相关,2:物流相关,3:售后相关,4:商品相关5:其他")
    private Integer type;
    @ApiModelProperty(value = "投诉来源 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序 ")
    private Integer source;
    @ApiModelProperty(value = "详细说明")
    private String cause;
    @ApiModelProperty(value = "投诉图片")
    private String picture;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "投诉状态 10：待处理 20：已处理")
    private Integer status;
    @ApiModelProperty(value = "是否展示 0 展示  1 不展示")
    private Integer showFlag;
    @ApiModelProperty(value = "处理人")
    private String disposePerson;
    @ApiModelProperty(value = "处理时间")
    private Date disposeDate;
    @ApiModelProperty(value = "投诉时间")
    private Date createDate;
    @ApiModelProperty(value = "投诉判定 1：有效投诉，2：重点问题，3：无效投诉")
    private Integer verdict;
    @ApiModelProperty(value = "回复内容")
    private String replyContent;
    @ApiModelProperty(value = "处理备注")
    private String remark;
    @ApiModelProperty(value = "评价客服满意度1-2星为差，3-4星为一般，5星为满意")
    private Integer customerSatisfaction;
    @ApiModelProperty(value = "评价客服内容")
    private String customerContent;
}