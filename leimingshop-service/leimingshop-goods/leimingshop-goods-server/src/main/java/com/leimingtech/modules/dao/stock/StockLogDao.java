/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.stock;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.stock.StockLogDTO;
import com.leimingtech.modules.entity.stock.StockLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 库存修改记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Mapper
public interface StockLogDao extends BaseDao<StockLogEntity> {
    /**
     * 根据规格ID 去查询最近的更近记录
     *
     * @param ids 规格ID
     * @return
     */
    List<StockLogDTO> selectStockLog(@Param("ids") List<Long> ids);

    /**
     * 查询库存记录
     *
     * @param params 查询条件
     * @return 返回库存记录
     */
    List<StockLogDTO> selectLogPage(Map<String, Object> params);

}