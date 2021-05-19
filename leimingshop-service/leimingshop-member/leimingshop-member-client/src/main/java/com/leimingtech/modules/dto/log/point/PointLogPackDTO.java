/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.log.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


/**
 * 积分日志封装DTO （Service传输使用）
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-24
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PointLogPackDTO implements Serializable {

    private static final long serialVersionUID = 496714839414892478L;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 积分值
     */
    private Integer pointValue;

    /**
     * 成长值
     */
    private Integer growthValue;

    /**
     * 积分/成长值描述
     */
    private String pointDesc;

    /**
     * 积分/成长值获取类型（1:新手欢迎奖励;2:设置头像;3:设置昵称;4:完善个人信息;5:首次关注店铺;6:首次分享商品;7:首次收藏商品;8:首次购物成功;9:首次完成评价;10:每日登录;11:每日签到;12:分享商品;13:邀请好友;14:好友首次下单成功;15:评价
     */
    private Integer sourceType;

    /**
     * 是否增加积分/成长值 0全部增加 1只增加积分 2只增加成长值
     */
    private Integer status;
}
