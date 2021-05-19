/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.order.OrderLogisticsLogDao;
import com.leimingtech.modules.dto.order.*;
import com.leimingtech.modules.entity.order.OrderLogisticsLogEntity;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderLogisticsLogService;
import com.leimingtech.modules.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单物流消息记录管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Service
@Transactional
@Slf4j
public class OrderLogisticsLogServiceImpl extends CrudServiceImpl<OrderLogisticsLogDao, OrderLogisticsLogEntity, OrderLogisticsLogDTO>
        implements OrderLogisticsLogService {
    @Autowired
    private NosqlService nosqlService;

    @Autowired

    private OrderGoodsService orderGoodsService;

    @Autowired

    private OrderService orderService;

    /**
     * 功能描述:
     * 〈物流消息记录分页〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<OrderLogisticsLogDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 功能描述:
     * 〈根据物流消息主键查询物流消息记录〉
     *
     * @param id 物流消息主键id
     * @author : 刘远杰
     */

    @Override
    public OrderLogisticsLogDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 功能描述:
     * 〈根据orderId查询物流消息记录〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    @Override
    public List<OrderLogisticsLogDTO> getByOrderId(Long orderId,
                                                   @RequestParam(value = "buyerId", required = false) Long buyerId) {
        QueryWrapper<OrderLogisticsLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(orderId != null, "order_id", orderId);
        wrapper.eq(buyerId != null, "buyer_id", buyerId);
        List<OrderLogisticsLogEntity> entities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(entities, OrderLogisticsLogDTO.class);
    }

    /**
     * 功能描述:
     * 〈根据shipmentNumber查询物流消息记录〉
     *
     * @param shipmentNumber 物流单号
     * @author : 刘远杰
     */

    @Override
    public List<OrderLogisticsLogDTO> getByShipmentNumber(String shipmentNumber) {
        QueryWrapper<OrderLogisticsLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(shipmentNumber), "shipment_number", shipmentNumber);
        List<OrderLogisticsLogEntity> entities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(entities, OrderLogisticsLogDTO.class);
    }

    /**
     * 功能描述:
     * 〈根据主键删除物流消息记录〉
     *
     * @param id 物流消息主键id
     * @author : 刘远杰
     */

    @Override
    public boolean deleteById(Long id) {
        return super.deleteById(id);
    }

    /**
     * 功能描述:
     * 〈根据orderId删除物流消息记录〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    @Override
    public int deleteByOrderId(Long orderId) {
        QueryWrapper<OrderLogisticsLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(orderId != null, "order_id", orderId);
        return baseDao.delete(wrapper);
    }

    /**
     * 功能描述:
     * 〈根据shipmentNumber删除物流消息记录〉
     *
     * @param shipmentNumber 物流单号
     * @author : 刘远杰
     */

    @Override
    public int deleteByShipmentNumber(String shipmentNumber) {
        QueryWrapper<OrderLogisticsLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(shipmentNumber), "shipment_number", shipmentNumber);
        return baseDao.delete(wrapper);
    }

    /**
     * 功能描述:
     * 〈保存订单物流消息记录〉
     *
     * @param dto 订单物流消息
     * @author : 刘远杰
     */

    @Override
    public void saveOrderLogisticsLogService(@RequestBody OrderLogisticsLogDTO dto) {
        super.save(dto);
    }

    /**
     * 根据用户id查询最新一条物流信息
     *
     * @param id
     * @return
     */

    @Override
    public LastestOrderLogisticsDTO findNewOrderLogisticsLog(Long id) {
        LastestOrderLogisticsDTO lastestOrderLogisticsDTO = new LastestOrderLogisticsDTO();
        // 根据用户id查询最新一条订单未完成的数据
        Long orderId = orderService.findLastNoComplete(id);
        if (null == orderId) {
            return lastestOrderLogisticsDTO;
        }
        OrderLogisticsDTO logistics = this.findLogistics(String.valueOf(orderId));
        if (null != logistics) {
            lastestOrderLogisticsDTO.setGoodsPicture(logistics.getGoodsPicture());
            if (CollectionUtils.isNotEmpty(logistics.getData())) {
                lastestOrderLogisticsDTO.setContext(logistics.getData().get(0).getContext());
                lastestOrderLogisticsDTO.setState(logistics.getState());
            }
            lastestOrderLogisticsDTO.setOrderId(orderId);
        }
        return lastestOrderLogisticsDTO;
    }

    @Override

    public OrderLogisticsDTO findLogistics(String orderId) {
        OrderLogisticsDTO orderLogisticsDTO = new OrderLogisticsDTO();
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", orderId);
        // 查询订单信息
        OrderDTO orderDTO = orderService.getOrderByMap(orderMap);
        String logisticsNo = orderDTO.getTransportCode();
        if (StringUtils.isEmpty(logisticsNo)) {
            return null;
        }
        orderLogisticsDTO.setNu(orderDTO.getTransportCode());
        orderLogisticsDTO.setCompanyName(orderDTO.getTransportCompanyName());
        orderLogisticsDTO.setCompanyPhone(orderDTO.getTransportCompanyPhone());

        // 查询订单商品信息
        List<OrderGoodsDTO> orderGoodsDTOList = orderGoodsService.getByOrderId(Long.valueOf(orderId), null, null);
        Integer goodsNum = 0;
        String goodsPicture = "";
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
            goodsNum = goodsNum + orderGoodsDTO.getGoodsNum();
            goodsPicture = orderGoodsDTO.getGoodsImage();
        }
        // 从mongodb中查询数据
        Map<String, Object> map = new HashMap<>();
        map.put("_id", logisticsNo);
        List<String> logistics_message = nosqlService.queryData("logistics_message", map);
        if (CollectionUtils.isNotEmpty(logistics_message)) {
            orderLogisticsDTO = JSONObject.parseObject(logistics_message.get(0), OrderLogisticsDTO.class);
            if (null == orderLogisticsDTO.getCompanyName()) {
                orderLogisticsDTO.setCompanyName(orderDTO.getTransportCompanyName());
                orderLogisticsDTO.setCompanyPhone(orderDTO.getTransportCompanyPhone());
            }
        }
        orderLogisticsDTO.setGoodsNum(goodsNum);
        orderLogisticsDTO.setGoodsPicture(goodsPicture);

        return orderLogisticsDTO;
    }

    @Override
    public QueryWrapper<OrderLogisticsLogEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        Long buyerId = (Long) params.get("buyerId");
        Long orderId = (Long) params.get("orderId");
        Integer logisticsState = (Integer) params.get("logisticsState");
        Integer orderStatus = (Integer) params.get("orderStatus");
        Integer resultStatus = (Integer) params.get("resultStatus");
        Integer isCheck = (Integer) params.get("isCheck");
        String companyNumber = (String) params.get("companyNumber");
        String shipmentNumber = (String) params.get("shipmentNumber");

        QueryWrapper<OrderLogisticsLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(buyerId != null, "buyer_id", buyerId);
        wrapper.eq(orderId != null, "order_id", orderId);
        wrapper.eq(logisticsState != null, "logistics_state", logisticsState);
        wrapper.eq(orderStatus != null, "order_status", orderStatus);
        wrapper.eq(resultStatus != null, "result_status", resultStatus);
        wrapper.eq(isCheck != null, "is_check", isCheck);
        wrapper.like(StringUtils.isNotBlank(companyNumber), "companyNumber", companyNumber);
        wrapper.eq(StringUtils.isNotBlank(shipmentNumber), "shipment_number", shipmentNumber);

        return wrapper;
    }


}
