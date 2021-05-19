/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.store;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.store.StoreInvoiceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺发票设置表
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-27
 */
@Mapper
public interface StoreInvoiceDao extends BaseDao<StoreInvoiceEntity> {

}