/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.invoice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.invoice.OrderInvoiceDTO;
import com.leimingtech.modules.entity.invoice.OrderInvoiceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 订单发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Mapper
public interface OrderInvoiceDao extends BaseDao<OrderInvoiceEntity> {

    List<OrderInvoiceDTO> selectInvoicePage(IPage<OrderInvoiceEntity> page, Map<String, Object> params);
}