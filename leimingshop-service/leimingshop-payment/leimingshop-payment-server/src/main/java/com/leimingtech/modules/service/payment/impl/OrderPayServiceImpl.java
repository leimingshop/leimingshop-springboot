/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dao.payment.OrderPayDao;
import com.leimingtech.modules.dto.payment.OrderPayDTO;
import com.leimingtech.modules.dto.payment.OrderToPayDTO;
import com.leimingtech.modules.entity.payment.OrderPayEntity;
import com.leimingtech.modules.enums.order.PayStateEnum;
import com.leimingtech.modules.service.payment.OrderPayService;
import com.leimingtech.modules.statuscode.PaymentStatusCode;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 订单支付管理
 * @Date :2019/6/18 10:25
 * @Version V1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderPayServiceImpl extends CrudServiceImpl<OrderPayDao, OrderPayEntity, OrderPayDTO> implements OrderPayService {

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Override
    public QueryWrapper<OrderPayEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<OrderPayEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public PageData<OrderPayDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有订单支付信息
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public List<OrderPayDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询订单支付信息
     * @Date :2019/5/28 19:52
     * @Param id 订单支付id
     * @Version V1.0
     **/

    @Override
    public OrderPayDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 功能描述:
     * 〈根据支付单号查询未支付信息〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    @Override
    public OrderPayDTO getNoByPaySn(@RequestParam("paySn") Long paySn) {
        return baseDao.getNoByPaySn(paySn);
    }

    /**
     * 功能描述:
     * 〈根据支付单号查询支付信息〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    @Override
    public OrderPayDTO getByPaySn(Long paySn) {
        QueryWrapper<OrderPayEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(paySn != null, "pay_sn", paySn);
        OrderPayEntity orderPayEntity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(orderPayEntity, OrderPayDTO.class);
    }


    /**
     * 功能描述:
     * 〈根据第三方支付单号查询支付信息〉
     *
     * @param transactionId 第三方交易单号
     * @author : 匡卫国
     */

    @Override
    public OrderPayDTO getByTransactionId(String transactionId) {
        QueryWrapper<OrderPayEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(transactionId != null, "transaction_id", transactionId);
        OrderPayEntity orderPayEntity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(orderPayEntity, OrderPayDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 保存订单支付信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public Boolean saveOrderPay(@RequestBody OrderPayDTO dto) {
        OrderPayEntity entity = ConvertUtils.sourceToTarget(dto, OrderPayEntity.class);
        return insert(entity);
    }

    /**
     * @Author: weixianchun
     * @Description: 修改订单支付信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public void update(@RequestBody OrderPayDTO dto) {
        super.update(dto);
    }

    /**
     * 功能描述:
     * 〈取消支付〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    @Override
    public Boolean cancelOrderPay(Long paySn) {
        // 修改条件未付款
        UpdateWrapper<OrderPayEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("pay_sn", paySn)
                .eq("pay_state", PayStateEnum.NO.value());
        // 设置修改后状态
        OrderPayEntity orderPayEntity = new OrderPayEntity();
        orderPayEntity.setPayState(PayStateEnum.CANCEL.value());

        int count = baseDao.update(orderPayEntity, updateWrapper);
        return count > 0;
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id删除订单支付信息
     * @Date :2019/5/28 19:53
     * @Param ids 订单支付id
     * @Version V1.0
     **/

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 功能描述:
     * 〈根据id删除订单支付信息〉
     *
     * @param id 订单支付id
     * @author : 刘远杰
     */

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    /**
     * 功能描述:
     * 〈修改订单支付信息〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    @Override
    public int updatePayStateByPaySn(@RequestParam("paySn") Long paySn, @RequestParam("transactionId") String transactionId) {
        // 查询条件
        UpdateWrapper<OrderPayEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq(paySn != null, "pay_sn", paySn);

        // 更新内容
        OrderPayEntity orderPayEntity = new OrderPayEntity();
        orderPayEntity.setPayState(PayStateEnum.Yes.value());
        orderPayEntity.setTransactionId(transactionId);

        return baseDao.update(orderPayEntity, wrapper);
    }

    /**
     * 功能描述:
     * 〈根据支付单号删除订单支付信息〉
     *
     * @param paySn
     * @return : void
     * @author : 刘远杰
     */

    @Override
    public int deleteByPaySn(@RequestParam("paySn") Long paySn) {
        QueryWrapper<OrderPayEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(paySn != null, "pay_sn", paySn);
        return baseDao.delete(wrapper);
    }


    /**
     * 保存订单支付信息
     *
     * @param memberId: 会员ID
     * @return 保存成功的订单支付信息
     * @date 2019/6/22 20:22
     * @author LX lixiangx@leimingtech.com
     **/
    @Override

    public OrderPayDTO saveOrderPay(@RequestParam("memberId") Long memberId,
                                    @RequestParam("orderSn") Long orderSn,
                                    @RequestParam("payAmount") BigDecimal payAmount,
                                    @RequestParam("goodsName") String goodsName,
                                    @RequestParam(value = "cancelOrderTime", required = false) Long cancelOrderTime) {
        OrderPayDTO orderPayDTO = new OrderPayDTO();
        orderPayDTO.setBuyerId(memberId);
        orderPayDTO.setOrderSn(orderSn);
        orderPayDTO.setPaySn(IdGenerator.defaultSnowflakeId());
        orderPayDTO.setPayState(PayStateEnum.NO.value());
        orderPayDTO.setId(IdGenerator.defaultSnowflakeId());
        orderPayDTO.setPayAmount(payAmount);
        orderPayDTO.setGoodsName(goodsName);
        Date cancelDate = new Date(System.currentTimeMillis() + cancelOrderTime);
        orderPayDTO.setCancalDate(cancelDate);
        this.save(orderPayDTO);
        return orderPayDTO;
    }

    /**
     * 定时取消订单
     */

    @Override
    public void canceledOrderTiming() {
        //获取当前时间
        Date nowDate = new Date();
        //查询可删除的订单单号
        List<Long> orderSns = baseDao.getOrderByDate(nowDate);
        log.info("定时取消订单编号：{}", orderSns);
        // 修改所有订单支付状态
//        baseDao.cancelOrderPayByTime(nowDate);
        if (CollectionUtils.isNotEmpty(orderSns)) {
            //发送消息
            Boolean flag = rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CANCEL_ORDERLOG_QUEUE, JSON.toJSONString(orderSns));
            if (!flag) {
                // 发送mq失败
                log.info("定时取消订单失败，调用mq修改订单状态失败");
                throw new ServiceException(PaymentStatusCode.ORDER_CANCEL_TIMING_UPDATE_ORDER_FAIL);
            }
        }
    }

    /**
     * 功能描述:
     * 〈待付款订单去支付〉
     *
     * @param paySn   支付单号
     * @param buyerId 买家id
     * @return :
     * @author : 刘远杰
     */

    @Override
    public OrderToPayDTO payOrder(@RequestParam("paySn") Long paySn, @RequestParam("buyerId") Long buyerId) {
        Map<String, Object> result = new HashMap<>();
        OrderToPayDTO orderToPayDTO = new OrderToPayDTO();
        log.info("判断订单支付状态，获取订单支付信息，paySn:{}", paySn);
        OrderPayDTO orderPay = this.getNoByPaySn(paySn);

        if (orderPay == null) {
            throw new ServiceException(PaymentStatusCode.TO_PAY_NO_PAY);
        } else {
            // 封装前台接受数据
            orderToPayDTO.setPaySn(orderPay.getPaySn());
            orderToPayDTO.setCreateDate(orderPay.getCreateDate());
            orderToPayDTO.setOrderAmount(orderPay.getPayAmount());
            orderToPayDTO.setCancelDate(orderPay.getCancalDate());
            orderToPayDTO.setBalanceAmount(orderPay.getBalanceAmount());
            return orderToPayDTO;
        }
    }


    @Override
    public void cancelOrderPayByOrderSnList(@RequestBody List<Long> orderSnList) {
        // 修改所有订单支付状态
        super.update(Wrappers.<OrderPayEntity>lambdaUpdate()
                .set(OrderPayEntity::getPayState, PayStateEnum.CANCEL.value())
                .in(OrderPayEntity::getOrderSn, orderSnList)
                .in(OrderPayEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }
}
