/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.log.point;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 积分日志DTO
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-24
 */
@Data
@ApiModel(description = "PointLogDTO")
public class PointLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    @ApiModelProperty(value = "用户名称")
    private String memberName;
    @ApiModelProperty(value = "积分值（正为加，负为减）")
    private Integer pointValue;
    @ApiModelProperty(value = "积分描述")
    private String pointDesc;
    @ApiModelProperty(value = "积分/成长值获取类型（1:新手欢迎奖励;2:设置头像;3:设置昵称;4:完善个人信息;5:首次关注店铺;" +
            "6:首次分享商品;7:首次收藏商品;8:首次购物成功;9:首次完成评价;10:每日登录;11:每日签到;12:分享商品;13:邀请好友;" +
            "14:好友首次下单成功;15:评价;16:更多规则购物消费;17:更多规则商品评价;18:退货进行积分成长值扣减）")
    private Integer sourceType;
    @ApiModelProperty(value = "当前积分/成长值")
    private Integer currentValue;
    @ApiModelProperty(value = "类型（1:积分;2:成长值）")
    private Integer pointType;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}