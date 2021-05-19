/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.evaluate;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.evaluate.FindEvaluateStoreDTO;
import com.leimingtech.modules.entity.evaluate.EvaluateStoreEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺评分管理
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-15
 */
@Mapper
public interface EvaluateStoreDao extends BaseDao<EvaluateStoreEntity> {
    /**
     * 动态获取店铺的动态评分
     *
     * @param storeId
     * @return 返回店铺评价信息
     */
    FindEvaluateStoreDTO findAvgPoint(long storeId);
}