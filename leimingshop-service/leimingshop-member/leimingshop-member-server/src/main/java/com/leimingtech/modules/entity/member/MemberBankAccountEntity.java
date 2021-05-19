/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.member;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户银行账户信息
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_member_bank_account")
public class MemberBankAccountEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 收款账号
     */
    private String bankAccount;
    /**
     * 收款人
     */
    private String bankPeople;
    /**
     * 收款银行
     */
    private String bankName;
    /**
     * 收款人手机号
     */
    private String bankPhone;
}
