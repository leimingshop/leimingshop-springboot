/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.label.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.label.GoodsLabelDao;
import com.leimingtech.modules.dto.goodslable.*;
import com.leimingtech.modules.entity.label.GoodsLabelEntity;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goodslabel.GoodsLabelRelService;
import com.leimingtech.modules.service.goodslabel.GoodsLabelService;
import com.leimingtech.modules.service.goodslabel.LabelGroupRelService;
import com.leimingtech.modules.service.goodslabel.LabelGroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: weixianchun
 * @Description: 商品标签GoodsLabelServiceImpl
 * @Date :2019/5/20 14:19
 * @Version V1.0
 **/
@Service

@Slf4j
public class GoodsLabelServiceImpl extends CrudServiceImpl<GoodsLabelDao, GoodsLabelEntity, GoodsLabelDTO> implements GoodsLabelService {

    @Autowired
    private GoodsLabelRelService goodsLabelRelService;
    @Autowired
    private LabelGroupRelService labelGroupRelService;
    @Autowired
    private LabelGroupService labelGroupService;

    @Autowired
    private GoodsService goodsService;

    @Override
    public QueryWrapper<GoodsLabelEntity> getWrapper(Map<String, Object> params) {

        String id = (String) params.get("id");
        //获取标签名称
        String labelName = (String) params.get("labelName");
        String groupStatus = (String) params.get("groupStatus");
        QueryWrapper<GoodsLabelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //根据标签名称模糊查询
        wrapper.like(StringUtils.isNotBlank(labelName), "label_name", labelName);
        wrapper.eq(StringUtils.isNotBlank(groupStatus), "group_status", groupStatus);
        return wrapper;
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询标签信息
     * @Date :2019/5/29 14:59
     * @Param id
     * @Version V1.0
     **/

    @Override
    public GoodsLabelDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @Author weixianchun
     * @Description 标签, 分组回显
     * @Param id
     * @Date 2019/12/12 20:12
     * @Return com.leimingtech.modules.dto.goodslable.GoodsLabelFindDTO
     * @version 1.0
     */

    @Override
    public GoodsLabelFindDTO findByLabelId(Long id) {

        //标签详情
        GoodsLabelDTO labelDTO = super.get(id);
        GoodsLabelFindDTO goodsLabelFindDTO = ConvertUtils.sourceToTarget(labelDTO, GoodsLabelFindDTO.class);

        //根据标签ID查询分组信息
        HashMap<String, Object> groupRelParams = new HashMap<>(10);
        groupRelParams.put("labelId", labelDTO.getId().toString());
        List<LabelGroupRelDTO> groupRelDTOList = labelGroupRelService.list(groupRelParams);

        //未关联分组直接返回标签信息
        if (CollectionUtils.isEmpty(groupRelDTOList)) {
            return goodsLabelFindDTO;
        }
        List<Long> ids = new ArrayList<>();
        for (LabelGroupRelDTO labelGroupRelDTO : groupRelDTOList) {
            ids.add(labelGroupRelDTO.getGroupId());
        }
        goodsLabelFindDTO.setGroupIds(ids);

        return goodsLabelFindDTO;

    }

    /**
     * @Author: weixianchun
     * @Description: 保存标签信息
     * @Date :2019/5/29 14:59
     * @Param dto
     * @Version V1.0
     **/

    @Override
    public void save(@RequestBody GoodsLabelSaveDTO dto) {
        //保存标签信息
        GoodsLabelEntity goodsLabelEntity = ConvertUtils.sourceToTarget(dto, GoodsLabelEntity.class);
        baseDao.insert(goodsLabelEntity);

        //如果关联分组(关联表保存)
        if (null != dto.getGroupIds() && dto.getGroupIds().length > 0) {

            // 获得关联表保存对象
            List<LabelGroupRelSaveDTO> labelGroupRelDTOList = getLabelGroupRel(dto.getGroupIds(), goodsLabelEntity.getId());
            labelGroupRelService.insertBatch(labelGroupRelDTOList);
        }
    }

    /**
     * 封装关联表对象
     *
     * @param groupIds
     * @param id
     * @return
     */
    private List<LabelGroupRelSaveDTO> getLabelGroupRel(Long[] groupIds, Long id) {
        // 批量保存规格分组关联内容
        List<LabelGroupRelSaveDTO> labelGroupRelDTOList = new ArrayList<>();
        for (Long groupId : groupIds) {
            LabelGroupRelSaveDTO labelGroupRelSaveDTO = new LabelGroupRelSaveDTO();
            labelGroupRelSaveDTO.setLabelId(id);
            labelGroupRelSaveDTO.setGroupId(groupId);
            labelGroupRelDTOList.add(labelGroupRelSaveDTO);
        }
        return labelGroupRelDTOList;
    }

    /**
     * @Author: weixianchun
     * @Description: 修改标签信息
     * @Date :2019/5/29 14:59
     * @Param dto
     * @Version V1.0
     **/

    @Override
    public void update(@RequestBody GoodsLabelUpdateDTO dto) {

        //修改标签信息
        GoodsLabelDTO goodsLabelDTO = ConvertUtils.sourceToTarget(dto, GoodsLabelDTO.class);
        super.update(goodsLabelDTO);

        // 获得关联表保存对象
        List<LabelGroupRelSaveDTO> labelGroupRelDTOList = getLabelGroupRel(dto.getGroupIds(), dto.getId());
        //删除原标签关联关系
        labelGroupRelService.deleteByLabelId(dto.getId());
        //批量新增
        labelGroupRelService.insertBatch(labelGroupRelDTOList);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id删除标签信息
     * @Date :2019/5/29 15:00
     * @Param :
     * @Param ids
     * @Version V1.0
     **/

    @Override
    public void delete(@RequestBody Long[] ids) {

        super.delete(ids);
        goodsLabelRelService.deleteBatchByLabelIds(ids);
        //根据标签ID删除关联关系
        if (null != ids && ids.length > 0) {
            for (Long id : ids) {
                labelGroupRelService.deleteByLabelId(id);
            }
        }
    }

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/29 15:00
     * @Param params
     * @Version V1.0
     **/

    @Override
    public PageData<GoodsLabelDTO> page(@RequestParam Map<String, Object> params) {
        //分页
        IPage<GoodsLabelEntity> page = getPage(params, "gl.create_date", false);

        //查询标签名称 分组名称
        List<GoodsLabelDTO> list = baseDao.findListPage(params);
        if (CollectionUtils.isNotEmpty(list)) {
            for (GoodsLabelDTO labelDTO : list) {
                //根据标签ID查询
                HashMap<String, Object> groupRelParams = new HashMap<>(10);
                groupRelParams.put("labelId", labelDTO.getId().toString());
                //查询分组名称
                List<LabelGroupDTO> labelGroupDTOList = Optional.ofNullable(labelGroupService.getByLabelId(labelDTO.getId())).orElse(new ArrayList<>());
                StringBuffer groupNames = new StringBuffer();
                for (LabelGroupDTO labelGroupDTO : labelGroupDTOList) {
                    groupNames.append(labelGroupDTO.getGroupName()).append(",");
                }
                if (groupNames.length() > 0) {
                    labelDTO.setGroupName(groupNames.toString().substring(0, groupNames.length() - 1));
                }
                //根据标签ID获取商品数,店铺数量
                HashMap<String, Object> hashMap = new HashMap<>(10);
                hashMap.put("labelId", labelDTO.getId());
                List<GoodsLabelRelDTO> relDTOList = goodsLabelRelService.list(hashMap);
                if (CollectionUtils.isNotEmpty(relDTOList)) {
                    labelDTO.setGoodsNum(relDTOList.size());
                    Set<Long> goodsIdsSet = relDTOList.stream().map(GoodsLabelRelDTO::getGoodsId).collect(Collectors.toSet());
                    List<Long> goodsIds = new ArrayList<>(goodsIdsSet);
                    labelDTO.setStoreNum(goodsService.selectStoreCountByGoodsIds(goodsIds));
                } else {
                    Integer defaultValue = 0;
                    labelDTO.setGoodsNum(defaultValue);
                    labelDTO.setStoreNum(defaultValue);
                }
            }
        }
        return new PageData(list, page.getTotal());
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有标签信息
     * @Date :2019/5/29 15:01
     * @Param params
     * @Version V1.0
     **/

    @Override
    public List<GoodsLabelDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @Author weixianchun
     * @Description 修改时校验标签名称是否重复
     * @Param GoodsLabelUpdateDTO
     * @Date 2019/12/11 15:02
     * @Return int
     * @version 1.0
     */

    @Override
    public int checkLabelNameUpate(@RequestBody GoodsLabelUpdateDTO labelUpdateDTO) {
        return baseDao.checkLabelNameUpate(labelUpdateDTO);
    }

    /**
     * @Author weixianchun
     * @Description 保存时校验标签名称是否重复
     * @Param goodsLabelDTO
     * @Date 2019/12/11 15:02
     * @Return int
     * @version 1.0
     */

    @Override
    public int checkLabelNameSave(@RequestParam("labelName") String labelName) {
        return baseDao.checkLabelNameSave(labelName);
    }

    /**
     * @Author weixianchun
     * @Description 批量保存标签信息
     * @Param dtoList
     * @Date 2019/12/11 9:26
     * @Return boolean
     * @version 1.0
     */

    @Override
    public boolean insertBatch(@RequestBody List<GoodsLabelSaveDTO> dtoList) {
        List<GoodsLabelEntity> labelEntities = ConvertUtils.sourceToTarget(dtoList, GoodsLabelEntity.class);
        return this.insertBatch(labelEntities, 100);
    }

    /**
     * 功能描述 根据标签名查看标签id
     *
     * @param goodsLabel 标签名
     * @return java.lang.Long id
     * @author lishuo
     * @date 24/6/2020
     */
    @Override

    public Long findByLabelName(@RequestParam("goodsLabel") String goodsLabel) {
        QueryWrapper<GoodsLabelEntity> queryWrapper = new QueryWrapper<>();
        GoodsLabelEntity goodsLabelEntity = baseDao.selectOne(queryWrapper.eq("label_name", goodsLabel));
        if (goodsLabelEntity != null) {
            return goodsLabelEntity.getId();
        }
        return null;

    }

    /**
     * 功能描述 Id 查找标签名字
     *
     * @param goodsId goodsId
     * @return java.lang.String 标签名字
     * @author lishuo
     * @date 27/6/2020
     */
    @Override

    public String findByGoodsId(Long goodsId) {
        return baseDao.findByGoodsId(goodsId);
    }
}
