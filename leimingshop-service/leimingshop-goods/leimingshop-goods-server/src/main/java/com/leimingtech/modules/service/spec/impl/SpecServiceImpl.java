/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.spec.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.constants.spec.SpecResultCodeConstants;
import com.leimingtech.modules.dao.spec.SpecDao;
import com.leimingtech.modules.dto.spec.*;
import com.leimingtech.modules.entity.spec.SpecEntity;
import com.leimingtech.modules.enums.spec.GroupStatusEnum;
import com.leimingtech.modules.service.spec.SpecGroupRelationService;
import com.leimingtech.modules.service.spec.SpecGroupService;
import com.leimingtech.modules.service.spec.SpecService;
import com.leimingtech.modules.service.spec.SpecValueService;
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
 * 规格管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SpecServiceImpl extends CrudServiceImpl<SpecDao, SpecEntity, SpecDTO> implements SpecService {

    @Autowired

    private SpecValueService specValueService;

    @Autowired

    private SpecGroupRelationService specGroupRelationService;

    @Autowired

    private SpecGroupService specGroupService;

    /**
     * 功能描述:
     * 〈规格条件分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<SpecDTO> findSpecPageList(@RequestParam Map<String, Object> params) {
        //分页
        IPage<SpecEntity> page = getPage(params, "s.update_date", false);
        //查询
        List<SpecEntity> list = baseDao.findSpecListByGroupName(params);
        List<SpecDTO> specDTOList = ConvertUtils.sourceToTarget(list, SpecDTO.class);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SpecDTO specDTO : specDTOList) {
                // 取规格分组值
                StringBuffer specGroupValue = new StringBuffer();
                HashMap<String, Object> relParams = new HashMap<>(10);
                relParams.put("specId", specDTO.getId().toString());
                List<SpecGroupRelationDTO> specGroupRelationDTOList = specGroupRelationService.list(relParams);
                for (SpecGroupRelationDTO specGroupRelationDTO : specGroupRelationDTOList) {
                    SpecGroupDTO specGroupDTO = specGroupService.get(specGroupRelationDTO.getGroupId());
                    if (specGroupDTO != null && specGroupDTO.getGroupStatus().equals(GroupStatusEnum.ENABLED.value())) {
                        specGroupValue.append(specGroupDTO.getGroupName()).append(",");
                    }
                }
                if (specGroupValue.length() > 0) {
                    String str = specGroupValue.toString().substring(0, specGroupValue.length() - 1);
                    specDTO.setSpecGroupValue(str);
                }
            }

        }
        return new PageData(specDTOList, page.getTotal());
    }

    /**
     * 功能描述:
     * 〈规格条件查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<SpecDTO> findSpecList(@RequestParam Map<String, Object> params) {
        List<SpecEntity> specEntityList = baseDao.findSpecList(params);
        return ConvertUtils.sourceToTarget(specEntityList, SpecDTO.class);
    }

    /**
     * 功能描述:
     * 〈保存规格及规格值〉
     *
     * @param dto 规格实体
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> saveSpec(@RequestBody InsertSpecDTO dto) {
        Map<String, Object> map = new HashMap<>(10);

        SpecDTO specDTO = ConvertUtils.sourceToTarget(dto, SpecDTO.class);
        List<InsertSpecValueDTO> list = dto.getSpecValueDTOList();
        // 保存规格
        map = this.checkSpecNameWhenAdd(dto.getSpecName());

        Object code = map.get("code");
        if ((int) code == SpecResultCodeConstants.SUCCESS) {
            this.setSpecValue(specDTO, list);
            SpecEntity specEntity = ConvertUtils.sourceToTarget(specDTO, SpecEntity.class);
            this.insert(specEntity);
            // 保存规格值
            List<SpecValueDTO> specValueDTOList = ConvertUtils.sourceToTarget(list, SpecValueDTO.class);
            int i = 255;
            for (SpecValueDTO specValueDTO : specValueDTOList) {
                specValueDTO.setSpecId(specEntity.getId());
                specValueDTO.setSpecValueSort(i--);
            }
            specValueService.insertBatch(specValueDTOList);
            map.put("message", "保存规格成功");
        }

        return map;
    }

    /**
     * 功能描述:
     * 〈修改规格及规格值〉
     *
     * @param dto 规格实体
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> updateSpec(@RequestBody UpdateSpecDTO dto) {
        Map<String, Object> map = new HashMap<>(10);
        map.put("code", SpecResultCodeConstants.SUCCESS);

        SpecDTO specDTO = ConvertUtils.sourceToTarget(dto, SpecDTO.class);

        List<InsertSpecValueDTO> list = dto.getSpecValueDTOList();

        // 修改规格
        if (StringUtils.isNotBlank(dto.getSpecName())) {
            // 校验规格名称
            map = this.checkSpecNameWhenUpdate(dto.getSpecName(), dto.getId());
        }

        Object code = map.get("code");
        if ((int) code == SpecResultCodeConstants.SUCCESS) {
            this.setSpecValue(specDTO, list);
            this.update(specDTO);
            // 修改规格值
            List<SpecValueDTO> specValueDTOList = ConvertUtils.sourceToTarget(list, SpecValueDTO.class);
            int i = 255;
            for (SpecValueDTO specValueDTO : specValueDTOList) {
                specValueDTO.setSpecId(dto.getId());
                specValueDTO.setSpecValueSort(i--);
            }
            // 删除原规格值重新插入
            specValueService.deleteSpecValueBySpecId(dto.getId());
            specValueService.insertBatch(specValueDTOList);
            map.put("message", "修改规格成功");
        }

        return map;
    }

    /**
     * 功能描述:
     * 〈判断规格是否关联后台分类〉
     *
     * @param ids 规格数组ids
     * @author : 刘远杰
     */

    @Override
    public List<SpecClassDTO> checkClass(@RequestBody Long[] ids) {
        return baseDao.countClass(ids);
    }

    /**
     * 功能描述:
     * 〈逻辑删除规格、规格值〉
     *
     * @param id 规格id
     * @author : 刘远杰
     */

    @Override
    public void deleteSpec(Long id) {
        this.deleteById(id);
        specValueService.logicDeleteSpecValueBySpecId(id);
    }

    /**
     * 功能描述:
     * 〈批量删除规格数组〉
     *
     * @param ids 规格id数组
     * @author : 刘远杰
     */

    @Override
    public int deleteSpecBatch(@RequestBody Long[] ids) {
        for (Long id : ids) {
            specValueService.logicDeleteSpecValueBySpecId(id);
        }
        return baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * 〈根据规格id子查询规格及规格值〉
     *
     * @param id 规格id
     * @author : 刘远杰
     */

    @Override
    public SpecAndSpecValueDTO findSpecAndSpecValueBySpecId(Long id) {
        // 规格详情查询
        SpecEntity specEntity = this.selectById(id);
        if (specEntity == null) {
            return null;
        }
        SpecAndSpecValueDTO specAndSpecValueDTO = ConvertUtils.sourceToTarget(specEntity, SpecAndSpecValueDTO.class);
        // 规格值查询
        Map<String, Object> params = new HashMap<>(10);
        params.put("specId", id);
        List<SpecValueDTO> specValueDTOList = specValueService.findSpecValueList(params);
        specAndSpecValueDTO.setSpecValueDTOList(specValueDTOList);
        return specAndSpecValueDTO;
    }

    /**
     * 功能描述:
     * 〈查询规格列表〉
     *
     * @author : 刘远杰
     */

    @Override
    public List<SpecDTO> findSpecList() {
        QueryWrapper<SpecEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("sp_sort");
        List<SpecEntity> specEntities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(specEntities, SpecDTO.class);
    }

    /**
     * 功能描述:
     * 〈批量查询〉
     *
     * @param ids 查询的规格ids
     * @author : 刘远杰
     */

    @Override
    public List<SpecDTO> selectBatchByids(@RequestBody List<Long> ids) {
        List<SpecEntity> specEntities = baseDao.selectBatchIds(ids);
        return ConvertUtils.sourceToTarget(specEntities, SpecDTO.class);
    }

    /**
     * 功能描述:
     * 〈批量查询规格名称〉
     *
     * @param ids 查询的规格ids
     * @author : 刘远杰
     */

    @Override
    public List<SpecIdAndNameDTO> selectNameBatchByids(@RequestBody Long[] ids) {
        return baseDao.selectNameBatchByids(ids);

    }

    /**
     * 功能描述:
     * 〈新增规格校验规格名称是否重复〉
     *
     * @param specName 规格名称
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> checkSpecNameWhenAdd(@RequestParam("specName") String specName) {
        Map<String, Object> map = new HashMap<>(10);
        if (StringUtils.isBlank(StringUtils.trim(specName))) {
            map.put("code", SpecResultCodeConstants.ERR_INV_PARAMS);
            map.put("message", "输入规格名称不合法");
        } else {
            Map<String, Object> params = new HashMap<>(10);
            params.put("specName", specName);
            QueryWrapper<SpecEntity> wrapper = this.getWrapper(params);
            List<SpecEntity> spceEntityList = baseDao.selectList(wrapper);

            if (CollectionUtils.isNotEmpty(spceEntityList)) {
                map.put("code", SpecResultCodeConstants.ERR_REPEAT);
                map.put("message", "规格名称已存在");
            } else {
                map.put("code", SpecResultCodeConstants.SUCCESS);
                map.put("message", "规格名称可用");
            }
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈修改规格校验规格名称是否重复〉
     *
     * @param specName 规格名称
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> checkSpecNameWhenUpdate(@RequestParam("specName") String specName, @RequestParam("id") Long id) {
        Map<String, Object> map = new HashMap<>(10);
        SpecEntity specEntity = baseDao.selectById(id);
        if (specEntity == null) {
            // 传入id是否正确
            map.put("code", SpecResultCodeConstants.ERR_INV_PARAMS);
            map.put("message", "规格不存在");
        } else {
            if (StringUtils.isBlank(specName)) {
                // 名称输入合法性判断
                map.put("code", SpecResultCodeConstants.ERR_INV_PARAMS);
                map.put("message", "输入规格名称不合法");
            } else {
                QueryWrapper<SpecEntity> wrapper = new QueryWrapper<>();
                // 除该规格外其他规格名是否重复
                wrapper.ne(id != null, "id", id);
                wrapper.eq(StringUtils.isNotBlank(specName), "spec_name", specName);
                List<SpecEntity> spceEntityList = baseDao.selectList(wrapper);

                if (CollectionUtils.isNotEmpty(spceEntityList)) {
                    map.put("code", SpecResultCodeConstants.ERR_REPEAT);
                    map.put("message", "规格名称已存在");
                } else {
                    map.put("code", SpecResultCodeConstants.SUCCESS);
                    map.put("message", "规格名称可用");
                }
            }
        }
        return map;
    }

    @Override
    public QueryWrapper<SpecEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String specName = (String) params.get("specName");

        QueryWrapper<SpecEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(specName), "spec_name", specName);

        return wrapper;
    }

    /**
     * 功能描述:
     * 〈根据规格值集合设置规格值冗余字段〉
     *
     * @param dto  规格实体
     * @param list 规格值集合
     * @return : boolean 是否成功
     * @author : 刘远杰
     */
    private boolean setSpecValue(SpecDTO dto, List<InsertSpecValueDTO> list) {
        StringBuffer specValue = new StringBuffer();

        for (InsertSpecValueDTO insertSpecValueDTO : list) {
            // 校验参数是否合法，设置specInfo
            ValidatorUtils.validateEntity(insertSpecValueDTO, AddGroup.class, DefaultGroup.class);
            specValue.append(insertSpecValueDTO.getSpecValueName()).append(",");
        }
        if (specValue.length() > 0) {
            String str = specValue.toString().substring(0, specValue.length() - 1);
            dto.setSpecValue(str);
        }
        return false;
    }

}
