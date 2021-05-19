/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.grade.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.grade.StoreGradeDao;
import com.leimingtech.modules.dto.grade.StoreGradeDTO;
import com.leimingtech.modules.dto.grade.StoreGradeSaveDTO;
import com.leimingtech.modules.dto.grade.StoreGradeUpdateDTO;
import com.leimingtech.modules.dto.grade.StoreShowFlagUpdateDTO;
import com.leimingtech.modules.entity.grade.StoreGradeEntity;
import com.leimingtech.modules.service.grade.StoreGradeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 店铺等级
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-30
 */
@Service
public class StoreGradeServiceImpl extends CrudServiceImpl<StoreGradeDao, StoreGradeEntity, StoreGradeDTO> implements StoreGradeService {

    @Override
    public QueryWrapper<StoreGradeEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String sgName = (String) params.get("sgName");
        Integer showFlag = (Integer) params.get("showFlag");
        QueryWrapper<StoreGradeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(showFlag != null, "show_flag", showFlag);
        wrapper.like(StringUtils.isNotBlank(sgName), "sg_name", sgName);
        return wrapper;
    }

    /**
     * 分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<StoreGradeDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 导出
     *
     * @param params
     * @return
     */

    @Override
    public List<StoreGradeDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 根据ID查询店铺等级
     *
     * @param id
     * @return
     */

    @Override
    public StoreGradeDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 根据ID查询店铺等级名称
     *
     * @param id
     * @return
     */

    @Override
    public String getNameById(Long id) {
        StoreGradeEntity storeGradeEntity = baseDao.selectById(id);
        if (null != storeGradeEntity && StringUtils.isNotBlank(storeGradeEntity.getSgName())) {
            return storeGradeEntity.getSgName();
        }
        return null;
    }

    /**
     * 保存店铺等级
     *
     * @param dto
     */

    @Override
    public void save(@RequestBody StoreGradeSaveDTO dto) {

        StoreGradeEntity storeGradeEntity = ConvertUtils.sourceToTarget(dto, StoreGradeEntity.class);
        this.insert(storeGradeEntity);

    }


    /**
     * 修改店铺等级
     *
     * @param dto
     */

    @Override
    public void update(@RequestBody StoreGradeUpdateDTO dto) {
        StoreGradeEntity storeGradeEntity = ConvertUtils.sourceToTarget(dto, StoreGradeEntity.class);
        this.updateById(storeGradeEntity);
    }

    /**
     * 根据ID删除店铺等级
     *
     * @param ids id 数组
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 启用/禁用店铺
     *
     * @param storeShowFlagUpdateDTO
     */

    @Override
    public void updateShowFlag(@RequestBody StoreShowFlagUpdateDTO storeShowFlagUpdateDTO) {
        StoreGradeEntity storeGradeEntity = ConvertUtils.sourceToTarget(storeShowFlagUpdateDTO, StoreGradeEntity.class);
        baseDao.updateById(storeGradeEntity);
    }

    /**
     * 校验等级名称是否重复
     *
     * @param params
     * @return
     */

    @Override
    public Integer checkGradeName(@RequestParam Map<String, Object> params) {
        Long id = (Long) params.get("id");
        String sgName = (String) params.get("sgName");
        return baseDao.selectCount(new QueryWrapper<StoreGradeEntity>()
                .eq(StringUtils.isNotBlank(sgName), "sg_name", sgName)
                .notIn(id != null, "id", id));
    }

    /**
     * 获取发布商品数量最少的店铺等级
     *
     * @return
     */

    @Override
    public Long selectGradeId() {
        return baseDao.selectGradeId();
    }
}
