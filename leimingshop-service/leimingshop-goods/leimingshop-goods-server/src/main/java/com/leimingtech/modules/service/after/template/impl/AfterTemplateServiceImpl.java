/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.after.template.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.after.template.AfterTemplateDao;
import com.leimingtech.modules.dto.after.template.AfterTemplateDTO;
import com.leimingtech.modules.dto.after.template.AfterTemplateSaveDTO;
import com.leimingtech.modules.entity.after.template.AfterTemplateEntity;
import com.leimingtech.modules.service.after.template.AfterTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 售后模板
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-17
 */
@Service
public class AfterTemplateServiceImpl extends CrudServiceImpl<AfterTemplateDao, AfterTemplateEntity, AfterTemplateDTO> implements AfterTemplateService {

    @Resource
    private AfterTemplateDao afterTemplateDao;

    @Override
    public QueryWrapper<AfterTemplateEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AfterTemplateEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * @return void
     * @Description 新增售后模板的时候调用这个方法，主要是为了不传id值
     * @Author huangkeyuan
     * @Date 17:49 2019-05-17
     * @Param [dto]
     */
    @Override

    public void saveAfterTemple(@RequestBody AfterTemplateSaveDTO dto) {

        AfterTemplateEntity afterTemplateEntity = ConvertUtils.sourceToTarget(dto, AfterTemplateEntity.class);

        afterTemplateDao.insert(afterTemplateEntity);
    }

    /**
     * 修改售后模板
     *
     * @param dto
     */

    @Override
    public void update(@RequestBody AfterTemplateDTO dto) {
        super.update(dto);
    }

    /**
     * @return com.leimingtech.modules.dto.after.template.AfterTemplateDTO
     * @Description 查询售后模板信息
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 10:52 2019-05-29
     */

    @Override
    public AfterTemplateDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 删除售后模板
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.after.template.AfterTemplateDTO>
     * @Description 查询分页
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 10:58 2019-05-29
     */

    @Override
    public PageData<AfterTemplateDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @return java.util.List<com.leimingtech.modules.dto.after.template.AfterTemplateDTO>
     * @Description 查询列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 10:58 2019-05-29
     */

    @Override
    public List<AfterTemplateDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 功能描述 根据售后模板的名称查询
     *
     * @param afterSaleTemplateName 售后模板名称
     * @return AfterTemplateDTO 售后模板
     * @author lishuo
     * @date 24/6/2020
     */
    @Override

    public AfterTemplateDTO findByName(@RequestParam("afterSaleTemplateName") String afterSaleTemplateName) {
        QueryWrapper<AfterTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", afterSaleTemplateName);
        AfterTemplateEntity afterTemplateEntity = afterTemplateDao.selectOne(queryWrapper);
        AfterTemplateDTO afterTemplateDTO = new AfterTemplateDTO();
        if (afterTemplateEntity != null) {
            BeanUtils.copyProperties(afterTemplateEntity, afterTemplateDTO);
        }
        return afterTemplateDTO;
    }
}
