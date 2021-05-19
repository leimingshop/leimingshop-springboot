/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.invoice;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.invoice.MemberInvoiceNameDTO;
import com.leimingtech.modules.entity.invoice.MemberInvoiceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-26
 */
@Mapper
public interface MemberInvoiceDao extends BaseDao<MemberInvoiceEntity> {

    /**
     * 获取发票抬头名称列表
     *
     * @param memberId 用户ID
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<MemberInvoiceNameDTO> selectInvoiceNames(Long memberId);
}
