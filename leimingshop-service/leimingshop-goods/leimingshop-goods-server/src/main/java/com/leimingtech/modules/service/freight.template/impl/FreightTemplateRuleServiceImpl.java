/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.freight.template.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.freight.template.FreightTemplateRuleDao;
import com.leimingtech.modules.dto.freight.template.FreightTemplateRuleDTO;
import com.leimingtech.modules.entity.freight.template.FreightTemplateRuleEntity;
import com.leimingtech.modules.service.freight.template.FreightTemplateRuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 运费模板规则管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class FreightTemplateRuleServiceImpl extends BaseServiceImpl<FreightTemplateRuleDao, FreightTemplateRuleEntity> implements FreightTemplateRuleService {

    @Override

    public PageData<FreightTemplateRuleDTO> page(@RequestParam Map<String, Object> params) {
        IPage<FreightTemplateRuleEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, FreightTemplateRuleDTO.class);
    }

    @Override

    public List<FreightTemplateRuleDTO> list(@RequestParam Map<String, Object> params) {
        List<FreightTemplateRuleEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, FreightTemplateRuleDTO.class);
    }

    private QueryWrapper<FreightTemplateRuleEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<FreightTemplateRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public FreightTemplateRuleDTO get(Long id) {
        FreightTemplateRuleEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, FreightTemplateRuleDTO.class);
    }

    /**
     * 功能描述：
     * <根据运费模板id查询模板规则>
     *
     * @param templateId 运费模板id
     * @return
     * @date 2020/4/22 11:12
     * @author 刘远杰
     **/
    @Override

    public List<FreightTemplateRuleDTO> getByTemplateId(@RequestParam("templateId") Long templateId) {
        QueryWrapper<FreightTemplateRuleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        List<FreightTemplateRuleEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, FreightTemplateRuleDTO.class);
    }

    @Override

    public void save(@RequestBody FreightTemplateRuleDTO dto) {
        FreightTemplateRuleEntity entity = ConvertUtils.sourceToTarget(dto, FreightTemplateRuleEntity.class);

        insert(entity);
    }

    /**
     * 功能描述：
     * <批量保存运费模板规则>
     *
     * @param dtoList 运费模板规则集合
     * @return
     * @date 2020/4/21 17:46
     * @author 刘远杰
     **/
    @Override

    public Boolean saveBatch(@RequestBody List<FreightTemplateRuleDTO> dtoList) {
        List<FreightTemplateRuleEntity> entityList = ConvertUtils.sourceToTarget(dtoList, FreightTemplateRuleEntity.class);
        return super.insertBatch(entityList);
    }

    @Override

    public void update(@RequestBody FreightTemplateRuleDTO dto) {
        FreightTemplateRuleEntity entity = ConvertUtils.sourceToTarget(dto, FreightTemplateRuleEntity.class);

        updateById(entity);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {
        // 逻辑删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述：
     * <根据模板id物理删除运费模板规则>
     *
     * @param templateId 运费模板id
     * @return
     * @date 2020/4/22 9:41
     * @author 刘远杰
     **/
    @Override

    public Boolean noLogicDeleteByTemplateId(@RequestParam("templateId") Long templateId) {
        baseDao.noLogicDeleteByTemplateId(templateId);
        return true;
    }

}
