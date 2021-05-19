/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.flashsale;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityPageDTO;
import com.leimingtech.modules.entity.flashsale.FlashSaleActivityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 限时抢购活动表
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020-10-15
 */
@Mapper
public interface FlashSaleActivityDao extends BaseDao<FlashSaleActivityEntity> {


    /**
     * 限时抢购管理列表
     *
     * @param params 查询条件
     * @return list 查询结果
     * @author xuzhch
     * @date 2020年10月16日
     */
    List<FlashSaleActivityPageDTO> managePage(@Param("params") Map<String, Object> params);

    void increaseBrowserNum(@Param("ids") List<Long> ids);

    int increaseOrderNum(@Param("ids") List<Long> ids);

}
