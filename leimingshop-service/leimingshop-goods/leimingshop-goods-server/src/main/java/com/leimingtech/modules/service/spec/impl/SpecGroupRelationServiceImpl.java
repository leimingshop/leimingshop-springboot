/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.spec.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.spec.SpecGroupRelationDao;
import com.leimingtech.modules.dto.spec.SpecGroupRelationDTO;
import com.leimingtech.modules.entity.spec.SpecGroupRelationEntity;
import com.leimingtech.modules.service.spec.SpecGroupRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 规格与规格分组关联管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SpecGroupRelationServiceImpl extends CrudServiceImpl<SpecGroupRelationDao, SpecGroupRelationEntity,
        SpecGroupRelationDTO> implements SpecGroupRelationService {
    /**
     * 功能描述:
     * 〈根据分组id物理删除〉
     *
     * @param groupId 规格分组id
     * @return : int 删除数量
     * @author : 刘远杰
     */

    @Override
    public int deleteByGroupId(Long groupId) {
        return baseDao.deleteByGroupId(groupId);
    }

    //TODO LX 17839193044@162.com 上下两个方法有什么区别

    /**
     * 功能描述:
     * 〈根据分组id逻辑删除〉
     *
     * @param groupId 规格分组id
     * @return : int 删除数量
     * @author : 刘远杰
     */

    @Override
    public int logicDeleteByGroupId(Long groupId) {
        UpdateWrapper<SpecGroupRelationEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq(groupId != null, "group_id", groupId);
        return baseDao.delete(wrapper);
    }

    /**
     * 功能描述:
     * 〈批量保存〉
     *
     * @param dtoList
     * @return : boolean
     * @author : 刘远杰
     */

    @Override
    public boolean insertBatch(@RequestBody List<SpecGroupRelationDTO> dtoList) {
        List<SpecGroupRelationEntity> entityList = ConvertUtils.sourceToTarget(dtoList, SpecGroupRelationEntity.class);
        return this.insertBatch(entityList, 100);
    }

    /**
     * 功能描述:
     * 〈规格关联条件查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<SpecGroupRelationDTO> list(@RequestParam Map<String, Object> params) {
        List<SpecGroupRelationEntity> entityList = this.baseDao.selectList(this.getWrapper(params));
        return ConvertUtils.sourceToTarget(entityList, this.currentDtoClass());
    }

    @Override
    public QueryWrapper<SpecGroupRelationEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String specId = (String) params.get("specId");
        String groupId = (String) params.get("groupId");

        QueryWrapper<SpecGroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(specId), "spec_id", specId);
        wrapper.eq(StringUtils.isNotBlank(groupId), "group_id", groupId);

        return wrapper;
    }


}
