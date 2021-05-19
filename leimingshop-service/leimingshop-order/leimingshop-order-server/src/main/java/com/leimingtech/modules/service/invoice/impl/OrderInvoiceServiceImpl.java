/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.invoice.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.dto.setting.SettingInvoiceDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.message.enums.EmailEnum;
import com.leimingtech.message.service.SysMailTemplateService;
import com.leimingtech.modules.dao.invoice.OrderInvoiceDao;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.invoice.*;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.dto.order.OrderGoodsDTO;
import com.leimingtech.modules.entity.invoice.OrderInvoiceEntity;
import com.leimingtech.modules.enums.order.InvoiceStatusEnum;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.invoice.InvoiceInfoService;
import com.leimingtech.modules.service.invoice.MemberInvoiceService;
import com.leimingtech.modules.service.invoice.OrderInvoiceService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.statuscode.InvoiceStatusCode;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Service
@Transactional

@Slf4j
public class OrderInvoiceServiceImpl extends BaseServiceImpl<OrderInvoiceDao, OrderInvoiceEntity> implements OrderInvoiceService {


    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private InvoiceInfoService invoiceInfoService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private SysMailTemplateService sysMailTemplateService;

    @Autowired
    private MemberInvoiceService memberInvoiceService;

    @Autowired
    private SettingService settingService;

    @Value("${image.base.url}")
    private String imgUrl;

    @Override

    public PageData<OrderInvoiceDTO> page(@RequestParam Map<String, Object> params) {
        if (params.get("storeId") == null) {
            return null;
        }
        IPage<OrderInvoiceEntity> page = getPage(params, "", false);
        List<OrderInvoiceDTO> invoiceDTOList = baseDao.selectInvoicePage(page, params);

        return new PageData<>(invoiceDTOList, page.getTotal());
    }

    @Override

