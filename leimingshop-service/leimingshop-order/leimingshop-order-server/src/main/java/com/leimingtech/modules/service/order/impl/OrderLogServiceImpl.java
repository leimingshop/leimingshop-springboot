/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.order.OrderLogDao;
import com.leimingtech.modules.dto.order.OrderLogDTO;
import com.leimingtech.modules.entity.order.OrderLogEntity;
import com.leimingtech.modules.service.order.OrderLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 订单状态记录管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Service
@Transactional

public class OrderLogServiceImpl extends CrudServiceImpl<OrderLogDao, OrderLogEntity, OrderLogDTO> implements OrderLogService {

    /**
     * 功能描述:
     * 〈保存订单操作日志〉
     *
     * @param dto 订单操作日志
     * @author : 刘远杰
     */

    @Override
    public void saveOrderLog(@RequestBody OrderLogDTO dto) {
        super.save(dto);
    }

    /**
     * 功能描述:
     * 〈根据订单日志id查询订单日志详情〉
     *
     * @param id 订单日志id
     * @author : 刘远杰
     */

    @Override
    public OrderLogDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 功能描述:
     * 〈根据订单id查询订单日志详情〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    @Override
    public List<OrderLogDTO> getByOrderId(Long orderId) {
        QueryWrapper<OrderLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(orderId != null, "order_id", orderId);
        List<OrderLogEntity> entities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(entities, OrderLogDTO.class);
    }


    @Override
    public void deleteByOrderId(Long orderId) {
        QueryWrapper<OrderLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(orderId != null, "order_id", orderId);
        baseDao.delete(wrapper);
    }

    @Override
    public QueryWrapper<OrderLogEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<OrderLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}