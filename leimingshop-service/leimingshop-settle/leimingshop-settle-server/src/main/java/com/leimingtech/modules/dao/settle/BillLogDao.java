/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.settle;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.settle.BillLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对账操作记录
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Mapper
public interface BillLogDao extends BaseDao<BillLogEntity> {


}