    public List<OrderInvoiceDTO> list(Map<String, Object> params) {
        List<OrderInvoiceEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, OrderInvoiceDTO.class);
    }

    private QueryWrapper<OrderInvoiceEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<OrderInvoiceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.ne("company_type", 0);

        return wrapper;
    }

    @Override

    public OrderInvoiceDTO get(Long id) {
        OrderInvoiceEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, OrderInvoiceDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody OrderInvoiceDTO dto) {
        OrderInvoiceEntity entity = ConvertUtils.sourceToTarget(dto, OrderInvoiceEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void makeInvoice(@RequestBody OrderInvoiceDTO dto) {

        Long id = dto.getId();
        OrderInvoiceEntity orderInvoiceEntity = baseDao.selectById(id);

        //判断是否换开
        if (orderInvoiceEntity.getChangeFlag().equals(InvoiceStatusEnum.IS_CHANGEFLAG_YES.value())) {
            dto.setInvoiceEvolve(InvoiceStatusEnum.INVOCIE_STATUS_HAS_CHANGE.value());
        } else if (orderInvoiceEntity.getChangeFlag().equals(InvoiceStatusEnum.IS_CHANGEFLAG_NO.value())) {
            dto.setInvoiceEvolve(InvoiceStatusEnum.INVOCIE_STATUS_YES.value());
        }
        dto.setInvoiceDate(new Date());
        OrderInvoiceEntity entity = ConvertUtils.sourceToTarget(dto, OrderInvoiceEntity.class);
        updateById(entity);

        //更改订单是否开票 （0：未开票，1，开票）
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setInvoiceFlag(1);
        orderDTO.setId(orderInvoiceEntity.getOrderId());
        orderService.updateByInvoiceFlag(orderDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody OrderInvoiceDTO dto) {
        OrderInvoiceEntity entity = ConvertUtils.sourceToTarget(dto, OrderInvoiceEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, OrderInvoiceEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }


    @Override
    public void saveBatch(@RequestBody List<OrderInvoiceSaveDTO> orderInvoiceSaveDTOS) {
        List<OrderInvoiceEntity> orderInvoiceEntities = ConvertUtils.sourceToTarget(orderInvoiceSaveDTOS, OrderInvoiceEntity.class);
        super.insertBatch(orderInvoiceEntities);
    }


    @Override
    public List<OrderInvoiceDTO> selectOrderInvoiceList(Long orderConfirmId) {
        List<OrderInvoiceEntity> orderInvoiceEntities = baseDao.selectList(Wrappers.<OrderInvoiceEntity>lambdaQuery().eq(OrderInvoiceEntity::getOrderId, orderConfirmId));
        return ConvertUtils.sourceToTarget(orderInvoiceEntities, OrderInvoiceDTO.class);
    }


    @Override
    public void updateBatch(@RequestBody List<OrderInvoiceDTO> orderInvoiceDTOS) {
        List<OrderInvoiceEntity> orderInvoiceEntities = ConvertUtils.sourceToTarget(orderInvoiceDTOS, OrderInvoiceEntity.class);
        this.updateBatchById(orderInvoiceEntities);
    }


    @Override
    public H5InvoiceDetailDTO h5InvoiceDetail(Long orderId) {
        OrderInvoiceEntity orderInvoiceEntity;
        orderInvoiceEntity = baseDao.selectOne(Wrappers.<OrderInvoiceEntity>lambdaQuery()
                .eq(OrderInvoiceEntity::getOrderId, orderId)
                .eq(OrderInvoiceEntity::getDelFlag, 0)
                .eq(OrderInvoiceEntity::getChangeFlag, 1));
        if (BeanUtil.isEmpty(orderInvoiceEntity)) {
            orderInvoiceEntity = baseDao.selectOne(Wrappers.<OrderInvoiceEntity>lambdaQuery()
                    .eq(OrderInvoiceEntity::getOrderId, orderId)
                    .eq(OrderInvoiceEntity::getDelFlag, 0)
                    .eq(OrderInvoiceEntity::getChangeFlag, 0));
        }
        return ConvertUtils.sourceToTarget(orderInvoiceEntity, H5InvoiceDetailDTO.class);
    }


    @Override
    public OrderInvoiceDTO getOrderInvoiceDTO(Long orderId) {
        List<OrderInvoiceEntity> orderInvoiceEntities = baseDao.selectList(Wrappers.<OrderInvoiceEntity>lambdaQuery()
                .eq(OrderInvoiceEntity::getOrderId, orderId)
                .eq(OrderInvoiceEntity::getDelFlag, 0)
                .orderByDesc(OrderInvoiceEntity::getCreateDate));

        if (CollectionUtils.isNotEmpty(orderInvoiceEntities)) {
            return ConvertUtils.sourceToTarget(orderInvoiceEntities.get(0), OrderInvoiceDTO.class);
        } else {
            return new OrderInvoiceDTO();
        }
    }


    @Override
    public void applyInvoice(@RequestBody ApplyOrChangeInvoiceDTO dto) {
        //获取订单信息
        OrderDTO orderDTO = orderService.get(dto.getOrderId());
        Long storeId = orderDTO.getStoreId();
        Long orderId = orderDTO.getId();
        Long buyerId = orderDTO.getBuyerId();
        Date completeTime = orderDTO.getCompleteTime();
        List<OrderInvoiceEntity> orderInvoiceEntities = baseDao.selectList(Wrappers.<OrderInvoiceEntity>lambdaQuery()
                .eq(OrderInvoiceEntity::getOrderId, dto.getOrderId())
                .eq(OrderInvoiceEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        if (CollectionUtils.isNotEmpty(orderInvoiceEntities)) {
            throw new ServiceException(InvoiceStatusCode.ORDER_INVOICE_ALREADY_APPLY);
        }
        //判断订单是否完成或者是否完成超过一个月
        if (BeanUtil.isEmpty(completeTime)) {
            throw new ServiceException(InvoiceStatusCode.ORDER_NOT_FINISH);
        } else {
            // 获取系统设置开票时间
            String queryRedisByName = settingService.queryRedisByName(SettingsEnum.INVOICE.value());
            SettingInvoiceDTO settingInvoiceDTO = JSON.parseObject(queryRedisByName, SettingInvoiceDTO.class);
            long addMonthTime = DateUtils.addDateDays(completeTime, settingInvoiceDTO.getOpenInvoice()).getTime();
            long currTime = System.currentTimeMillis();
            if (currTime > addMonthTime) {
                throw new ServiceException(InvoiceStatusCode.ORDER_FINISH_FOR_MONTH);
            }
        }
        //判断该订单是否有售后
        if (orderDTO.getAfterFlag() == 1) {
            throw new ServiceException(InvoiceStatusCode.ORDER_HAS_AFTER);
        }
        //修改发票表信息为申请开票
        OrderInvoiceEntity orderInvoiceEntity = ConvertUtils.sourceToTarget(dto, OrderInvoiceEntity.class);
        this.fillOrderInvoiceData(dto, orderInvoiceEntity);
        orderInvoiceEntity.setPayAmount(orderDTO.getOrderAmount());
        orderInvoiceEntity.setApplyDate(new Date());
        orderInvoiceEntity.setCreateOrderDate(orderDTO.getCreateDate());
        orderInvoiceEntity.setInvoiceEvolve(InvoiceStatusEnum.INVOCIE_STATUS_NO.value());
        orderInvoiceEntity.setStoreId(orderDTO.getStoreId());
        orderInvoiceEntity.setOrderSn(orderDTO.getOrderSn());
        orderInvoiceEntity.setId(IdGenerator.defaultSnowflakeId());
        try {
            baseDao.insert(orderInvoiceEntity);
        } catch (Exception e) {
            log.error("保存订单发票信息失败，错误信息为", e);
            throw new ServiceException(InvoiceStatusCode.INVOICE_PARAMETER_FAIL);
        }
        List<OrderGoodsDTO> orderGoodsDTOS = orderGoodsService.getByOrderId(orderId, buyerId, storeId);
        List<InvoiceInfoDTO> infoDTOS = new ArrayList<>();
        orderGoodsDTOS.stream().forEach(orderGoodsDTO -> {
            InvoiceInfoDTO invoiceInfoDTO = new InvoiceInfoDTO();
            BeanCopier.create(OrderGoodsDTO.class, InvoiceInfoDTO.class, false)
                    .copy(orderGoodsDTO, invoiceInfoDTO, null);
            invoiceInfoDTO.setSpu(orderGoodsDTO.getSpuSerial());
            invoiceInfoDTO.setInvoiceId(orderInvoiceEntity.getId());
            invoiceInfoDTO.setSpecPayPrice(orderGoodsDTO.getSpecPrice());
            invoiceInfoDTO.setSpecTotalPrice(orderGoodsDTO.getSpecPayPrice()
                    .multiply(new BigDecimal(orderGoodsDTO.getGoodsNum()))
                    .add(orderGoodsDTO.getAvgPreferentialAmount()));
            infoDTOS.add(invoiceInfoDTO);
        });
        invoiceInfoService.saveBatch(infoDTOS);
        //修改订单表发票标记状态
        OrderDTO orderInvoiceUpdate = new OrderDTO();
        orderInvoiceUpdate.setInvoiceFlag(orderInvoiceEntity.getCompanyType());
        orderInvoiceUpdate.setId(orderInvoiceEntity.getOrderId());
        orderService.updateByInvoiceFlag(orderInvoiceUpdate);
        if (dto.getCompanyType().equals(InvoiceStatusEnum.COMPANY_TYPE_COMPANY.value())) {
            MemberInvoiceDTO memberInvoiceDTO = dto.getMemberInvoiceDTO();
            memberInvoiceDTO.setMemberId(orderDTO.getBuyerId());
            memberInvoiceService.changeInvoiceInfo(memberInvoiceDTO);
        }
//        baseDao.update(orderInvoiceEntity, new QueryWrapper<OrderInvoiceEntity>()
//                .eq("order_id", dto.getOrderId())
//                .eq("company_type", 0));
    }

    private void fillOrderInvoiceData(@RequestBody ApplyOrChangeInvoiceDTO dto, OrderInvoiceEntity orderInvoiceEntity) {
        if (dto.getCompanyType().equals(InvoiceStatusEnum.COMPANY_TYPE_COMPANY.value())) {
            MemberInvoiceDTO memberInvoiceDTO = dto.getMemberInvoiceDTO();
            BeanCopier.create(MemberInvoiceDTO.class, OrderInvoiceEntity.class, false).copy(memberInvoiceDTO, orderInvoiceEntity, null);
        }
    }


    @Override
    public void changeInvoice(@RequestBody ApplyOrChangeInvoiceDTO dto) {
        //获取订单信息
        OrderDTO orderDTO = orderService.get(dto.getOrderId());
        List<OrderInvoiceEntity> orderInvoiceEntities = baseDao.selectList(Wrappers.<OrderInvoiceEntity>lambdaQuery()
                .eq(OrderInvoiceEntity::getOrderId, dto.getOrderId())
                .eq(OrderInvoiceEntity::getDelFlag, DelFlagEnum.NORMAL.value()));

        if (CollectionUtils.isEmpty(orderInvoiceEntities)) {
            //未申请过换开，无法申请换开
            throw new ServiceException(InvoiceStatusCode.ORDER_INVOICE_CHANGE_FAIL);
        }
        if (orderInvoiceEntities.size() > 1) {
            //已申请过换开，无法再次申请
            throw new ServiceException(InvoiceStatusCode.ORDER_CHANGE_REPEAT);
        }
        OrderInvoiceEntity oldInvoiceEntity = orderInvoiceEntities.get(0);
        Date completeTime = orderDTO.getCompleteTime();
        //判断订单是否完成或者是否完成超过一年
        if (BeanUtil.isEmpty(completeTime)) {
            throw new ServiceException(InvoiceStatusCode.ORDER_NOT_FINISH);
        } else {
            // 获取系统设置开票时间
            String queryRedisByName = settingService.queryRedisByName(SettingsEnum.INVOICE.value());
            SettingInvoiceDTO settingInvoiceDTO = JSON.parseObject(queryRedisByName, SettingInvoiceDTO.class);
            long addMonthTime = DateUtils.addDateMonths(completeTime, settingInvoiceDTO.getExchangeInvoice()).getTime();
            long currTime = System.currentTimeMillis();
            if (currTime > addMonthTime) {
                throw new ServiceException(InvoiceStatusCode.ORDER_FINISH_FOR_MONTH);
            }
        }
        //判断该订单是否有售后
        if (orderDTO.getAfterFlag() == 1) {
            throw new ServiceException(InvoiceStatusCode.ORDER_HAS_AFTER);
        }
        //修改原有发票信息为已换开
        List<InvoiceInfoDTO> detailInvoiceID = invoiceInfoService.getDetailInvoiceID(oldInvoiceEntity.getId());
        //重新插入一条发票信息
        OrderInvoiceEntity orderInvoiceEntity = new OrderInvoiceEntity();
        BeanCopier.create(ApplyOrChangeInvoiceDTO.class, OrderInvoiceEntity.class, false).copy(dto, orderInvoiceEntity, null);
        this.fillOrderInvoiceData(dto, orderInvoiceEntity);
        orderInvoiceEntity.setId(null);
        orderInvoiceEntity.setChangeFlag(1);
        orderInvoiceEntity.setStoreId(orderDTO.getStoreId());
        orderInvoiceEntity.setInvoiceEvolve(3);
        orderInvoiceEntity.setOrderSn(orderDTO.getOrderSn());
        orderInvoiceEntity.setMemberName(SecurityUser.getUserName());
        orderInvoiceEntity.setApplyDate(new Date());
        orderInvoiceEntity.setCreateOrderDate(orderDTO.getCreateDate());
        orderInvoiceEntity.setPayAmount(orderDTO.getOrderAmount());
        try {
            baseDao.insert(orderInvoiceEntity);
        } catch (Exception e) {
            log.error("保存订单发票信息失败，错误信息为", e);
            throw new ServiceException(InvoiceStatusCode.INVOICE_PARAMETER_FAIL);
        }
        detailInvoiceID.stream().forEach(detailInvoice -> {
            detailInvoice.setId(null);
            detailInvoice.setInvoiceId(orderInvoiceEntity.getId());
        });

        invoiceInfoService.saveBatch(detailInvoiceID);
        if (dto.getCompanyType().equals(InvoiceStatusEnum.COMPANY_TYPE_COMPANY.value())) {
            MemberInvoiceDTO memberInvoiceDTO = dto.getMemberInvoiceDTO();
            memberInvoiceDTO.setMemberId(orderDTO.getBuyerId());
            memberInvoiceService.changeInvoiceInfo(memberInvoiceDTO);
        }
    }


    @Override
    public CanInvoiceDTO checkApplyInvoice(Long orderId) {
        List<OrderInvoiceEntity> orderInvoiceEntities = Optional.ofNullable(baseDao.selectList(Wrappers.<OrderInvoiceEntity>lambdaQuery()
                .eq(OrderInvoiceEntity::getOrderId, orderId)
                .eq(OrderInvoiceEntity::getDelFlag, DelFlagEnum.NORMAL))).orElse(new ArrayList<OrderInvoiceEntity>());
        OrderDTO orderDTO = orderService.get(orderId);
        Boolean check = check(orderDTO.getInvoiceFlag(), orderDTO.getCompleteTime());
        if (!check) {
            throw new ServiceException(InvoiceStatusCode.ORDER_FINISH_FOR_MONTH);
        }
        for (OrderInvoiceEntity orderInvoiceEntity : orderInvoiceEntities) {
            if (orderInvoiceEntity.getInvoiceEvolve().equals(1)) {
                throw new ServiceException(InvoiceStatusCode.ORDER_INVOICE_ALREADY_APPLY);
            }
            if (orderInvoiceEntity.getInvoiceEvolve().equals(3)) {
                throw new ServiceException(InvoiceStatusCode.ORDER_CHANGE_REPEAT);
            }
        }
        CanInvoiceDTO canInvoiceDTO = new CanInvoiceDTO();
        canInvoiceDTO.setFlag(false);
        List<OrderGoodsDTO> orderGoodsDTOS = orderGoodsService.getByOrderId(orderId, null, null);
        if (CollectionUtils.isEmpty(orderGoodsDTOS)) {
            throw new ServiceException(InvoiceStatusCode.ORDER_GOODS_NOT_INVOICE);
        }
        List<Long> goodsIds = orderGoodsDTOS.stream().map(OrderGoodsDTO::getGoodsId).collect(Collectors.toList());
        List<GoodsDTO> goodsDTOS = goodsService.getByGoodsIds(goodsIds, null);
        List<String> invoiceTypes = new ArrayList<>();
        List<String> invoiceContents = new ArrayList<>();
        for (GoodsDTO goodsDTO : goodsDTOS) {
            if (goodsDTO.getInvoiceFlag() == 0) {
                throw new ServiceException(InvoiceStatusCode.ORDER_GOODS_NOT_INVOICE);
            }
            invoiceTypes.add(goodsDTO.getInvoiceType());
            invoiceContents.add(goodsDTO.getInvoiceContent());
        }
        if (invoiceTypes.size() < goodsDTOS.size() || invoiceContents.size() < goodsDTOS.size()) {
            throw new ServiceException(InvoiceStatusCode.ORDER_GOODS_NOT_INVOICE);
        }
        List<String> types = getInvoiceTypes(invoiceTypes);
        List<String> contents = getInvoiceContents(invoiceContents);
        if (CollectionUtils.isEmpty(types) || CollectionUtils.isEmpty(contents)) {
            throw new ServiceException(InvoiceStatusCode.ORDER_GOODS_NOT_INVOICE);
        }
        canInvoiceDTO.setFlag(true);
        canInvoiceDTO.setInvoiceContents(contents);
        canInvoiceDTO.setInvoiceTypes(types);
        log.info("可开发票返回结果，{}", JSON.toJSONString(canInvoiceDTO));
        return canInvoiceDTO;
    }

    private List<String> getInvoiceContents(List<String> invoiceContents) {
        Set<String> invoiceContentsCan = new HashSet<String>();
        Set<String> invoiceContentsNot = new HashSet<String>();
        boolean flag = true;
        for (String invoiceContent : invoiceContents) {
            String[] invoiceContentArr = invoiceContent.split(",");
            for (int i = 0; i < invoiceContentArr.length; i++) {
                String s = invoiceContentArr[i];
                for (String type : invoiceContents) {
                    flag = type.contains(s);
                    if (flag) {
                        invoiceContentsCan.add(s);
                    } else {
                        invoiceContentsNot.add(s);
                        break;
                    }
                    if (!flag) {
                        break;
                    }
                }
                if (!flag) {
                    break;
                }
            }
        }
        log.info("可以开票的发票内容：{}", JSON.toJSONString(invoiceContentsCan));
        log.info("不可以开票的发票内容：{}", JSON.toJSONString(invoiceContentsNot));
        List<String> res = new ArrayList<String>();
        for (String s : invoiceContentsCan) {
            if (CollectionUtils.isEmpty(invoiceContentsNot)) {
                res.addAll(invoiceContentsCan);
                return res;
            }
            boolean mark = invoiceContentsNot.contains(s);
            if (!mark) {
                res.add(s);
            }
        }
        return res;
    }

    private List<String> getInvoiceTypes(List<String> invoiceTypes) {
        Set<String> invoiceTypeCan = new HashSet<String>();
        Set<String> invoiceTypeNot = new HashSet<String>();
        boolean flag = true;
        for (String invoiceType : invoiceTypes) {
            String[] invoiceTypeArr = invoiceType.split(",");
            for (int i = 0; i < invoiceTypeArr.length; i++) {
                String s = invoiceTypeArr[i];
                for (String type : invoiceTypes) {
                    flag = type.contains(s);
                    if (flag) {
                        invoiceTypeCan.add(s);
                    } else {
                        invoiceTypeNot.add(s);
                        break;
                    }
                    if (!flag) {
                        break;
                    }
                }
                if (!flag) {
                    break;
                }
            }
        }
        log.info("可以开票的发票类型：{}", JSON.toJSONString(invoiceTypeCan));
        log.info("不可以开票的发票类型：{}", JSON.toJSONString(invoiceTypeNot));
        List<String> res = new ArrayList<String>();
        for (String s : invoiceTypeCan) {
            if (CollectionUtils.isEmpty(invoiceTypeCan)) {
                res.addAll(invoiceTypeCan);
                return res;
            }
            boolean mark = invoiceTypeNot.contains(s);
            if (!mark) {
                res.add(s);
            }
        }
        return res;
    }


    @Override
    public OrderInvoiceDetailDTO detail(Long id) {
        OrderInvoiceDetailDTO orderInvoiceDetailDTO = new OrderInvoiceDetailDTO();
        OrderInvoiceEntity orderInvoiceEntity = baseDao.selectById(id);
        BeanCopier.create(OrderInvoiceEntity.class, OrderInvoiceDetailDTO.class, false)
                .copy(orderInvoiceEntity, orderInvoiceDetailDTO, null);
        List<InvoiceInfoDTO> detailInvoiceID = invoiceInfoService.getDetailInvoiceID(id);
        orderInvoiceDetailDTO.setInvoiceInfoDTOS(detailInvoiceID);
        return orderInvoiceDetailDTO;
    }


    @Override
    public void sendEmail(@RequestParam Map<String, String> params) {
        String id = params.get("id");
        String email = params.get("email");
        if (StringUtils.isBlank(id) || StringUtils.isBlank(email)) {
            throw new ServiceException(InvoiceStatusCode.EMAIL_SEND_FAIL);
        }
        OrderInvoiceEntity invoiceEntity = baseDao.selectById(id);
        Long orderId = invoiceEntity.getOrderId();
        String fileUrl = invoiceEntity.getFileUrl();
        Map<String, Object> sendParams = new HashMap<>();
        sendParams.put("orderId", orderId);
        sendParams.put("path", imgUrl + fileUrl);
        String sendStr = JSON.toJSONString(sendParams);
        sysMailTemplateService.sendMail(Long.valueOf(EmailEnum.INVOICE_EMAIL_TEMPLATE.value()), email, "", sendStr);
    }

    /**
     * 检查是否可以开票或者换开
     *
     * @param type         0 开票  1 换开
     * @param completeTime 订单完成时间
     */

    @Override
    public Boolean check(@RequestParam("type") Integer type, @RequestParam("completeTime") Date completeTime) {
        // 获取系统设置开票时间
        String queryRedisByName = settingService.queryRedisByName(SettingsEnum.INVOICE.value());
        SettingInvoiceDTO settingInvoiceDTO = JSON.parseObject(queryRedisByName, SettingInvoiceDTO.class);
        Long addMonthTime = null;
        if (type == 0) {
            addMonthTime = DateUtils.addDateDays(completeTime, settingInvoiceDTO.getOpenInvoice()).getTime();
        } else {
            addMonthTime = DateUtils.addDateMonths(completeTime, settingInvoiceDTO.getExchangeInvoice()).getTime();
        }
        long currTime = System.currentTimeMillis();
        if (currTime > addMonthTime) {
            return false;
        }
        return true;
    }
}
