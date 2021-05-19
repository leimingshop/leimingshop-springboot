/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.reduce.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.reduce.ReduceGoodsDao;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.entity.reduce.ReduceGoodsEntity;
import com.leimingtech.modules.service.reduce.ReduceGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 满减活动商品管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Service

public class ReduceGoodsServiceImpl extends BaseServiceImpl<ReduceGoodsDao, ReduceGoodsEntity> implements ReduceGoodsService {

    @Override

    public PageData<ReduceGoodsDTO> page(@RequestParam Map<String, Object> params) {
        IPage<ReduceGoodsEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ReduceGoodsDTO.class);
    }

    @Override

    public List<ReduceGoodsDTO> list(@RequestParam Map<String, Object> params) {
        List<ReduceGoodsEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ReduceGoodsDTO.class);
    }

    private QueryWrapper<ReduceGoodsEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<ReduceGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public ReduceGoodsDTO get(Long id) {
        ReduceGoodsEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ReduceGoodsDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody ReduceGoodsDTO dto) {
        ReduceGoodsEntity entity = ConvertUtils.sourceToTarget(dto, ReduceGoodsEntity.class);

        insert(entity);
    }

    @Override

    public void update(@RequestBody ReduceGoodsDTO dto) {
        ReduceGoodsEntity entity = ConvertUtils.sourceToTarget(dto, ReduceGoodsEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * @Author weixianchun
     * @Description 批量保存满减商品关联信息
     * @Param activityId
     * @Date 2019/12/26 14:11
     * @Return void
     * @version 1.0
     */

    @Override
    public Boolean saveBatch(@RequestBody List<ReduceGoodsDTO> dtoList) {
        List<ReduceGoodsEntity> entityList = ConvertUtils.sourceToTarget(dtoList, ReduceGoodsEntity.class);
        return super.insertBatch(entityList);
    }

    /**
     * @Author weixianchun
     * @Description 根据活动ID删除商品关联信息
     * @Param activityId
     * @Date 2019/12/26 14:11
     * @Return void
     * @version 1.0
     */

    @Override
    public void deleteByActivityId(@RequestParam("activityId") Long activityId) {
        UpdateWrapper<ReduceGoodsEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activity_id", activityId);
        baseDao.delete(updateWrapper);
    }

    /**
     * 功能描述:
     * 〈获取满减活动关联的商品集合〉
     *
     * @param activityId 满减活动id
     * @author : 刘远杰
     */

    @Override
    public List<ReduceGoodsDTO> getByActivityId(Long activityId) {
        QueryWrapper<ReduceGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        List<ReduceGoodsEntity> entityList = baseDao.selectList(queryWrapper);
        List<ReduceGoodsDTO> collectList = entityList.stream().map(entity -> {
            ReduceGoodsDTO reduceGoodsDTO = new ReduceGoodsDTO();
            BeanCopier.create(ReduceGoodsEntity.class, ReduceGoodsDTO.class, false)
                    .copy(entity, reduceGoodsDTO, null);
            return reduceGoodsDTO;
        }).collect(Collectors.toList());
        return collectList;
    }

}
