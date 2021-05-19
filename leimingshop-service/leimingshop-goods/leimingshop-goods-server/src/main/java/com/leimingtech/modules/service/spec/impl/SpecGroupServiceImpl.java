/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.spec.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constants.spec.SpecResultCodeConstants;
import com.leimingtech.modules.dao.spec.SpecGroupDao;
import com.leimingtech.modules.dto.spec.*;
import com.leimingtech.modules.entity.spec.SpecGroupEntity;
import com.leimingtech.modules.service.spec.SpecGroupRelationService;
import com.leimingtech.modules.service.spec.SpecGroupService;
import com.leimingtech.modules.service.spec.SpecService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 规格分组管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SpecGroupServiceImpl extends CrudServiceImpl<SpecGroupDao, SpecGroupEntity, SpecGroupDTO> implements SpecGroupService {

    @Autowired

    private SpecGroupRelationService specGroupRelationService;
    @Autowired

    private SpecService specService;

    /**
     * 功能描述:
     * 〈规格分组分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<SpecGroupDTO> page(@RequestParam Map<String, Object> params) {

        //分页
        IPage<SpecGroupEntity> page = getPage(params, Constant.CREATE_DATE, false);

        //查询
        List<SpecGroupEntity> list = baseDao.findSpecGroupShowList(params);

        return getPageData(list, page.getTotal(), SpecGroupDTO.class);
    }

    /**
     * 功能描述:
     * 〈查询规格分组及规格列表〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<SpecGroupAndSpecDTO> listSpec(@RequestParam Map<String, Object> params) {
        List<SpecGroupAndSpecDTO> specGroupAndSpecDTOList = new ArrayList<>();

        //分页
        params.put(Constant.ORDER_FIELD, "sort");

        //查询
        List<SpecGroupEntity> specGroupEntityList = baseDao.findSpecGroupShowList(params);

        if (CollectionUtils.isNotEmpty(specGroupEntityList)) {
            for (SpecGroupEntity specGroupEntity : specGroupEntityList) {
                SpecGroupAndSpecDTO specGroupAndSpecDTO = getSpecGroupAndSpecDTO(specGroupEntity);
                if (CollectionUtils.isNotEmpty(specGroupAndSpecDTO.getSpecDTOList())) {
                    specGroupAndSpecDTOList.add(specGroupAndSpecDTO);
                }
            }
        }
        return specGroupAndSpecDTOList;
    }

    /**
     * 功能描述:
     * 〈修改规格分组及关联规格〉
     *
     * @param dto 更新规格分组实体
     * @return : Result 结果信息
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> updateSpecGroup(@RequestBody UpdateSpecGroupDTO dto) {
        Map<String, Object> map = new HashMap<>(10);
        map.put("code", SpecResultCodeConstants.SUCCESS);
        if (StringUtils.isNotBlank(dto.getGroupName())) {
            // 校验分组名称重复
            map = this.checkSpecGroupNameWhenUpdate(dto.getGroupName(), dto.getId());
        }

        Object code = map.get("code");
        if ((int) code == SpecResultCodeConstants.SUCCESS) {
            // 分组名称不重复，或者未填
            // 修改规格分组
            SpecGroupDTO specGroupDTO = ConvertUtils.sourceToTarget(dto, SpecGroupDTO.class);
            Long[] specIds = dto.getSpecIds();
            this.update(specGroupDTO);
            // 批量保存规格分组关联内容
            List<SpecGroupRelationDTO> specGroupRelationDTOList = new ArrayList<>();
            // 获得关联表保存对象
            getSpecGroupRelation(specIds, specGroupRelationDTOList, dto.getId());
            // 删除原关联规格
            specGroupRelationService.deleteByGroupId(dto.getId());
            specGroupRelationService.insertBatch(specGroupRelationDTOList);
            map.put("message", "修改规格分组成功");
        }

        return map;

    }

    /**
     * 功能描述:
     * 〈保存规格分组及关联规格〉
     *
     * @param dto 新增规格分组实体
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> saveSpecGroup(@RequestBody InsertSpecGroupDTO dto) {
        // 校验分组名称
        Map<String, Object> map = this.checkSpecGroupNameWhenAdd(dto.getGroupName());
        // 保存规格分组
        Object code = map.get("code");
        if ((int) code == SpecResultCodeConstants.SUCCESS) {
            SpecGroupEntity specGroupEntity = ConvertUtils.sourceToTarget(dto, SpecGroupEntity.class);
            Long[] specIds = dto.getSpecIds();
            baseDao.insert(specGroupEntity);
            // 批量保存规格分组关联内容
            List<SpecGroupRelationDTO> specGroupRelationDTOList = new ArrayList<>();
            // 获得关联表保存对象
            getSpecGroupRelation(specIds, specGroupRelationDTOList, specGroupEntity.getId());
            specGroupRelationService.insertBatch(specGroupRelationDTOList);
            map.put("message", "保存规格分组成功");
        }

        return map;
    }

    /**
     * 功能描述:
     * 〈查询分组详情及其关联的规格〉
     *
     * @param id 规格分组id
     * @return : SpecGroupAndSpecDTO 查询结果
     * @author : 刘远杰
     */

    @Override
    public SpecGroupAndSpecDTO findSpecGroupAndSpec(@RequestParam("id") Long id) {
        // 分组详情查询
        SpecGroupEntity specGroupEntity = baseDao.selectById(id);
        if (specGroupEntity == null) {
            return null;
        }
        return getSpecGroupAndSpecDTO(specGroupEntity);
    }

    /**
     * 功能描述:
     * 〈获得规格分组下所有规格〉
     *
     * @param specGroupEntity 规格分组实体
     * @author : 刘远杰
     */
    private SpecGroupAndSpecDTO getSpecGroupAndSpecDTO(SpecGroupEntity specGroupEntity) {
        SpecGroupAndSpecDTO specGroupAndSpecDTO = ConvertUtils.sourceToTarget(specGroupEntity, SpecGroupAndSpecDTO.class);
        Map<String, Object> params = new HashMap<>(10);
        params.put("groupId", specGroupEntity.getId().toString());
        // 关联规格id
        List<SpecGroupRelationDTO> list = specGroupRelationService.list(params);

        if (CollectionUtils.isNotEmpty(list)) {
            ArrayList<Long> ids = new ArrayList<>();
            for (SpecGroupRelationDTO specGroupRelationDTO : list) {
                ids.add(specGroupRelationDTO.getSpecId());
            }
            // 查询规格详情
            List<SpecDTO> specDTOList = specService.selectBatchByids(ids);
            if (CollectionUtils.isNotEmpty(specDTOList)) {
                specGroupAndSpecDTO.setSpecDTOList(specDTOList);
                specGroupAndSpecDTO.setLableNum(specDTOList.size());
            } else {
                specGroupAndSpecDTO.setLableNum(0);
            }
        }
        return specGroupAndSpecDTO;
    }

    /**
     * 功能描述:
     * 〈新增规格分组校验规格分组名称重复性〉
     *
     * @param specGroupName 规格分组名称
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> checkSpecGroupNameWhenAdd(@RequestParam("specGroupName") String specGroupName) {
        Map<String, Object> map = new HashMap<>(10);
        if (StringUtils.isBlank(StringUtils.trim(specGroupName))) {
            map.put("code", SpecResultCodeConstants.ERR_INV_PARAMS);
            map.put("message", "输入规格分组名称不合法");
        } else {
            QueryWrapper<SpecGroupEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("group_name", specGroupName);
            List<SpecGroupEntity> specGroupEntities = baseDao.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(specGroupEntities)) {
                map.put("code", SpecResultCodeConstants.ERR_REPEAT);
                map.put("message", "规格分组名称已存在");
            } else {
                map.put("code", SpecResultCodeConstants.SUCCESS);
                map.put("message", "规格分组名称可用");
            }
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈修改规格分组校验规格分组名称重复性〉
     *
     * @param specGroupName 规格分组名称
     * @param id            规格分组id
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> checkSpecGroupNameWhenUpdate(@RequestParam("specGroupName") String specGroupName, @RequestParam("id") Long id) {
        Map<String, Object> map = new HashMap<>(10);
        SpecGroupEntity specGroupEntity = baseDao.selectById(id);
        if (specGroupEntity == null) {
            // 传入id是否正确
            map.put("code", SpecResultCodeConstants.ERR_INV_PARAMS);
            map.put("message", "规格分组不存在");
        } else {
            if (StringUtils.isBlank(StringUtils.trim(specGroupName))) {
                map.put("code", SpecResultCodeConstants.ERR_INV_PARAMS);
                map.put("message", "输入规格分组名称不合法");
            } else {
                QueryWrapper<SpecGroupEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("group_name", specGroupName);
                wrapper.ne("id", id);
                List<SpecGroupEntity> specGroupEntities = baseDao.selectList(wrapper);
                if (CollectionUtils.isNotEmpty(specGroupEntities)) {
                    map.put("code", SpecResultCodeConstants.ERR_REPEAT);
                    map.put("message", "规格分组名称已存在");
                } else {
                    map.put("code", SpecResultCodeConstants.SUCCESS);
                    map.put("message", "规格分组名称可用");
                }
            }
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈删除规格分组〉
     *
     * @param id 规格分组id
     * @return : int 删除数量
     * @author : 刘远杰
     */

    @Override
    public int deleteSpecGroup(Long id) {
        // 删除分组
        int count = baseDao.deleteById(id);
        // 删除分组关联内容
        specGroupRelationService.logicDeleteByGroupId(id);
        return count;
    }

    /**
     * 功能描述:
     * 〈批量删除规格分组〉
     *
     * @param ids 规格id数组
     * @author : 刘远杰
     */

    @Override
    public int deleteSpecGroupBatch(@RequestBody Long[] ids) {
        for (Long id : ids) {
            specGroupRelationService.logicDeleteByGroupId(id);
        }
        return baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * 〈获得规格分组详情〉
     *
     * @param id 规格id
     * @author : 刘远杰
     */

    @Override
    public SpecGroupDTO get(Long id) {
        SpecGroupEntity entity = this.baseDao.selectById(id);
        return ConvertUtils.sourceToTarget(entity, this.currentDtoClass());
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
        SpecGroupEntity specGroupEntity = new SpecGroupEntity();
        specGroupEntity.setId(id);
        specGroupEntity.setGroupStatus(groupStatus);

        return baseDao.updateById(specGroupEntity);

    }

    @Override
    public QueryWrapper<SpecGroupEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String groupName = (String) params.get("groupName");
        String groupStatus = (String) params.get("groupStatus");

        QueryWrapper<SpecGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(groupName), "group_name", groupName);
        wrapper.eq(StringUtils.isNotBlank(groupStatus), "group_status", groupStatus);

        return wrapper;
    }

    private void getSpecGroupRelation(Long[] specIds, List<SpecGroupRelationDTO> specGroupRelationDTOList, Long id) {
        for (Long specId : specIds) {
            SpecGroupRelationDTO specGroupRelationDTO = new SpecGroupRelationDTO();
            specGroupRelationDTO.setGroupId(id);
            specGroupRelationDTO.setSpecId(specId);
            specGroupRelationDTOList.add(specGroupRelationDTO);
        }
    }

}
