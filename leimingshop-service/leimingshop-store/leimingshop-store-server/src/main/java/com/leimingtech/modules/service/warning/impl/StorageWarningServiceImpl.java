/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.warning.StorageWarningDao;
import com.leimingtech.modules.dto.warning.SaveStorageWarningDTO;
import com.leimingtech.modules.dto.warning.StorageWarningDTO;
import com.leimingtech.modules.dto.warning.UpdateStorageWarningDTO;
import com.leimingtech.modules.entity.warning.StorageWarningEntity;
import com.leimingtech.modules.service.warning.StorageWarningService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 库存预警表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-12
 */
@Service
public class StorageWarningServiceImpl extends BaseServiceImpl<StorageWarningDao, StorageWarningEntity> implements StorageWarningService {


    private QueryWrapper<StorageWarningEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StorageWarningEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 根据店铺ID 查询预警信息
     *
     * @param storeId
     * @return
     */
    @Override

    public StorageWarningDTO get(@RequestParam("storeId") Long storeId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("store_id", storeId);
        StorageWarningEntity entity = baseDao.selectOne(wrapper);

        return ConvertUtils.sourceToTarget(entity, StorageWarningDTO.class);
    }

    /**
     * 保存预警信息
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody SaveStorageWarningDTO dto) {
        StorageWarningEntity entity = ConvertUtils.sourceToTarget(dto, StorageWarningEntity.class);

        insert(entity);
    }

    /**
     * 修改预警信息
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody UpdateStorageWarningDTO dto) {
        StorageWarningEntity entity = ConvertUtils.sourceToTarget(dto, StorageWarningEntity.class);

        updateById(entity);
    }

}