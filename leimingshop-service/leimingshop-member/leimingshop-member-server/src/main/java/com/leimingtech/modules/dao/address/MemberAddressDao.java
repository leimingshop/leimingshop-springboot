/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.address;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.address.MemberAddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员地址表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Mapper
public interface MemberAddressDao extends BaseDao<MemberAddressEntity> {

    /**
     * 修改地址默认状态
     *
     * @param id
     * @param defaultFlag
     */
    void updateDefaultFlag(@Param("id") Long id, @Param("defaultFlag") Integer defaultFlag);

    /**
     * 根据id将会员地址设为非默认状态
     *
     * @param memberId
     * @param defaultFlag
     */
    void updateDefaultFlagByMemberId(@Param("memberId") Long memberId, @Param("defaultFlag") Integer defaultFlag);

    /**
     * 根据id将会员地址上次选中设置为未选中
     *
     * @param memberId     会员ID
     * @param lastSelected 选中状态
     */
    void updateLastSelectedByMemberId(@Param("memberId") Long memberId, @Param("lastSelected") Integer lastSelected);

    /**
     * 根据id将会员地址上次选中设置为未选中
     *
     * @param addressId    地址ID
     * @param lastSelected 选中状态
     */
    void updateSettingSelected(@Param("addressId") Long addressId, @Param("lastSelected") Integer lastSelected);

}