/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.member;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.member.MemberBankAccountEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户银行账户信息
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-21
 */
@Mapper
public interface MemberBankAccountDao extends BaseDao<MemberBankAccountEntity> {

}
