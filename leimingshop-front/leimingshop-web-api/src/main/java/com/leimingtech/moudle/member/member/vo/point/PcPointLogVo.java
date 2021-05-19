/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.member.vo.point;

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
public class PcPointLogVo implements Serializable {
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
    @ApiModelProperty(value = "积分/成长值获取类型（1:新手欢迎奖励;2:设置头像;3:设置昵称;4:完善个人信息;5:首次关注店铺;6:首次分享商品;7:首次收藏商品;8:首次购物成功;9:首次完成评价;10:每日登录;11:每日签到;12:分享商品;13:邀请好友;14:好友首次下单成功;15:评价）")
    private Integer sourceType;
    @ApiModelProperty(value = "当前积分/成长值")
    private Integer currentValue;
    @ApiModelProperty(value = "类型（1:积分;2:成长值）")
    private Integer pointType;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "用户消费积分")
    private Integer consumePoint;
    /**
     * 用户可用积分
     */
    @ApiModelProperty(value = "用户可用积分")
    private Integer availablePoint;
}
