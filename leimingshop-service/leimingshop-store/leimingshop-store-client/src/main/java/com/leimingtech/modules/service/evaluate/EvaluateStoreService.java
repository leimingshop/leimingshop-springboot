/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.evaluate;


import com.leimingtech.modules.dto.evaluate.FindEvaluateStoreDTO;
import com.leimingtech.modules.dto.evaluate.SaveEvaluateStoreDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 店铺评分管理
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-15
 */

public interface EvaluateStoreService {
    /**
     * 获取点前店铺的动态评分
     *
     * @param storeId 店铺ID
     * @return
     */

    FindEvaluateStoreDTO findAvgPoint(@RequestParam("storeId") Long storeId);

    /**
     * 新增店铺评价
     *
     * @param dto 店铺评价实体类
     */

    void saveEvaluateStore(@RequestBody SaveEvaluateStoreDTO dto);
}