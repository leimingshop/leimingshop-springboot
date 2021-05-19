/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.reduce.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.reduce.ReduceRuleDao;
import com.leimingtech.modules.dto.reduce.ReduceRuleDTO;
import com.leimingtech.modules.dto.reduce.ReduceRuleSaveDTO;
import com.leimingtech.modules.entity.reduce.ReduceRuleEntity;
import com.leimingtech.modules.service.reduce.ReduceRuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 满减活动规则管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class ReduceRuleServiceImpl extends BaseServiceImpl<ReduceRuleDao, ReduceRuleEntity> implements ReduceRuleService {

    @Override

    public PageData<ReduceRuleDTO> page(@RequestParam Map<String, Object> params) {
        IPage<ReduceRuleEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ReduceRuleDTO.class);
    }

    @Override

    public List<ReduceRuleDTO> list(@RequestParam Map<String, Object> params) {
        List<ReduceRuleEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ReduceRuleDTO.class);
    }

    private QueryWrapper<ReduceRuleEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String limitAmount = (String) params.get("limitAmount");
        String ruleType = (String) params.get("ruleType");

        QueryWrapper<ReduceRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(ruleType), "rule_type", ruleType);
        wrapper.orderByDesc("limit_amount", limitAmount);
        return wrapper;
    }

    @Override

    public ReduceRuleDTO get(Long id) {
        ReduceRuleEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ReduceRuleDTO.class);
    }

    @Override

    public void save(@RequestBody ReduceRuleDTO dto) {
        ReduceRuleEntity entity = ConvertUtils.sourceToTarget(dto, ReduceRuleEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody ReduceRuleDTO dto) {
        ReduceRuleEntity entity = ConvertUtils.sourceToTarget(dto, ReduceRuleEntity.class);

        updateById(entity);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * @Author weixianchun
     * @Description 批量保存满减规则信息
     * @Param dtoList
     * @Date 2019/12/26 18:45
     * @Return java.lang.Boolean
     * @version 1.0
     */

    @Override
    public Boolean saveBatch(@RequestBody List<ReduceRuleSaveDTO> dtoList) {
        List<ReduceRuleEntity> entityList = ConvertUtils.sourceToTarget(dtoList, ReduceRuleEntity.class);
        return super.insertBatch(entityList);
    }

    /**
     * 功能描述:
     * 〈获取满减活动关联的规则集合〉
     *
     * @param activityId 满减活动id
     * @author : 刘远杰
     */

    @Override
    public List<ReduceRuleDTO> getByActivityId(Long activityId) {
        QueryWrapper<ReduceRuleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId).orderByDesc("reduce_amount");
        List<ReduceRuleEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, ReduceRuleDTO.class);
    }

    /**
     * @Author weixianchun
     * @Description 根据活动ID删除商品关联信息
     * @Param activityId
     * @Date 2019/12/26 14:11
     * @Return void
     * @version 1.0
     */

    @Override
    public void deleteByActivityId(@RequestParam("activityId") Long activityId) {
        UpdateWrapper<ReduceRuleEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activity_id", activityId);
        baseDao.delete(updateWrapper);
    }
}
