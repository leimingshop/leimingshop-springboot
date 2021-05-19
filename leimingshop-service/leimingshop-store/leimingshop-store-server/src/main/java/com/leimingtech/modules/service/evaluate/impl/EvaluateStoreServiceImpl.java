/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.evaluate.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.evaluate.EvaluateStoreDao;
import com.leimingtech.modules.dto.evaluate.EvaluateStoreDTO;
import com.leimingtech.modules.dto.evaluate.FindEvaluateStoreDTO;
import com.leimingtech.modules.dto.evaluate.SaveEvaluateStoreDTO;
import com.leimingtech.modules.entity.evaluate.EvaluateStoreEntity;
import com.leimingtech.modules.service.evaluate.EvaluateStoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 店铺评分管理
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-15
 */
@Service
public class EvaluateStoreServiceImpl extends CrudServiceImpl<EvaluateStoreDao, EvaluateStoreEntity, EvaluateStoreDTO> implements EvaluateStoreService {

    @Resource
    private EvaluateStoreDao evaluateStoreDao;


    /**
     * 分页查询条件构造器
     *
     * @param params 分页参数
     * @return
     */
    @Override
    public QueryWrapper<EvaluateStoreEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<EvaluateStoreEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 动态获取店铺的评价信息
     *
     * @param storeId 店铺ID
     * @return
     */
    @Override

    public FindEvaluateStoreDTO findAvgPoint(@RequestParam("storeId") Long storeId) {

        return evaluateStoreDao.findAvgPoint(storeId);
    }

    /**
     * 保存店铺评分
     *
     * @param dto 店铺评价实体类
     */
    @Override

    public void saveEvaluateStore(@RequestBody SaveEvaluateStoreDTO dto) {
        EvaluateStoreEntity evaluateStoreEntity = ConvertUtils.sourceToTarget(dto, EvaluateStoreEntity.class);
        evaluateStoreDao.insert(evaluateStoreEntity);
    }
}