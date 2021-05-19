/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.order.OrderActivityDao;
import com.leimingtech.modules.dto.order.OrderActivityDTO;
import com.leimingtech.modules.entity.order.OrderActivityEntity;
import com.leimingtech.modules.service.order.OrderActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 订单活动表
 *
 * @author weixianchun@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Service
@Transactional

public class OrderActivityServiceImpl extends BaseServiceImpl<OrderActivityDao, OrderActivityEntity> implements OrderActivityService {


    @Override

    public List<OrderActivityDTO> list(@RequestParam Map<String, Object> params) {
        List<OrderActivityEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, OrderActivityDTO.class);
    }

    private QueryWrapper<OrderActivityEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<OrderActivityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public OrderActivityDTO get(Long id) {
        OrderActivityEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, OrderActivityDTO.class);
    }

    /**
     * 功能描述：
     * <根据订单id集合查询订单活动集合>
     *
     * @param orderIds 订单id集合
     * @return
     * @date 2020/2/24 13:34
     * @author 刘远杰
     **/
    @Override

    public List<OrderActivityDTO> getByOrderIds(@RequestBody List<Long> orderIds) {
        QueryWrapper<OrderActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_id", orderIds);
        List<OrderActivityEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, OrderActivityDTO.class);
    }

    /**
     * 功能描述：
     * <根据订单id查询订单活动集合>
     *
     * @param orderId 订单id
     * @return
     * @date 2020/2/24 13:34
     * @author 刘远杰
     **/
    @Override

    public List<OrderActivityDTO> getByOrderId(Long orderId) {
        QueryWrapper<OrderActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<OrderActivityEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, OrderActivityDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody OrderActivityDTO dto) {
        OrderActivityEntity entity = ConvertUtils.sourceToTarget(dto, OrderActivityEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody OrderActivityDTO dto) {
        OrderActivityEntity entity = ConvertUtils.sourceToTarget(dto, OrderActivityEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * 〈批量保存订单活动〉
     *
     * @param dtoList 订单活动
     * @author : 刘远杰
     */

    @Override
    public Boolean saveBatch(@RequestBody List<OrderActivityDTO> dtoList) {
        return super.insertBatch(ConvertUtils.sourceToTarget(dtoList, OrderActivityEntity.class));
    }

    /**
     * 功能描述:
     * (下单数量查询)
     *
     * @param activityType 活动类型 1优惠券 2满减
     * @param activityId   活动id
     * @return 下单数量
     * @date 2020/1/3 14:15
     * @author weixianchun
     **/

    @Override
    public int findOrderNum(@RequestParam("activityType") int activityType, @RequestParam("activityId") Long activityId) {
        return baseDao.findOrderNum(activityType, activityId);
    }

}
