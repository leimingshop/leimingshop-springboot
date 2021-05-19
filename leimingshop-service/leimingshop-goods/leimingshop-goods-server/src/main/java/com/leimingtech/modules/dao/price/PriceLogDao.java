/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.price;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.price.PriceLogDTO;
import com.leimingtech.modules.entity.price.PriceLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 价格修改记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Mapper
public interface PriceLogDao extends BaseDao<PriceLogEntity> {
    /**
     * 查询价格记录
     *
     * @param ids 规格ID
     * @return
     */
    List<PriceLogDTO> selectPriceLog(@Param("ids") List<Long> ids);

    /**
     * 查询修改记录
     *
     * @param params 查询参数
     * @return 返回操作记录
     */
    List<PriceLogDTO> selectLogPage(Map<String, Object> params);
}