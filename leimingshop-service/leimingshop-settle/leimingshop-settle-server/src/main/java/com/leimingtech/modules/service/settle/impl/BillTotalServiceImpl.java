/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.settle.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.dto.setting.OrderBillSettingDTO;
import com.leimingtech.dto.setting.SettingDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.modules.aftersale.service.AftersaleApplyService;
import com.leimingtech.modules.dao.settle.BillTotalDao;
import com.leimingtech.modules.dto.grade.StoreGradeDTO;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.dto.settle.*;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.entity.settle.BillTotalEntity;
import com.leimingtech.modules.enums.settle.SettleEnum;
import com.leimingtech.modules.service.grade.StoreGradeService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.settle.BillLogService;
import com.leimingtech.modules.service.settle.BillTotalService;
import com.leimingtech.modules.service.settle.OrderBillService;
import com.leimingtech.modules.service.settle.ReturnBillService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.service.setting.SettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;

/**
 * 对账汇总表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class BillTotalServiceImpl extends BaseServiceImpl<BillTotalDao, BillTotalEntity> implements BillTotalService {

    @Autowired
    private SettingService settingService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreGradeService storeGradeService;

    @Autowired
    private OrderBillService orderBillService;

    @Autowired
    private AftersaleApplyService aftersaleApplyService;

    @Autowired
    private ReturnBillService returnBillService;

    @Autowired
    private BillLogService billLogService;

    /**
     * 分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<BillTotalDTO> page(@RequestParam Map<String, Object> params) {
        IPage<BillTotalEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, BillTotalDTO.class);
    }

    /**
     * 查询所有
     *
     * @param params
     * @return
     */
    @Override

    public List<BillTotalDTO> list(@RequestParam Map<String, Object> params) {
        List<BillTotalEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, BillTotalDTO.class);
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<BillTotalEntity> getWrapper(Map<String, Object> params) {
        String billTotalSn = (String) params.get("billTotalSn");
        String storeName = (String) params.get("storeName");
        String storeId = (String) params.get("storeId");
        String storeType = (String) params.get("storeType");
        String confirmStatus = (String) params.get("confirmStatus");
        String billState = (String) params.get("billState");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");

        QueryWrapper<BillTotalEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(billTotalSn), "bill_total_sn", billTotalSn);
        wrapper.like(StringUtils.isNotBlank(storeName), "store_name", storeName);
        wrapper.like(StringUtils.isNotBlank(storeId), "store_id", storeId);
        wrapper.eq(StringUtils.isNotBlank(storeType), "store_type", storeType);
        wrapper.eq(StringUtils.isNotBlank(confirmStatus), "confirm_status", confirmStatus);
        wrapper.eq(StringUtils.isNotBlank(billState), "bill_state", billState);
        wrapper.ge(StringUtils.isNotBlank(startTime), "bill_time", startTime + " 00:00:00");
        wrapper.le(StringUtils.isNotBlank(endTime), "bill_time", endTime + " 23:59:59");

        return wrapper;
    }

    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */
    @Override

    public BillTotalDTO get(Long id) {
        BillTotalEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, BillTotalDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody BillTotalDTO dto) {
        BillTotalEntity entity = ConvertUtils.sourceToTarget(dto, BillTotalEntity.class);

        insert(entity);
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody BillTotalDTO dto) {
        BillTotalEntity entity = ConvertUtils.sourceToTarget(dto, BillTotalEntity.class);
        updateById(entity);

        if (entity.getBillState() == 1) {
            //保存操作记录
            BillLogDTO billLogDTO = new BillLogDTO();
            billLogDTO.setOperator(entity.getUpdater());
            billLogDTO.setStoreName("运营平台");
            billLogDTO.setBillId(entity.getId());
            billLogDTO.setOperatorType("已结算该对账单款项");
            billLogService.save(billLogDTO);
        }

    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, BillTotalEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 定时生成对账
     */

    @Override
    public void timeBill() {
        // 获取对账周期
        SettingDTO settingDTO = settingService.queryByName(SettingsEnum.ORDER_BILL.value());
        String substringBefore = StringUtils.substringBefore(settingDTO.getValue(), "*");

        if (StringUtils.isBlank(substringBefore)) {
            return;
        }

        OrderBillSettingDTO orderBill = JSON.parseObject(substringBefore, OrderBillSettingDTO.class);

        // 获取对账记录中最大的对账日期
        Date date = baseDao.findMaxDate();

        if (orderBill.getType() == 1) {
            // 天
            String[] split = orderBill.getDays().split(",");

            //获取当前时间
            Calendar cal = Calendar.getInstance();
            String day = String.valueOf(cal.get(Calendar.DATE));

            boolean contains = Arrays.asList(split).contains(day);

            if (!contains) {
                return;
            }
            Date endDate = DateUtils.getToday();
            if (endDate.compareTo(new Date()) <= 0) {
                monthBill(date, endDate);
            }

        } else {
            Date start = null;
            if (date == null) {
                start = orderBill.getStartTime();
            } else {
                start = date;
            }

            Date endDate;
            if (orderBill.getType() == SettleEnum.TYPE_MONTH.value()) {
                endDate = DateUtils.addDateWeeks(start, Integer.parseInt(orderBill.getDays()));
            } else {
                endDate = DateUtils.addDateMonths(start, Integer.parseInt(orderBill.getDays()));
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            endDate = cal.getTime();

            if (endDate.compareTo(new Date()) <= 0) {
                monthBill(date, endDate);
            }

        }
    }

    /**
     * 生成账单
     *
     * @param startDate
     */
    private void monthBill(Date startDate, Date endDate) {

        List<OrderBillDTO> orderBillDTOList = new ArrayList<>();
        List<BillTotalDTO> billTotalDTOList = new ArrayList<>();
        String start;
        // 查询日期内的所有订单
        if (startDate != null) {
            start = DateUtils.format(startDate, DateUtils.DATE_TIME_PATTERN);
        } else {
            start = "";
        }
        String end = DateUtils.format(endDate, DateUtils.DATE_TIME_PATTERN);
        // 查询出所有的店铺
        List<Long> storeList = storeService.findStoreAll();
        storeList.stream().forEach(storeId -> {

            StoreDTO storeDTO = storeService.get(storeId);
            StoreGradeDTO storeGradeDTO = storeGradeService.get(storeDTO.getGradeId());
            BigDecimal storeCost = storeGradeDTO.getSgPrice().multiply(BigDecimal.valueOf(storeGradeDTO.getBrokerageScale() / 100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            List<OrderDTO> orderDTOList = orderService.findBillOrderList(storeId, start, end);

            if (CollectionUtils.isEmpty(orderDTOList)) {
                List<ReturnBillDTO> returnBillDTOList = aftersaleApplyService.findBillOrderList(storeId, start, end);
                createEmpty(startDate, endDate, storeDTO, returnBillDTOList, storeCost, storeGradeDTO.getSgPrice());
                return;
            }

            // 佣金
            BigDecimal cost = BigDecimal.ZERO;
            // 订单总金额
            BigDecimal totalAmount = BigDecimal.ZERO;
            // 退款总金额
            BigDecimal returnAmount = BigDecimal.ZERO;
            // 退款佣金
            BigDecimal returnCost = BigDecimal.ZERO;
            // 店铺总活动费用
            BigDecimal storeCostTotals = BigDecimal.ZERO;


            BillTotalDTO billTotalDTO = new BillTotalDTO();
            billTotalDTO.setStoreId(storeId);
            billTotalDTO.setId(IdWorker.getId());

            for (OrderDTO orderDTO : orderDTOList) {
                OrderBillDTO orderBillDTO = new OrderBillDTO();
                orderBillDTO.setOrderSn(orderDTO.getOrderSn());
                orderBillDTO.setBillTotalId(billTotalDTO.getId());
                orderBillDTO.setOrderTotals(orderDTO.getGoodsAmount());
                orderBillDTO.setPaymentName(orderDTO.getPaymentName());
                orderBillDTO.setPayTime(orderDTO.getPaymentTime());
                orderBillDTO.setOrderId(orderDTO.getId());

                //TODO  计算总积分
                orderBillDTO.setPointAmount(BigDecimal.ZERO);

                // 店铺活动总费用
                orderBillDTO.setStoreCostTotals(orderDTO.getPreferentialPrice());
                storeCostTotals = storeCostTotals.add(orderBillDTO.getStoreCostTotals());

                // 佣金费用
                BigDecimal divide = orderDTO.getOrderAmount().divide(storeGradeDTO.getSgPrice());
                orderBillDTO.setCommisTotals(divide.multiply(storeCost).setScale(2, BigDecimal.ROUND_HALF_UP));

                // 总佣金
                cost = cost.add(orderBillDTO.getCommisTotals());

                // 订单总金额
                totalAmount = totalAmount.add(orderBillDTO.getOrderTotals());

                orderBillDTOList.add(orderBillDTO);
            }
            //----------------------退款信息--------------------------------------------
            List<ReturnBillDTO> returnBillDTOList = aftersaleApplyService.findBillOrderList(storeId, start, end);
            if (CollectionUtils.isNotEmpty(returnBillDTOList)) {
                for (ReturnBillDTO returnBillDTO : returnBillDTOList) {
                    returnBillDTO.setBillTotalId(billTotalDTO.getId());
                    // todo 计算退还积分

                    // 退款总佣金
                    BigDecimal returnDivide = returnBillDTO.getReturnAmount().divide(storeGradeDTO.getSgPrice());
                    BigDecimal bigDecimal = returnDivide.multiply(storeCost).setScale(2, BigDecimal.ROUND_HALF_UP);
                    returnCost = returnCost.add(bigDecimal);
                    //退款总金额
                    returnAmount = returnAmount.add(returnBillDTO.getReturnAmount());
                    if (StringUtils.isNotBlank(returnBillDTO.getPaymentName()) && Integer.valueOf(returnBillDTO.getPaymentName()) <= 4) {
                        returnBillDTO.setPaymentName("微信支付");
                    } else if (Integer.valueOf(returnBillDTO.getPaymentName()) == 8) {
                        returnBillDTO.setPaymentName("余额支付");
                    } else {
                        returnBillDTO.setPaymentName("支付宝支付");
                    }
                }
                returnBillService.saveBatch(returnBillDTOList);
            }
            if (startDate == null) {
                billTotalDTO.setObtStartTime(storeDTO.getCreateDate());
            } else {
                billTotalDTO.setObtStartTime(startDate);
            }
            billTotalDTO.setBillTotalSn(String.valueOf(IdWorker.getId()));
            billTotalDTO.setObtEndTime(endDate);
            billTotalDTO.setBillTime(endDate);
            billTotalDTO.setStoreName(orderDTOList.get(0).getStoreName());
            billTotalDTO.setStoreType(storeDTO.getStoreType());
            billTotalDTO.setConfirmStatus(0);
            billTotalDTO.setBillState(0);
            billTotalDTO.setCommisTotal(cost);
            billTotalDTO.setPointTotal(BigDecimal.ZERO);
            billTotalDTO.setReturnCommisTotal(returnCost);
            billTotalDTO.setStoreCostTotal(storeCostTotals);
            billTotalDTO.setReturnAmount(returnAmount);
            billTotalDTO.setOrderTotalAmount(totalAmount);

            //应结金额
            // todo 加减积分
            BigDecimal subtract = totalAmount.subtract(cost)
                    .subtract(returnAmount)
                    .add(returnCost).subtract(storeCostTotals);

            subtract.setScale(2, BigDecimal.ROUND_HALF_UP);
            billTotalDTO.setResultTotals(subtract);

            billTotalDTOList.add(billTotalDTO);
        });
        //保存
        insertBatch(ConvertUtils.sourceToTarget(billTotalDTOList, BillTotalEntity.class));
        orderBillService.saveBatch(orderBillDTOList);
    }

    /**
     * 对账详情
     *
     * @param id
     * @return
     */

    @Override
    public BillTotalInfoDTO info(@RequestParam("id") Long id) {
        BillTotalInfoDTO billTotalInfoDTO = new BillTotalInfoDTO();
        BillTotalDTO billTotalDTO = get(id);
        Map<String, Object> params = new HashMap<>(10);
        params.put("billTotalId", billTotalDTO.getBillTotalSn());

        List<ReturnBillDTO> returnBillDTOList = returnBillService.list(params);
        List<OrderBillDTO> orderBillDTOList = orderBillService.list(params);
        List<BillLogDTO> list = billLogService.list(params);
        billTotalInfoDTO.setList(list);
        billTotalInfoDTO.setBillTotalDTO(billTotalDTO);
        billTotalInfoDTO.setOrderBillDTO(orderBillDTOList);
        billTotalInfoDTO.setReturnBillDTO(returnBillDTOList);
        return billTotalInfoDTO;
    }

    /**
     * 店铺备注和确认
     *
     * @param storeConfirmBillTotalDTO
     */

    @Override
    public void updateStatus(@RequestBody StoreConfirmBillTotalDTO storeConfirmBillTotalDTO,
                             @RequestParam("userName") String userName) {
        BillTotalEntity billTotalEntity = ConvertUtils.sourceToTarget(storeConfirmBillTotalDTO, BillTotalEntity.class);
        updateById(billTotalEntity);
        BillLogDTO billLogDTO = new BillLogDTO();
        if (storeConfirmBillTotalDTO.getConfirmStatus() != null) {
            billLogDTO.setStoreId(billTotalEntity.getStoreId());
            billLogDTO.setOperatorType("确认结算账单");
            billLogDTO.setOperator(userName);
            billLogDTO.setBillId(billTotalEntity.getId());
            billLogDTO.setStoreName(billTotalEntity.getStoreName());
        }
        billLogService.save(billLogDTO);

    }

    private void createEmpty(Date startDate, Date endDate, StoreDTO storeDTO,
                             List<ReturnBillDTO> returnBillDTOList, BigDecimal storeCost, BigDecimal sgPrice) {

        BillTotalEntity billTotalDTO = new BillTotalEntity();
        billTotalDTO.setId(IdWorker.getId());

        if (CollectionUtils.isNotEmpty(returnBillDTOList)) {
            for (ReturnBillDTO returnBillDTO : returnBillDTOList) {
                returnBillDTO.setBillTotalId(billTotalDTO.getId());
                // todo 计算退还积分

                // 退款总佣金
                BigDecimal returnDivide = returnBillDTO.getReturnAmount().divide(sgPrice);
                BigDecimal bigDecimal = returnDivide.multiply(storeCost).setScale(2, BigDecimal.ROUND_HALF_UP);
                billTotalDTO.setReturnCommisTotal(billTotalDTO.getReturnCommisTotal().add(bigDecimal));
                //退款总金额
                billTotalDTO.setReturnAmount(billTotalDTO.getReturnAmount().add(returnBillDTO.getReturnAmount()));
            }
            returnBillService.saveBatch(returnBillDTOList);
        }
        if (startDate == null) {
            billTotalDTO.setObtStartTime(storeDTO.getCreateDate());
        } else {
            billTotalDTO.setObtStartTime(startDate);
        }
        billTotalDTO.setStoreId(storeDTO.getId());
        billTotalDTO.setObtEndTime(endDate);
        billTotalDTO.setBillTime(endDate);
        billTotalDTO.setStoreName(storeDTO.getStoreName());
        billTotalDTO.setStoreType(storeDTO.getStoreType());
        billTotalDTO.setConfirmStatus(0);
        billTotalDTO.setBillState(0);
        billTotalDTO.setCommisTotal(BigDecimal.ZERO);
        billTotalDTO.setPointTotal(BigDecimal.ZERO);
        billTotalDTO.setReturnCommisTotal(BigDecimal.ZERO);
        billTotalDTO.setStoreCostTotal(BigDecimal.ZERO);
        billTotalDTO.setReturnAmount(BigDecimal.ZERO);
        billTotalDTO.setResultTotals(BigDecimal.ZERO);
        billTotalDTO.setBillTotalSn(String.valueOf(IdWorker.getId()));
        billTotalDTO.getResultTotals().subtract(billTotalDTO.getReturnAmount()).subtract(billTotalDTO.getReturnCommisTotal());

        insert(billTotalDTO);
    }

}
