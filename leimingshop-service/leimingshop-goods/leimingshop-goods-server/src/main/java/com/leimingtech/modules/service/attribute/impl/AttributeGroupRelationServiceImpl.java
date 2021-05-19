/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attribute.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.attribute.AttributeGroupRelationDao;
import com.leimingtech.modules.dto.attribute.AttributeGroupRelationDTO;
import com.leimingtech.modules.entity.attribute.AttributeGroupRelationEntity;
import com.leimingtech.modules.service.attribute.AttributeGroupRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 属性与属性分组关联分类
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Service("attributeGroupRelationService")
@Transactional(rollbackFor = Exception.class)
public class AttributeGroupRelationServiceImpl extends CrudServiceImpl<AttributeGroupRelationDao,
        AttributeGroupRelationEntity, AttributeGroupRelationDTO> implements AttributeGroupRelationService {

    /**
     * 根据分组id物理删除
     *
     * @param groupId 分组id
     * @author 刘远杰
     * @Description 根据分组id物理删除
     * @Date 2019/5/17 16:43
     */
    @Override
    public int deleteByAttrId(Long groupId) {
        return baseDao.deleteByAttrId(groupId);
    }

    /**
     * @param groupId 属性分组id
     * @author 刘远杰
     * @Description 根据分组id逻辑删除
     * @Date 2019/5/20 16:42
     */
    @Override
    public int logicDeleteByGroupId(Long groupId) {
        UpdateWrapper<AttributeGroupRelationEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq(groupId != null, "group_id", groupId);
        return baseDao.delete(wrapper);
    }

    @Override
    public void insertBatch(List<AttributeGroupRelationDTO> attributeGroupRelationDTOList) {
        List<AttributeGroupRelationEntity> attributeGroupRelationEntities = ConvertUtils.sourceToTarget(attributeGroupRelationDTOList, AttributeGroupRelationEntity.class);
        super.insertBatch(attributeGroupRelationEntities);
    }

    @Override
    public QueryWrapper<AttributeGroupRelationEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String groupId = (String) params.get("groupId");
        String attrId = (String) params.get("attrId");

        QueryWrapper<AttributeGroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(attrId), "attr_id", attrId);
        wrapper.eq(StringUtils.isNotBlank(groupId), "group_id", groupId);

        return wrapper;
    }


}
