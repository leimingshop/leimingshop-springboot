/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.label.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.exception.SentinelBadRequestException;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.label.LabelGroupDao;
import com.leimingtech.modules.dto.goodslable.*;
import com.leimingtech.modules.entity.label.LabelGroupEntity;
import com.leimingtech.modules.service.goodslabel.LabelGroupRelService;
import com.leimingtech.modules.service.goodslabel.LabelGroupService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标签分组表
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Service

public class LabelGroupServiceImpl extends CrudServiceImpl<LabelGroupDao, LabelGroupEntity, LabelGroupDTO> implements LabelGroupService {

    @Autowired
    private LabelGroupRelService groupRelService;

    @Autowired
    private LabelGroupRelService labelGroupRelService;

    /**
     * @Author weixianchun
     * @Description 分页查询分组信息
     * @Param params
     * @Date 2019/12/11 9:26
     * @Return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.goodslable.LabelGroupDTO>
     * @version 1.0
     */
    @Override

    public PageData<LabelGroupDTO> page(@RequestParam Map<String, Object> params) {

        //分页
        IPage<LabelGroupEntity> page = getPage(params, "sort", false);
        List<LabelGroupDTO> list = baseDao.findListPage(params);
        for (LabelGroupDTO labelGroupDTO : list) {
            LabelNumDTO dto = groupRelService.findLabelNum(labelGroupDTO.getId());
            if (null != dto) {
                labelGroupDTO.setLabelNum(dto.getLabelNum());
            }
        }
        return getPageData(list, page.getTotal(), LabelGroupDTO.class);
    }

    /**
     * @Author weixianchun
     * @Description 查询所有分组信息
     * @Param params
     * @Date 2019/12/11 9:29
     * @Return java.util.List<com.leimingtech.modules.dto.goodslable.LabelGroupDTO>
     * @version 1.0
     */
    @Override

    public List<LabelGroupDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @Author weixianchun
     * @Description 构造器
     * @Param params
     * @Date 2019/12/11 9:29
     * @Return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.leimingtech.modules.entity.label.LabelGroupEntity>
     * @version 1.0
     */
    @Override
    public QueryWrapper<LabelGroupEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String groupStatus = params.get("groupStatus").toString();
        String groupName = (String) params.get("groupName");

