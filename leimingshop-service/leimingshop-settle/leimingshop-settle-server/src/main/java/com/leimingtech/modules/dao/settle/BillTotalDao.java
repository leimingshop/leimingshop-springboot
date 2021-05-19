/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.settle;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.settle.BillTotalEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * 对账汇总表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Mapper
public interface BillTotalDao extends BaseDao<BillTotalEntity> {

    /**
     * 获取记录中最大的对账日期
     *
     * @return
     */
    Date findMaxDate();

}