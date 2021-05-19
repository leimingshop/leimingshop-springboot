/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.warning;


import com.leimingtech.modules.dto.warning.SaveStorageWarningDTO;
import com.leimingtech.modules.dto.warning.StorageWarningDTO;
import com.leimingtech.modules.dto.warning.UpdateStorageWarningDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 库存预警表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-12
 */

public interface StorageWarningService {

    /**
     * 根据店铺ID 查询预警信息
     *
     * @param storeId
     * @return
     */

    StorageWarningDTO get(@RequestParam("storeId") Long storeId);

    /**
     * 保存预警信息
     *
     * @param dto
     */

    void save(@RequestBody SaveStorageWarningDTO dto);

    /**
     * 修改预警信息
     *
     * @param dto
     */

    void update(@RequestBody UpdateStorageWarningDTO dto);

}