/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @Author: SWH ab4856812@163.com
 * @Description: 个人中心
 * @Date: 2019/7/16 21:21
 * @Version: V1.0
 */
@Data
@ApiModel(description = "MemberPersonCenterDTO")
public class MemberPersonCenterDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String memberName;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String memberMobile;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;
    /**
     * 用户性别（默认0:保密，1：女，2：男）
     */
    @ApiModelProperty(value = "用户性别 （默认0:保密，1：女，2：男）")
    private Integer memberSex;
    /**
     * 用户生日
     */
    @ApiModelProperty(value = "用户生日")
    private String memberBirthday;
    /**
     * 是否设置密码
     */
    @ApiModelProperty(value = "是否设置密码 0未设置 1已设置")
    private Integer pwdFlag;

    /**
     * 可用余额
     */
    @ApiModelProperty(value = "可用余额")
    private BigDecimal availableBalance;
    /**
     * 是否设置密码
     */
    @ApiModelProperty(value = "是否设置密码 0未设置 1已设置")
    private Integer payPwdFlag;

    /**
     * 地址内容
     */
    @ApiModelProperty(value = "地址内容")
    private String areaInfo;

    /**
     * 待付款数量
     */
    @ApiModelProperty(value = "待付款数量")
    private Integer paymentNum;
    /**
     * 待发货数量
     */
    @ApiModelProperty(value = "待发货数量")
    private Integer shippedNum;
    /**
     * 待收货数量
     */
    @ApiModelProperty(value = "待收货数量")
    private Integer receivingNum;
    /**
     * 待评价数量
     */
    @ApiModelProperty(value = "待评价数量")
    private Integer evaluateNum;
    /**
     * 退还/售后数量
     */
    @ApiModelProperty(value = "退还/售后数量")
    private Integer afterSaleNum;
    /**
     * 全部订单数量
     */
    @ApiModelProperty(value = "全部订单数量")
    private Integer orderNum;
    /**
     * 消息数量
     */
    @ApiModelProperty(value = "消息数量")
    private Integer messageNum;
    /**
     * 收藏数量
     */
    @ApiModelProperty(value = "收藏数量")
    private Integer collectNum;

    /**
     * 足迹数量
     */
    @ApiModelProperty(value = "足迹数量")
    private Integer browseNum;

    /**
     * 优惠券数量
     */
    @ApiModelProperty(value = "优惠券数量")
    private Integer couponNum;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "跟踪信息的描述")
    private String context;

    @ApiModelProperty(value = "商品图片")
    private String goodsPicture;

    @ApiModelProperty(value = "快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回")
    private Integer state;

    @ApiModelProperty(value = "成长值")
    private Integer growthValue;

    @ApiModelProperty(value = "等级名称")
    private String memberGraderName;

    @ApiModelProperty(value = "会员积分")
    private Integer pointValue;

}
