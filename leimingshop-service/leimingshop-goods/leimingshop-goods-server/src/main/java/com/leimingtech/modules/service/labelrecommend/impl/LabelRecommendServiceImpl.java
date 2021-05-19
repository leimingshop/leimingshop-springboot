/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.labelrecommend.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.labelrecommend.LabelRecommendDao;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendDTO;
import com.leimingtech.modules.entity.labelrecommend.LabelRecommendEntity;
import com.leimingtech.modules.service.labelrecommend.LabelRecommendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 标签推荐表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class LabelRecommendServiceImpl extends BaseServiceImpl<LabelRecommendDao, LabelRecommendEntity> implements LabelRecommendService {

    @Override

    public PageData<LabelRecommendDTO> page(@RequestParam Map<String, Object> params) {
        IPage<LabelRecommendEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, LabelRecommendDTO.class);
    }

    @Override

    public List<LabelRecommendDTO> list(@RequestParam Map<String, Object> params) {
        List<LabelRecommendEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, LabelRecommendDTO.class);
    }

    private QueryWrapper<LabelRecommendEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String labelName = (String) params.get("labelName");

        QueryWrapper<LabelRecommendEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(labelName), "label_name", labelName);

        return wrapper;
    }

    @Override

    public LabelRecommendDTO get(Long id) {
        LabelRecommendEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, LabelRecommendDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody LabelRecommendDTO dto) {
        LabelRecommendEntity entity = ConvertUtils.sourceToTarget(dto, LabelRecommendEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody LabelRecommendDTO dto) {
        LabelRecommendEntity entity = ConvertUtils.sourceToTarget(dto, LabelRecommendEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {

        //逻辑删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * <修改标签状态>
     *
     * @param labelStatus 标签状态(启用,禁用)
     * @param id          标签推荐ID
     * @return 根据数量判断是否修改成功
     * @date 2020/1/9 15:29
     * @author weixianchun
     **/

    @Override
    public int updateLabelStatus(@RequestParam("labelStatus") Integer labelStatus, @RequestParam("id") Long id) {
        LabelRecommendEntity labelRecommendEntity = new LabelRecommendEntity();
        labelRecommendEntity.setId(id);
        labelRecommendEntity.setLabelStatus(labelStatus);

        return baseDao.updateById(labelRecommendEntity);
    }

    /**
     * 功能描述:
     * <保存时-根据标签名称 标识查询标签推荐信息>
     *
     * @param labelName 标签名称
     * @param labelFlag 标识
     * @date 2020/1/9 16:23
     * @author weixianchun
     **/

    @Override
    public LabelRecommendDTO findByName(@RequestParam("labelName") String labelName, @RequestParam("labelFlag") String labelFlag) {
        return baseDao.findByName(labelName, labelFlag);
    }

    /**
     * 功能描述:
     * <修改时-根据标签名称 标识查询标签推荐信息>
     *
     * @param labelName 标签名称
     * @param labelFlag 标识
     * @param id        标签id
     * @date 2020/1/9 16:23
     * @author weixianchun
     **/

    @Override
    public int findByLabelNameUpdate(@RequestParam("id") Long id, @RequestParam("labelName") String labelName, @RequestParam("labelFlag") String labelFlag) {
        return baseDao.findByLabelNameUpdate(id, labelName, labelFlag);
    }

}
