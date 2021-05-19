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
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.constants.attribute.AttrResultCodeConstants;
import com.leimingtech.modules.dao.attribute.AttributeDao;
import com.leimingtech.modules.dto.attribute.*;
import com.leimingtech.modules.entity.attribute.AttributeEntity;
import com.leimingtech.modules.enums.attribute.GroupStatusEnum;
import com.leimingtech.modules.service.attribute.AttributeGroupRelationService;
import com.leimingtech.modules.service.attribute.AttributeGroupService;
import com.leimingtech.modules.service.attribute.AttributeService;
import com.leimingtech.modules.service.attribute.AttributeValueService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 属性管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Transactional(rollbackFor = Exception.class)
@Service

public class AttributeServiceImpl extends CrudServiceImpl<AttributeDao, AttributeEntity, AttributeDTO> implements AttributeService {

    @Autowired
    private AttributeValueService attributeValueService;

    @Autowired
    private AttributeGroupRelationService attributeGroupRelationService;

    @Autowired
    private AttributeGroupService attributeGroupService;

    /**
     * 功能描述:
     * 〈属性分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<AttributeDTO> page(@RequestParam Map<String, Object> params) {
        //分页
        IPage<AttributeEntity> page = getPage(params, Constant.CREATE_DATE, false);
        //查询
        List<AttributeDTO> attributeDTOList = baseDao.findAttrListByGroupName(params);
        List<AttributeDTO> list = ConvertUtils.sourceToTarget(attributeDTOList, AttributeDTO.class);
        if (CollectionUtils.isNotEmpty(list)) {
            for (AttributeDTO attributeDTO : list) {
                // 取规格分组值
                StringBuffer attrGroupValue = new StringBuffer();
                HashMap<String, Object> relParams = new HashMap<>(10);
                relParams.put("attrId", attributeDTO.getId().toString());
                List<AttributeGroupRelationDTO> attributeGroupRelationDTOList = attributeGroupRelationService.list(relParams);
                for (AttributeGroupRelationDTO attributeGroupRelationDTO : attributeGroupRelationDTOList) {
                    AttributeGroupDTO attributeGroupDTO = attributeGroupService.get(attributeGroupRelationDTO.getGroupId());
                    if (attributeGroupDTO != null && attributeGroupDTO.getGroupStatus().equals(GroupStatusEnum.ENABLED.value())) {
                        attrGroupValue.append(attributeGroupDTO.getGroupName()).append(",");
                    }
                }
                if (attrGroupValue.length() > 0) {
                    String str = attrGroupValue.toString().substring(0, attrGroupValue.length() - 1);
                    attributeDTO.setAttrGroupValue(str);
                }
            }
        }
        return new PageData(list, page.getTotal());
    }

    /**
     * 功能描述:
     * 〈保存属性、属性值〉
     *
     * @param dto 属性实体
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> saveAttr(@RequestBody InsertAttributeDTO dto) {
        Map<String, Object> map = new HashMap<>(10);
        List<InsertAttributeValueDTO> list = dto.getAttributeValueDTOList();
        AttributeEntity attributeEntity = ConvertUtils.sourceToTarget(dto, AttributeEntity.class);
        // 保存规格
        map = this.checkAttrNameWhenAdd(dto.getAttrName());
        Object code = map.get("code");
        if ((int) code == AttrResultCodeConstants.SUCCESS) {
            this.setAttrValue(attributeEntity, list);
            // 属性名称不重复
            this.insert(attributeEntity);
            // 保存属性值
            List<AttributeValueDTO> attributeValueDTOList = ConvertUtils.sourceToTarget(list, AttributeValueDTO.class);
            int i = 255;
            for (AttributeValueDTO attributeValueDTO : attributeValueDTOList) {
                attributeValueDTO.setAttrId(attributeEntity.getId());
                attributeValueDTO.setAttrValueSort(i--);
            }
            attributeValueService.insertBatch(attributeValueDTOList);
            map.put("message", "保存属性成功");
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈保存属性、属性值〉
     *
     * @param dto 属性实体
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> updateAttr(@RequestBody UpdateAttributeDTO dto) {
        Map<String, Object> map = new HashMap<>(10);
        List<InsertAttributeValueDTO> list = dto.getAttributeValueDTOList();
        AttributeEntity attributeEntity = ConvertUtils.sourceToTarget(dto, AttributeEntity.class);
        map.put("code", AttrResultCodeConstants.SUCCESS);
        // 修改规格
        if (StringUtils.isNotBlank(dto.getAttrName())) {
            // 校验规格名称
            map = this.checkAttrNameWhenUpdate(dto.getAttrName(), dto.getId());
        }
        Object code = map.get("code");
        if ((int) code == AttrResultCodeConstants.SUCCESS) {
            this.setAttrValue(attributeEntity, list);
            // 修改属性值
            baseDao.updateById(attributeEntity);
            List<AttributeValueDTO> attributeValueDTOList = ConvertUtils.sourceToTarget(list, AttributeValueDTO.class);
            int i = 255;
            for (AttributeValueDTO attributeValueDTO : attributeValueDTOList) {
                attributeValueDTO.setAttrId(dto.getId());
                attributeValueDTO.setAttrValueSort(i--);
            }
            // 删除原规格值重新插入
            attributeValueService.deleteAttrValueByAttrId(attributeEntity.getId());
            attributeValueService.insertBatch(attributeValueDTOList);
            map.put("message", "修改属性成功");
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈新增属性校验属性名称是否重复〉
     *
     * @param attrName 属性名称
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> checkAttrNameWhenAdd(@RequestParam("attrName") String attrName) {
        Map<String, Object> map = new HashMap<>(10);
        if (StringUtils.isBlank(StringUtils.trim(attrName))) {
            map.put("code", AttrResultCodeConstants.ERR_INV_PARAMS);
            map.put("message", "输入属性名称不合法");
        } else {
            Map<String, Object> params = new HashMap<>(10);
            params.put("attrName", attrName);
            QueryWrapper<AttributeEntity> wrapper = this.getWrapper(params);
            List<AttributeEntity> attributeEntityListEntityList = baseDao.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(attributeEntityListEntityList)) {
                map.put("code", AttrResultCodeConstants.ERR_REPEAT);
                map.put("message", "属性名称已存在");
            } else {
                map.put("code", AttrResultCodeConstants.SUCCESS);
                map.put("message", "属性名称可用");
            }
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈修改规格校验规格名称是否重复〉
     *
     * @param attrName 属性名称
     * @param id       属性id
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object>
    checkAttrNameWhenUpdate(@RequestParam("attrName") String attrName, @RequestParam("id") Long id) {
        Map<String, Object> map = new HashMap<>(10);
        AttributeEntity attributeEntity = baseDao.selectById(id);
        if (attributeEntity == null) {
            // 传入id是否正确
            map.put("code", AttrResultCodeConstants.ERR_NO_RESULT);
            map.put("message", "属性不存在");
        } else {
            if (StringUtils.isBlank(StringUtils.trim(attrName))) {
                // 名称输入合法性判断
                map.put("code", AttrResultCodeConstants.ERR_INV_PARAMS);
                map.put("message", "输入属性名称不合法");

            } else {
                QueryWrapper<AttributeEntity> wrapper = new QueryWrapper<>();
                // 除该规格外其他规格名是否重复
                wrapper.ne(id != null, "id", id);
                wrapper.eq(StringUtils.isNotBlank(attrName), "attr_name", attrName);
                List<AttributeEntity> attributeEntityList = baseDao.selectList(wrapper);
                if (CollectionUtils.isNotEmpty(attributeEntityList)) {
                    map.put("code", AttrResultCodeConstants.ERR_REPEAT);
                    map.put("message", "属性名称已存在");
                } else {
                    map.put("code", AttrResultCodeConstants.SUCCESS);
                    map.put("message", "属性名称可用");
                }
            }
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈查询属性及属性值详情〉
     *
     * @param id 属性id
     * @author : 刘远杰
     */

