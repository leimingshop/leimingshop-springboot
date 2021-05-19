/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.balance;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户余额明细表
 *
 * @author xuzhch kuangweiguo@leimingtech.com
 * @since v1.0.0 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_member_balance_log")
public class MemberBalanceLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 用户名
     */
    private String memberName;
    /**
     * 当前余额
     */
    private BigDecimal currentBalance;
    /**
     * 变动前余额
     */
    private BigDecimal beforeBalance;
    /**
     * 余额变动（正为加，负为减）
     */
    private BigDecimal changeBalance;
    /**
     * 余额类型(1:运营端调整2:用户购买下单3:订单退款4:用户充值5:商家返利)
     */
    private Integer balanceType;
    /**
     * 备注
     */
    private String remark;
}