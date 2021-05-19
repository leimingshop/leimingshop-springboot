/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.freight.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constant.order.RedisConstants;
import com.leimingtech.modules.dao.order.freight.FreightRuleDao;
import com.leimingtech.modules.dto.order.freight.FreightRuleDTO;
import com.leimingtech.modules.entity.order.freight.FreightRuleEntity;
import com.leimingtech.modules.enums.order.FreightRuleEnum;
import com.leimingtech.modules.service.order.freight.FreightRuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 运费规则表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-22
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class FreightRuleServiceImpl extends BaseServiceImpl<FreightRuleDao, FreightRuleEntity> implements FreightRuleService {

    @Autowired
    private RedisUtils redisUtils;

    @Override

    public PageData<FreightRuleDTO> page(@RequestParam Map<String, Object> params) {
        IPage<FreightRuleEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, FreightRuleDTO.class);
    }

    @Override

    public List<FreightRuleDTO> list(@RequestParam Map<String, Object> params) {
        List<FreightRuleEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, FreightRuleDTO.class);
    }

    private QueryWrapper<FreightRuleEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<FreightRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public FreightRuleDTO get(Long id) {
        FreightRuleEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, FreightRuleDTO.class);
    }

    /**
     * 功能描述：
     * <查询店铺运费模板>
     *
     * @param storeId
     * @return
     * @date 2020/4/22 15:41
     * @author 刘远杰
     **/
    @Override

    public FreightRuleDTO getStoreFreightRule(@RequestParam("storeId") Long storeId) {
        String key = RedisConstants.FREIGHT_RULE + storeId;

        FreightRuleDTO freightRuleDTO = new FreightRuleDTO();

        Object obj = redisUtils.get(key);
        if (obj == null) {
            QueryWrapper<FreightRuleEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("store_id", storeId);
            FreightRuleEntity entity = baseDao.selectOne(queryWrapper);

            if (entity != null) {
                freightRuleDTO = ConvertUtils.sourceToTarget(entity, FreightRuleDTO.class);
                // 更新缓存
                redisUtils.set(key, freightRuleDTO.getRuleType(), -1);
            } else {
                // 设置默认计算方式
                freightRuleDTO.setStoreId(storeId);
                freightRuleDTO.setRuleType(FreightRuleEnum.RULE_TYPE_CEIL.value());
                // 更新缓存
                redisUtils.set(key, FreightRuleEnum.RULE_TYPE_CEIL.value(), -1);
            }
        } else {
            freightRuleDTO = new FreightRuleDTO();
            freightRuleDTO.setRuleType(Integer.parseInt(obj.toString()));
            freightRuleDTO.setStoreId(storeId);
        }

        return freightRuleDTO;
    }

    @Override

    public void save(@RequestBody FreightRuleDTO dto) {
        FreightRuleEntity entity = ConvertUtils.sourceToTarget(dto, FreightRuleEntity.class);

        insert(entity);
    }

    @Override

    public void update(@RequestBody FreightRuleDTO dto) {
        FreightRuleEntity entity = ConvertUtils.sourceToTarget(dto, FreightRuleEntity.class);

        updateById(entity);
    }

    /**
     * 功能描述：
     * <修改运费规则>
     *
     * @param dto 运费规则修改实体
     * @return
     * @date 2020/4/22 15:36
     * @author 刘远杰
     **/
    @Override

    public void updateFreightRule(@RequestBody FreightRuleDTO dto) {

        // 修改redis运费规则
        String key = RedisConstants.FREIGHT_RULE + dto.getStoreId();
        redisUtils.set(key, dto.getRuleType(), -1);

        UpdateWrapper<FreightRuleEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("store_id", dto.getStoreId());

        FreightRuleEntity entity = ConvertUtils.sourceToTarget(dto, FreightRuleEntity.class);
        baseDao.update(entity, updateWrapper);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {
        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
