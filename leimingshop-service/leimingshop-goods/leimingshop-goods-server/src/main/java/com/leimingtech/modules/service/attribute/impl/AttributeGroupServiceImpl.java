/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attribute.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constants.attribute.AttrResultCodeConstants;
import com.leimingtech.modules.dao.attribute.AttributeGroupDao;
import com.leimingtech.modules.dto.attribute.*;
import com.leimingtech.modules.entity.attribute.AttributeEntity;
import com.leimingtech.modules.entity.attribute.AttributeGroupEntity;
import com.leimingtech.modules.service.attribute.AttributeGroupRelationService;
import com.leimingtech.modules.service.attribute.AttributeGroupService;
import com.leimingtech.modules.service.attribute.AttributeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 属性分组管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Transactional(rollbackFor = Exception.class)
@Service

public class AttributeGroupServiceImpl extends CrudServiceImpl<AttributeGroupDao, AttributeGroupEntity, AttributeGroupDTO>
        implements AttributeGroupService {

    @Autowired
    private AttributeGroupRelationService attributeGroupRelationService;
    @Autowired
    private AttributeService attributeService;

    /**
     * @param params 条件
     * @author 刘远杰
     * @Description 属性分组分页查询
     * @Date 2019/5/20 16:12
     */

    @Override
    public PageData<AttributeGroupDTO> page(@RequestParam Map<String, Object> params) {
        IPage<AttributeGroupEntity> iPage = this.getPage(params, Constant.CREATE_DATE, false);
        IPage<AttributeGroupEntity> page = this.baseDao.selectPage(iPage, this.getWrapper(params));
        return this.getPageData(page, this.currentDtoClass());
    }

    /**
     * 功能描述:
     * 〈查询属性分组及属性列表〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */
    @Override

    public List<AttrGroupAndAttrDTO> listAttr(@RequestParam Map<String, Object> params) {
        List<AttrGroupAndAttrDTO> attrGroupAndAttrDTOList = new ArrayList<>();

        String groupName = (String) params.get("groupName");


        QueryWrapper<AttributeGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(groupName), "group_name", groupName);
        wrapper.orderByDesc("sort");

        //查询
        List<AttributeGroupEntity> attributeGroupEntityList = baseDao.selectList(wrapper);

        if (CollectionUtils.isNotEmpty(attributeGroupEntityList)) {
            for (AttributeGroupEntity attributeGroupEntity : attributeGroupEntityList) {
                AttrGroupAndAttrDTO attrGroupAndAttrDTO = getAttrGroupAndAttrDTO(attributeGroupEntity);

                if (CollectionUtils.isNotEmpty(attrGroupAndAttrDTO.getAttrDTOList())) {
                    attrGroupAndAttrDTOList.add(attrGroupAndAttrDTO);
                }
            }
        }
        return attrGroupAndAttrDTOList;
    }

    /**
     * @param dto 实体
     * @author 刘远杰
     * @Description 保存属性分组数据
     * @Data 2019/5/20 16:39
     */

    @Override
    public Map<String, Object> saveAttrGroup(@RequestBody InsertAttributeGroupDTO dto) {
        // 校验分组名称
        Map<String, Object> map = this.checkAttrGroupNameWhenAdd(dto.getGroupName());
        Object code = map.get("code");
        if ((int) code == AttrResultCodeConstants.SUCCESS) {
            // 保存属性分组
            AttributeGroupEntity attributeGroupEntity = ConvertUtils.sourceToTarget(dto, AttributeGroupEntity.class);
            Long[] attrIds = dto.getAttrIds();
            baseDao.insert(attributeGroupEntity);
            // 批量保存属性分组关联内容
            List<AttributeGroupRelationDTO> attributeGroupRelationDTOList = new ArrayList<>();
            // 获得关联表保存对象
            getAttrGroupRelation(attrIds, attributeGroupRelationDTOList, attributeGroupEntity.getId());
            attributeGroupRelationService.insertBatch(attributeGroupRelationDTOList);
            map.put("message", "保存属性分组成功");
        }
        return map;
    }

    /**
     * @param dto 实体
     * @author 刘远杰
     * @Description 修改属性分组数据
     * @Date 2019/5/20 16:39
     */

    @Override
    public Map<String, Object> updateAttrGroup(@RequestBody UpdateAttributeGroupDTO dto) {
        Map<String, Object> map = new HashMap<>(10);
        map.put("code", AttrResultCodeConstants.SUCCESS);
        map.put("message", "输入属性分组名称不合法");
        if (StringUtils.isNotBlank(dto.getGroupName())) {
            // 校验分组名称重复
            map = this.checkAttrGroupNameWhenUpdate(dto.getGroupName(), dto.getId());
        }

        // 分组名称不重复，或者未填
        Object code = map.get("code");
        if ((int) code == AttrResultCodeConstants.SUCCESS) {
            // 修改属性分组
            AttributeGroupDTO attributeGroupDTO = ConvertUtils.sourceToTarget(dto, AttributeGroupDTO.class);
            Long[] attrIds = dto.getAttrIds();
            this.update(attributeGroupDTO);
            // 批量保存规属性分组关联内容
            List<AttributeGroupRelationDTO> attributeGroupRelationDTOList = new ArrayList<>();
            // 获得关联表保存对象
            getAttrGroupRelation(attrIds, attributeGroupRelationDTOList, dto.getId());
            // 删除原关联属性
            attributeGroupRelationService.deleteByAttrId(dto.getId());
            attributeGroupRelationService.insertBatch(attributeGroupRelationDTOList);
            map.put("message", "修改属性分组成功");
        }
        return map;
    }

    /**
     * @param id
     * @author 刘远杰
     * @Description 查询属性分组详情及关联属性
     * @Date 2019/5/20 17:17
     */

    @Override
    public AttrGroupAndAttrDTO findAttrGroupAndAttr(Long id) {
        // 属性详情查询
        AttributeGroupEntity attributeGroupEntity = baseDao.selectById(id);
        if (attributeGroupEntity == null) {
            return null;
        }
        return getAttrGroupAndAttrDTO(attributeGroupEntity);
    }

    /**
     * 功能描述:
     * 〈获取分组id下关联的所有属性〉
     *
     * @param attributeGroupEntity 属性分组实体
     * @author : 刘远杰
     */
    private AttrGroupAndAttrDTO getAttrGroupAndAttrDTO(AttributeGroupEntity attributeGroupEntity) {
        AttrGroupAndAttrDTO attrGroupAndAttrDTO = ConvertUtils.sourceToTarget(attributeGroupEntity, AttrGroupAndAttrDTO.class);
        Map<String, Object> params = new HashMap<>(10);
        params.put("groupId", attributeGroupEntity.getId().toString());
        // 关联属性id
        List<AttributeGroupRelationDTO> list = attributeGroupRelationService.list(params);
        if (CollectionUtils.isNotEmpty(list)) {
            QueryWrapper<AttributeEntity> wrapper = new QueryWrapper<>();
            ArrayList<Long> ids = new ArrayList<>();
            for (AttributeGroupRelationDTO attributeGroupRelationDTO : list) {
                ids.add(attributeGroupRelationDTO.getAttrId());
            }
            wrapper.in("id", ids);
            // 查询属性详情
            List<AttributeDTO> attributeDTOList = attributeService.selectBatchByids(ids);
            if (CollectionUtils.isNotEmpty(attributeDTOList)) {
                attrGroupAndAttrDTO.setAttrDTOList(attributeDTOList);
                attrGroupAndAttrDTO.setLableNum(attributeDTOList.size());
            } else {
                attrGroupAndAttrDTO.setLableNum(0);
            }
        }

        return attrGroupAndAttrDTO;
    }

    /**
     * @param attrGroupName 属性分组名称
     * @author 刘远杰
     * @Description 修改属性分组校验属性分组名称重复性
     * @Date 2019/5/20 16:19
     */

    @Override
    public Map<String, Object> checkAttrGroupNameWhenAdd(@RequestParam("attrGroupName") String attrGroupName) {
        Map<String, Object> map = new HashMap<>(10);
        if (StringUtils.isBlank(StringUtils.trim(attrGroupName))) {
            map.put("code", AttrResultCodeConstants.ERR_INV_PARAMS);
            map.put("message", "输入属性分组名称不合法");
        } else {
            QueryWrapper<AttributeGroupEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("group_name", attrGroupName);
            List<AttributeGroupEntity> attrGroupEntities = baseDao.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(attrGroupEntities)) {
                map.put("code", AttrResultCodeConstants.ERR_REPEAT);
                map.put("message", "属性分组名称已存在");
            } else {
                map.put("code", AttrResultCodeConstants.SUCCESS);
                map.put("message", "属性分组名称可用");
            }
        }
        return map;
    }

    /**
     * @param attrGroupName 属性分组名称
     * @param id            属性分组id
     * @author 刘远杰
     * @Description 修改属性分组校验属性分组名称重复性
     * @Date 2019/5/20 16:20
     */

    @Override
    public Map<String, Object> checkAttrGroupNameWhenUpdate(@RequestParam("arrtGroupName") String attrGroupName, @RequestParam("id") Long id) {
        Map<String, Object> map = new HashMap<>(10);
        AttributeGroupEntity attributeGroupEntity = baseDao.selectById(id);
        if (attributeGroupEntity == null) {
            // 传入id是否正确
            map.put("code", AttrResultCodeConstants.ERR_NO_RESULT);
            map.put("message", "属性分组不存在");
        } else {
            if (StringUtils.isBlank(StringUtils.trim(attrGroupName))) {
                map.put("code", AttrResultCodeConstants.ERR_INV_PARAMS);
                map.put("message", "输入属性分组名称不合法");
            } else {
                QueryWrapper<AttributeGroupEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("group_name", attrGroupName);
                wrapper.ne("id", id);
                List<AttributeGroupEntity> attrGroupEntities = baseDao.selectList(wrapper);
                if (CollectionUtils.isNotEmpty(attrGroupEntities)) {
                    map.put("code", AttrResultCodeConstants.ERR_REPEAT);
                    map.put("message", "属性分组名称已存在");
                } else {
                    map.put("code", AttrResultCodeConstants.SUCCESS);
                    map.put("message", "属性分组名称可用");
                }
            }
        }
        return map;

    }

    /**
     * @param id
     * @author 刘远杰
     * @Description 删除属性分组
     * @Date 2019/5/20 17:40
     */

    @Override
    public int deleteAttrGroup(Long id) {
        // 删除分组
        int count = baseDao.deleteById(id);
        // 删除分组关联内容
        attributeGroupRelationService.logicDeleteByGroupId(id);
        return count;
    }

    /**
     * @param ids 属性分组数组
     * @author 刘远杰
     * @Description 删除属性分组
     * @Date 2019/5/20 17:40
     */

    @Override
    public int deleteAttrGroupBatch(@RequestBody Long[] ids) {
        // 删除分组
        int count = baseDao.deleteBatchIds(Arrays.asList(ids));
        // 删除分组关联内容
        for (Long id : ids) {
            attributeGroupRelationService.logicDeleteByGroupId(id);
        }
        return count;
    }

    /**
     * 功能描述:
     * 〈分组状态改变〉
     *
     * @param groupStatus 改变后状态
     * @param id          分组id
     * @return : int
     * @author : 刘远杰
     */

    @Override
    public int updateGroupStatus(@RequestParam("groupStatus") Integer groupStatus, @RequestParam("id") Long id) {
        AttributeGroupEntity attributeGroupEntity = new AttributeGroupEntity();
        attributeGroupEntity.setId(id);
        attributeGroupEntity.setGroupStatus(groupStatus);

        return baseDao.updateById(attributeGroupEntity);

    }

    /**
     * 功能描述:
     * 〈根据分组id查询分组〉
     *
     * @param id
     * @author : 刘远杰
     */

    @Override
    public AttributeGroupDTO get(Long id) {
        AttributeGroupEntity entity = this.baseDao.selectById(id);
        return ConvertUtils.sourceToTarget(entity, this.currentDtoClass());
    }

    @Override
    public QueryWrapper<AttributeGroupEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String groupName = (String) params.get("groupName");
        String groupStatus = (String) params.get("groupStatus");

        QueryWrapper<AttributeGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(groupName), "group_name", groupName);
        wrapper.eq(StringUtils.isNotBlank(groupStatus), "group_status", groupStatus);
        wrapper.orderByDesc(Constant.CREATE_DATE);


        return wrapper;
    }

    private void getAttrGroupRelation(Long[] attrIds, List<AttributeGroupRelationDTO> list, Long id) {
        for (Long attrId : attrIds) {
            AttributeGroupRelationDTO attributeGroupRelationDTO = new AttributeGroupRelationDTO();
            attributeGroupRelationDTO.setGroupId(id);
            attributeGroupRelationDTO.setAttrId(attrId);
            list.add(attributeGroupRelationDTO);
        }
    }

}
