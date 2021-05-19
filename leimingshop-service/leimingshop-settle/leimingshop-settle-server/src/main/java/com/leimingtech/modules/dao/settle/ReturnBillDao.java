/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.settle;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.settle.ReturnBillEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退货结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Mapper
public interface ReturnBillDao extends BaseDao<ReturnBillEntity> {

}