    @Override
    public AttributeAndAttributeValueDTO findAttrAndAttrValueByAttrId(Long id) {
        // 属性详情查询
        AttributeEntity attributeEntity = this.selectById(id);
        if (attributeEntity == null) {
            return null;
        }
        AttributeAndAttributeValueDTO attributeAndAttributeValueDTO
                = ConvertUtils.sourceToTarget(attributeEntity, AttributeAndAttributeValueDTO.class);

        // 属性值查询
        List<AttributeValueDTO> attrValueDTOList = attributeValueService.findListByAttrId(id);
        attributeAndAttributeValueDTO.setAttributeValueDTOList(attrValueDTOList);
        return attributeAndAttributeValueDTO;
    }

    /**
     * 功能描述:
     * 〈判断属性是否关联后台分类〉
     *
     * @param ids 属性id
     * @author : 刘远杰
     */

    @Override
    public List<AttrClassDTO> checkClass(@RequestBody Long[] ids) {
        return baseDao.countClass(ids);
    }

    /**
     * 功能描述:
     * 〈删除属性及属性值〉
     *
     * @param id 属性id
     * @author : 刘远杰
     */

    @Override
    public Integer deleteAttr(Long id) {
        attributeValueService.logicDeleteAttrValueByAttrId(id);
        return baseDao.deleteById(id);
    }

