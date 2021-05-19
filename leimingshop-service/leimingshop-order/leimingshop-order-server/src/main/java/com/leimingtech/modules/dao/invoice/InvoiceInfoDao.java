/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.invoice;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.invoice.InvoiceInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 发票信息表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Mapper
public interface InvoiceInfoDao extends BaseDao<InvoiceInfoEntity> {

    List<Long> selectGoodsIdsByInvoiceId(Long id);
}