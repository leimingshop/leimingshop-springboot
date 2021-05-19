/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.labelrecommend.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.labelrecommend.LabelRecommendRelDao;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendRelDTO;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendRelSaveDTO;
import com.leimingtech.modules.entity.labelrecommend.LabelRecommendRelEntity;
import com.leimingtech.modules.service.labelrecommend.LabelRecommendRelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 标签推荐关联表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class LabelRecommendRelServiceImpl extends BaseServiceImpl<LabelRecommendRelDao, LabelRecommendRelEntity> implements LabelRecommendRelService {


    @Override

    public List<LabelRecommendRelDTO> list(@RequestParam Map<String, Object> params) {
        List<LabelRecommendRelEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, LabelRecommendRelDTO.class);
    }

    private QueryWrapper<LabelRecommendRelEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<LabelRecommendRelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public LabelRecommendRelDTO get(Long id) {
        LabelRecommendRelEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, LabelRecommendRelDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody LabelRecommendRelDTO dto) {
        LabelRecommendRelEntity entity = ConvertUtils.sourceToTarget(dto, LabelRecommendRelEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody LabelRecommendRelDTO dto) {
        LabelRecommendRelEntity entity = ConvertUtils.sourceToTarget(dto, LabelRecommendRelEntity.class);

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
     * <根据商品id删除标签关联的商品信息>
     *
     * @param goodsId 商品id
     * @return 根据数量判断是否删除成功
     * @date 2020/1/10 10:25
     * @author weixianchun
     **/
    @Override

    public int deleteByGoodsId(@RequestParam("goodsId") Long goodsId) {

        UpdateWrapper<LabelRecommendRelEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("goods_id", goodsId);
        baseDao.delete(updateWrapper);

        //逻辑删除
        return baseDao.deleteBatchIds(Collections.singletonList(goodsId));
    }

    /**
     * 功能描述:
     * <批量保存数据>
     *
     * @param dtoList
     * @return 判断是否添加成功
     * @date 2020/1/10 18:34
     * @author weixianchun
     **/

    @Override
    public boolean insertBatch(@RequestBody List<LabelRecommendRelSaveDTO> dtoList) {
        List<LabelRecommendRelEntity> recommendRelEntities = ConvertUtils.sourceToTarget(dtoList, LabelRecommendRelEntity.class);
        return this.insertBatch(recommendRelEntities, 100);
    }

}