    /**
     * 功能描述:
     * 〈批量删除属性及属性值〉
     *
     * @param ids 属性id数组
     * @return : int
     * @author : 刘远杰
     */
    @Override

    public int deleteAttrBatch(@RequestBody Long[] ids) {
        for (Long id : ids) {
            attributeValueService.logicDeleteAttrValueByAttrId(id);
        }
        return baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * 〈批量查询〉
     *
     * @param ids 属性id数组
     * @author : 刘远杰
     */

    @Override
    public List<AttributeDTO> selectBatchByids(@RequestBody List<Long> ids) {
        List<AttributeEntity> attributeEntities = baseDao.selectBatchIds(ids);
        return ConvertUtils.sourceToTarget(attributeEntities, AttributeDTO.class);
    }

    /**
     * 功能描述:
     * 〈批量查询属性名称〉
     *
     * @param ids 查询的属性ids
     * @author : 刘远杰
     */

    @Override
    public List<AttributeIdAndNameDTO> selectNameBatchByids(@RequestBody Long[] ids) {
        return baseDao.selectNameBatchByids(ids);
    }

    @Override
    public QueryWrapper<AttributeEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String attrName = (String) params.get("attrName");

        QueryWrapper<AttributeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(attrName), "attr_name", attrName);

        if (StringUtils.isNotBlank((String) params.get(Constant.ORDER_FIELD))
                && StringUtils.isNotBlank((String) params.get(Constant.ORDER))) {
            if (StringUtils.equals((String) params.get(Constant.ORDER), Constant.ASC)) {
                wrapper.orderByAsc((String) params.get(Constant.ORDER_FIELD));
            } else {
                wrapper.orderByDesc((String) params.get(Constant.ORDER_FIELD));
            }
        }

        return wrapper;
    }

    /**
     * 功能描述:
     * 〈设置书属性值〉
     *
     * @param attributeEntity       属性实体
     * @param attributeValueDTOList 属性值集合
     * @return : boolean 结果
     * @author : 刘远杰
     */
    private boolean setAttrValue(AttributeEntity attributeEntity, List<InsertAttributeValueDTO> attributeValueDTOList) {
        StringBuffer attrValue = new StringBuffer();

        for (InsertAttributeValueDTO insertAttributeValueDTO : attributeValueDTOList) {
            // 校验参数是否合法，设置attrInfo
            ValidatorUtils.validateEntity(insertAttributeValueDTO, AddGroup.class, DefaultGroup.class);
            attrValue.append(insertAttributeValueDTO.getAttrValueName()).append(",");
        }
        // 截取末尾“，”
        if (attrValue.length() > 0) {
            String str = attrValue.toString().substring(0, attrValue.length() - 1);
            attributeEntity.setAttrValue(str);
        }
        return false;
    }

    /**
     * 功能描述:
     * 〈属性列表查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<AttributeDTO> list(@RequestParam Map<String, Object> params) {
        String attrName = (String) params.get("attrName");

        QueryWrapper<AttributeEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(attrName), "attr_name", attrName);

        List<AttributeEntity> attributeEntities = baseDao.selectList(wrapper);

        return ConvertUtils.sourceToTarget(attributeEntities, AttributeDTO.class);
    }

}
