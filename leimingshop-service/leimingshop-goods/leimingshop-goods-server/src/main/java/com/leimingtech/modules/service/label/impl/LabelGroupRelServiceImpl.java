/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.label.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.label.LabelGroupRelDao;
import com.leimingtech.modules.dto.goodslable.LabelGroupRelDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupRelSaveDTO;
import com.leimingtech.modules.dto.goodslable.LabelNumDTO;
import com.leimingtech.modules.entity.label.LabelGroupRelEntity;
import com.leimingtech.modules.service.goodslabel.LabelGroupRelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 标签与标签分组关联管理
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Service

public class LabelGroupRelServiceImpl extends CrudServiceImpl<LabelGroupRelDao, LabelGroupRelEntity, LabelGroupRelDTO> implements LabelGroupRelService {

    /**
     * @Author weixianchun
     * @Description 查询关联所有信息
     * @Param params
     * @Date 2019/12/11 9:57
     * @Return java.util.List<com.leimingtech.modules.dto.goodslable.LabelGroupRelDTO>
     * @version 1.0
     */
    @Override

    public List<LabelGroupRelDTO> list(@RequestParam Map<String, Object> params) {
        List<LabelGroupRelEntity> entityList = this.baseDao.selectList(this.getWrapper(params));
        return ConvertUtils.sourceToTarget(entityList, this.currentDtoClass());
    }

    /**
     * @Author weixianchun
     * @Description 构造器
     * @Param params
     * @Date 2019/12/11 9:58
     * @Return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.leimingtech.modules.entity.label.LabelGroupRelEntity>
     * @version 1.0
     */
    @Override
    public QueryWrapper<LabelGroupRelEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String labelId = (String) params.get("labelId");
        String groupId = (String) params.get("groupId");

        QueryWrapper<LabelGroupRelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(labelId), "label_id", labelId);
        wrapper.eq(StringUtils.isNotBlank(groupId), "group_id", groupId);

        return wrapper;
    }


    /**
     * @Author weixianchun
     * @Description 批量保存关联信息
     * @Param dtoList
     * @Date 2019/12/11 9:26
     * @Return boolean
     * @version 1.0
     */

    @Override
    public boolean insertBatch(@RequestBody List<LabelGroupRelSaveDTO> dtoList) {
        List<LabelGroupRelEntity> labelGroupRelEntities = ConvertUtils.sourceToTarget(dtoList, LabelGroupRelEntity.class);
        return this.insertBatch(labelGroupRelEntities, 100);
    }


    /**
     * @Author weixianchun
     * @Description 根据分组ID删除数据
     * @Param groupId
     * @Date 2019/12/12 10:43
     * @Return int
     * @version 1.0
     */

    @Override
    public int deleteByGroupId(Long groupId) {
        return baseDao.deleteByGroupId(groupId);
    }

    /**
     * @Author weixianchun
     * @Description 根据标签ID删除数据
     * @Param labelId
     * @Date 2019/12/12 10:43
     * @Return int
     * @version 1.0
     */

    @Override
    public int deleteByLabelId(Long labelId) {
        return baseDao.deleteByLabelId(labelId);
    }

    /**
     * @Author weixianchun
     * @Description 根据分组ID查询标签数量
     * @Param groupId
     * @Date 2019/12/11 12:08
     * @Return com.leimingtech.modules.dto.goodslable.LabelGroupRelDTO
     * @version 1.0
     */

    @Override
    public LabelNumDTO findLabelNum(@RequestParam("groupId") Long groupId) {
        return baseDao.findLabelNum(groupId);
    }

}