        QueryWrapper<LabelGroupEntity> wrapper = new QueryWrapper<>();

        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        wrapper.eq(StringUtils.isNotBlank(groupStatus), "group_status", groupStatus);
        wrapper.like(StringUtils.isNotBlank(groupName), "group_name", groupName);
        return wrapper;
    }

    /**
     * @Author weixianchun
     * @Description 根据ID查询分组信息
     * @Param id
     * @Date 2019/12/11 9:29
     * @Return com.leimingtech.modules.dto.goodslable.LabelGroupDTO
     * @version 1.0
     */
    @Override

    public LabelGroupDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @Author weixianchun
     * @Description 分组标签关联回显
     * @Param id
     * @Date 2019/12/12 21:38
     * @Return com.leimingtech.modules.dto.goodslable.GoodsGroupFindDTO
     * @version 1.0
     */

    @Override
    public GoodsGroupFindDTO findByGroupId(Long id) {

        //分组详情
        LabelGroupDTO labelGroupDTO = super.get(id);
        GoodsGroupFindDTO groupFindDTO = ConvertUtils.sourceToTarget(labelGroupDTO, GoodsGroupFindDTO.class);

        //根据分组ID查询分组信息
        HashMap<String, Object> groupRelParams = new HashMap<>(10);
        groupRelParams.put("groupId", labelGroupDTO.getId().toString());
        List<LabelGroupRelDTO> groupRelDTOList = labelGroupRelService.list(groupRelParams);

        //未关联分组直接返回分组信息
        if (CollectionUtils.isEmpty(groupRelDTOList)) {
            return groupFindDTO;
        }
        List<Long> ids = new ArrayList<>();
        for (LabelGroupRelDTO labelGroupRelDTO : groupRelDTOList) {
            ids.add(labelGroupRelDTO.getLabelId());
        }
        groupFindDTO.setLabelId(ids);

        return groupFindDTO;
    }

    /**
     * @Author weixianchun
     * @Description 保存分组信息
     * @Param dto
     * @Date 2019/12/11 9:30
     * @Return void
     * @version 1.0
     */
    @Override

    public void save(@RequestBody LabelGroupSaveDTO dto) {
        //保存分组信息
        LabelGroupEntity labelGroupEntity = ConvertUtils.sourceToTarget(dto, LabelGroupEntity.class);
        baseDao.insert(labelGroupEntity);

        //如果关联分组(关联表保存)
        if (null != dto.getLabelIds() && dto.getLabelIds().length > 0) {

            // 获得关联表保存对象
            List<LabelGroupRelSaveDTO> groupRelSaveDTOList = getLabelGroupRel(dto.getLabelIds(), labelGroupEntity.getId());
            labelGroupRelService.insertBatch(groupRelSaveDTOList);
        }
    }

    /**
     * 封装关联表对象
     *
     * @param getLabelIds
     * @param id
     * @return
     */
    private List<LabelGroupRelSaveDTO> getLabelGroupRel(Long[] getLabelIds, Long id) {
        List<LabelGroupRelSaveDTO> labelGroupRelSaveDTOList = new ArrayList<>();
        for (Long labelId : getLabelIds) {
            LabelGroupRelSaveDTO labelGroupRelSaveDTO = new LabelGroupRelSaveDTO();
            labelGroupRelSaveDTO.setLabelId(labelId);
            labelGroupRelSaveDTO.setGroupId(id);
            labelGroupRelSaveDTOList.add(labelGroupRelSaveDTO);
        }
        return labelGroupRelSaveDTOList;
    }

    /**
     * @Author weixianchun
     * @Description 批量保存分组信息
     * @Param dtoList
     * @Date 2019/12/11 9:30
     * @Return boolean
     * @version 1.0
     */

    @Override
    public boolean insertBatch(@RequestBody List<LabelGroupSaveDTO> dtoList) {
        List<LabelGroupEntity> labelGroupEntities = ConvertUtils.sourceToTarget(dtoList, LabelGroupEntity.class);
        return this.insertBatch(labelGroupEntities, 100);
    }

    /**
     * @Author weixianchun
     * @Description 修改分组信息
     * @Param dto
     * @Date 2019/12/11 9:30
     * @Return void
     * @version 1.0
     */
    @Override

    public int update(@RequestBody LabelGroupUpdateDTO dto) {

        // 获得关联表保存对象
        List<LabelGroupRelSaveDTO> relSaveDTOList = this.getLabelGroupRel(dto.getLabelIds(), dto.getId());
        //删除原分组关联关系
        labelGroupRelService.deleteByGroupId(dto.getId());
        //新增标签分组关联表
        labelGroupRelService.insertBatch(relSaveDTOList);

        //修改分组信息
        LabelGroupEntity labelGroupEntity = ConvertUtils.sourceToTarget(dto, LabelGroupEntity.class);
        return baseDao.updateById(labelGroupEntity);

    }

    /**
     * @Author weixianchun
     * @Description 修改分组状态(启用, 禁用)
     * @Param dto
     * @Date 2019/12/11 9:28
     * @Return void
     * @version 1.0
     */

    @Override
    public int updateGroupStatus(@RequestParam("groupStatus") Integer groupStatus, @RequestParam("id") Long id) {
        LabelGroupEntity groupEntity = new LabelGroupEntity();
        if (groupStatus == 2) {
            LabelNumDTO dto = groupRelService.findLabelNum(id);
            if (dto != null && dto.getLabelNum() > 0 && dto.getLabelNum() != null) {
                throw new SentinelBadRequestException("该分组已关联标签,请取消关联后禁用");
            }
        }
        groupEntity.setId(id);
        groupEntity.setGroupStatus(groupStatus);
        return baseDao.updateById(groupEntity);
    }

    /**
     * @Author weixianchun
     * @Description 删除分组信息
     * @Param ids
     * @Date 2019/12/11 9:31
     * @Return void
     * @version 1.0
     */
    @Override

    public void delete(@RequestBody Long[] ids) {

        super.delete(ids);

        //根据分组ID删除关联关系
        if (null != ids && ids.length > 0) {
            for (Long id : ids) {
                labelGroupRelService.deleteByGroupId(id);
            }
        }
    }

    /**
     * @Author weixianchun
     * @Description 修改时校验分组名称是否重复
     * @Param dto
     * @Date 2019/12/11 15:02
     * @Return int
     * @version 1.0
     */

    @Override
    public int checkGroupNameUpate(@RequestBody LabelGroupUpdateDTO dto) {
        return baseDao.checkGroupNameUpate(dto);
    }

    /**
     * @Author weixianchun
     * @Description 保存时校验分组名称是否重复
     * @Param groupName
     * @Date 2019/12/11 15:02
     * @Return int
     * @version 1.0
     */

    @Override
    public int checkGroupNameSave(@RequestParam("groupName") String groupName) {
        return baseDao.checkGroupNameSave(groupName);
    }


    @Override
    public List<LabelGroupDTO> getByLabelId(Long labelId) {
        return baseDao.getByLabelId(labelId);
    }

}
