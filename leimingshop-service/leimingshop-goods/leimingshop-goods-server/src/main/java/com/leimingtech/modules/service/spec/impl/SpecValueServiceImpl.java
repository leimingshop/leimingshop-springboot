/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.spec.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.spec.SpecValueDao;
import com.leimingtech.modules.dto.spec.SpecValueDTO;
import com.leimingtech.modules.entity.spec.SpecValueEntity;
import com.leimingtech.modules.service.spec.SpecValueService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 规格值管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SpecValueServiceImpl extends CrudServiceImpl<SpecValueDao, SpecValueEntity, SpecValueDTO> implements SpecValueService {

    /**
     * 功能描述:
     * 〈规格值条件查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<SpecValueDTO> findSpecValueList(@RequestParam Map<String, Object> params) {
        List<SpecValueEntity> specValueList = baseDao.findSpecValueList(params);
        return ConvertUtils.sourceToTarget(specValueList, SpecValueDTO.class);
    }

    /**
     * 功能描述:
     * 〈规格物理删除〉
     *
     * @param specId 规格id
     * @return : int 删除数量
     * @author : 刘远杰
     */
    @Override

    public int deleteSpecValueBySpecId(Long specId) {
        return baseDao.deleteSpecValueBySpecId(specId);
    }

    /**
     * 功能描述:
     * 〈逻辑删除规格、规格值〉
     *
     * @param specId 规格id
     * @author : 刘远杰
     */

    @Override
    public int logicDeleteSpecValueBySpecId(Long specId) {
        UpdateWrapper<SpecValueEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq(specId != null, "spec_id", specId);
        return baseDao.delete(wrapper);
    }

    /**
     * 功能描述:
     * 〈规格值批量保存〉
     *
     * @param list
     * @return : void
     * @author : 刘远杰
     */

    @Override
    public void insertBatch(@RequestBody List<SpecValueDTO> list) {
        List<SpecValueEntity> specValueEntityList = ConvertUtils.sourceToTarget(list, SpecValueEntity.class);
        this.insertBatch(specValueEntityList);
    }

    @Override
    public QueryWrapper<SpecValueEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String specId = (String) params.get("specId");

        QueryWrapper<SpecValueEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(specId), "spec_id", specId);

        return wrapper;
    }

}
