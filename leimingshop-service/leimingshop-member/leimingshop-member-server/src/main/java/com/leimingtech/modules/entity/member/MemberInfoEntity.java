/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.member;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会员详细信息表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_member_info")
public class MemberInfoEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long memberId;

    /**
     * 支付密码
     */
    private String paymentPasswd;
    /**
     * 可用余额
     */
    private BigDecimal availableBalance;
    /**
     * 冻结余额
     */
    private BigDecimal blockedBalance;

    /**
     * 可提现余额
     */
    private BigDecimal availableWithdrawal;

    /**
     * 用户消费积分
     */
    private Integer consumePoint;

    /**
     * 用户可用积分
     */
    private Integer availablePoint;

    /**
     * 用户等级积分
     */
    private Integer gradePoint;

    /**
     * 地区ID
     */
    private String memberAreaid;

    /**
     * 城市ID
     */
    private String memberCityid;

    /**
     * 省份ID
     */
    private String memberProvinceid;

    /**
     * 街道ID
     */
    private String stressId;

    /**
     * 地区内容
     */
    private String memberAreainfo;

    // TODO: 2020/7/8/008 xuzhch  先注释成长值字段
//	/**
//	 * 用户成长值
//	 */
//	private Integer growthValue;
}
