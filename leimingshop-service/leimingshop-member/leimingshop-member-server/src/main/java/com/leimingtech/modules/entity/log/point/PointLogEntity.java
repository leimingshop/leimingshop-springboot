/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.log.point;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分日志 PO
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_point_log")
public class PointLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 用户名称
     */
    private String memberName;
    /**
     * 积分值（正为加，负为减）
     */
    private Integer pointValue;
    /**
     * 积分描述
     */
    private String pointDesc;

    /**
     * 积分/成长值获取类型（1:新手欢迎奖励;2:设置头像;3:设置昵称;4:完善个人信息;5:首次关注店铺;6:首次分享商品;
     * 7:首次收藏商品;8:首次购物成功;9:首次完成评价;10:每日登录;11:每日签到;12:分享商品;13:邀请好友;
     * 14:好友首次下单成功;15:评价;16:更多规则购物消费;17:更多规则商品评价;18:退货进行积分成长值扣减
     */
    private Integer sourceType;

    /**
     * 类型（1:积分;2:成长值）
     */
    private Integer pointType;

    /**
     * 当前积分/成长值
     */
    private Integer currentValue;
}