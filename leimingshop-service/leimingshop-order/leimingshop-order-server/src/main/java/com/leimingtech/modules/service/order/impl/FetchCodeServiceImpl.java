/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DataMaskingUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dao.order.FetchCodeDao;
import com.leimingtech.modules.dto.order.AdminOrderDetailDTO;
import com.leimingtech.modules.dto.order.OrderVerifyCodeDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDetailDTO;
import com.leimingtech.modules.dto.virtual.VerifyRecordDTO;
import com.leimingtech.modules.entity.order.FetchCodeEntity;
import com.leimingtech.modules.enums.order.CodeStatusEnum;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.FetchCodeService;
import com.leimingtech.modules.service.order.OrderAddressService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.order.VerifyRecordService;
import com.leimingtech.modules.statuscode.OrderStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 电子提货码
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */
@Slf4j
@Service
@Transactional

public class FetchCodeServiceImpl extends BaseServiceImpl<FetchCodeDao, FetchCodeEntity> implements FetchCodeService {

    @Resource
    OrderService orderService;

    @Resource
    VerifyRecordService verifyRecordService;

    @Resource
    OrderAddressService orderAddressService;

    @Autowired
    MemberService memberService;

    @Autowired
    private RedisUtils redisUtils;

    @Override

    public PageData<FetchCodeDTO> page(@RequestParam Map<String, Object> params) {
        IPage<FetchCodeEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, FetchCodeDTO.class);
    }

    @Override

    public List<FetchCodeDTO> list(@RequestParam Map<String, Object> params) {
        List<FetchCodeEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, FetchCodeDTO.class);
    }

    private QueryWrapper<FetchCodeEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String buyerName = (String) params.get("buyerName");
        String goodsSerial = (String) params.get("goodsSerial");
        String orderSn = (String) params.get("orderSn");
        String orderId = (String) params.get("orderId");
        String goodsName = (String) params.get("goodsName");
        String storeName = (String) params.get("storeName");
        String codeStatus = (String) params.get("codeStatus");
        String storeId = (String) params.get("storeId");
        String memberId = (String) params.get("memberId");

        QueryWrapper<FetchCodeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(buyerName), "member_name", buyerName);
        wrapper.eq(StringUtils.isNotBlank(goodsSerial), "goods_serial", goodsSerial);
        wrapper.eq(StringUtils.isNotBlank(orderSn), "order_sn", orderSn);
        wrapper.eq(StringUtils.isNotBlank(orderId), "order_id", orderId);
        wrapper.like(StringUtils.isNotBlank(goodsName), "goods_name", goodsName);
        wrapper.like(StringUtils.isNotBlank(storeName), "store_name", storeName);
        wrapper.eq(StringUtils.isNotBlank(codeStatus), "code_status", codeStatus);
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        wrapper.eq(StringUtils.isNotBlank(memberId), "member_id", memberId);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public FetchCodeDTO get(Long id) {
        FetchCodeEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, FetchCodeDTO.class);
    }


    /**
     * seller查询电子提货码详情
     *
     * @param id        提货码id
     * @param fetchCode 电子提货码
     * @param storeId   店铺id
     * @return
     * @date 2020-04-29 15:28
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public FetchCodeDetailDTO sellerDetail(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "fetchCode", required = false) String fetchCode, @RequestParam(value = "storeId", required = false) Long storeId) {
        FetchCodeDetailDTO fetchCodeDetailDTO = new FetchCodeDetailDTO();

        QueryWrapper<FetchCodeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);
        wrapper.eq(storeId != null, "store_id", storeId);
        wrapper.eq(fetchCode != null, "fetch_code", fetchCode);
        FetchCodeEntity fetchCodeEntity = baseDao.selectOne(wrapper);
        if (null == fetchCodeEntity) {
            return null;
        }

        if (null != fetchCodeEntity) {
            fetchCodeDetailDTO = ConvertUtils.sourceToTarget(fetchCodeEntity, FetchCodeDetailDTO.class);
            AdminOrderDetailDTO adminOrderDetailDTO = orderService.getAdminOrderDetail(fetchCodeDetailDTO.getOrderId(), null, storeId);
            fetchCodeDetailDTO.setAdminOrderDetailDTO(adminOrderDetailDTO);
        }

        // 处理电子提货码加密
        fetchCodeDetailDTO.setFetchCode(DataMaskingUtils.idEncrypt(fetchCodeDetailDTO.getFetchCode()));

        return fetchCodeDetailDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody FetchCodeDTO dto) {
        FetchCodeEntity entity = ConvertUtils.sourceToTarget(dto, FetchCodeEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody FetchCodeDTO dto) {
        FetchCodeEntity entity = ConvertUtils.sourceToTarget(dto, FetchCodeEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, FetchCodeEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 确认核销
     *
     * @param orderVerifyCodeDTO
     * @return
     * @date 2020-04-30 09:49
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    @Transactional
    public void verifyCode(@RequestBody OrderVerifyCodeDTO orderVerifyCodeDTO) {

        // 1.校验当前电子提货码的id是否存在
        FetchCodeDTO fetchCodeDTO = this.get(orderVerifyCodeDTO.getId());
        if (null == fetchCodeDTO) {
            throw new ServiceException(OrderStatusCode.VERIFY_ORDER_NOT_EXIST);
        }

        // 2.校验提货码状态是否过期
        if (CodeStatusEnum.EXPIRED.value() == fetchCodeDTO.getCodeStatus()) {
            throw new ServiceException(OrderStatusCode.VERIFY_ORDER_EXPRISE);
        }

        // 3.核销数量不能大于当前 商品数量- 已核销数量
        if (orderVerifyCodeDTO.getVerifyNum() <= 0 && orderVerifyCodeDTO.getVerifyNum() > fetchCodeDTO.getGoodsNum() - fetchCodeDTO.getExchangeNum()) {
            throw new ServiceException(OrderStatusCode.VERIFY_ORDER_NUMBER_ERROR);
        }
        // 3.定时任务，扫码过期的更新状态为已过期

        // 4.修改当前核销码的已核销数量和核销状态
        Integer exchangeNum = fetchCodeDTO.getExchangeNum() + orderVerifyCodeDTO.getVerifyNum();
        fetchCodeDTO.setExchangeNum(exchangeNum);
        if (fetchCodeDTO.getGoodsNum() - exchangeNum == 0) {
            fetchCodeDTO.setCodeStatus(CodeStatusEnum.ALL_CHECK.value());
        } else if (fetchCodeDTO.getGoodsNum() - exchangeNum > 0) {
            fetchCodeDTO.setCodeStatus(CodeStatusEnum.PART_CHECK.value());
        }

        this.update(fetchCodeDTO);
        // 5.生成一条对应的核销记录
        VerifyRecordDTO verifyRecordDTO = ConvertUtils.sourceToTarget(fetchCodeDTO, VerifyRecordDTO.class);
        verifyRecordDTO.setId(IdGenerator.defaultSnowflakeId());
        verifyRecordDTO.setExchangeNum(orderVerifyCodeDTO.getVerifyNum());
        // 获取用户虚拟订单下单时的手机号
        AdminOrderDetailDTO adminOrderDetailDTO = orderService.getAdminOrderDetail(fetchCodeDTO.getOrderId(), null, fetchCodeDTO.getStoreId());
        verifyRecordDTO.setMemberMobile(adminOrderDetailDTO.getOrderAddressDTO().getMobPhone());
        verifyRecordDTO.setCreator(SecurityUser.getUserName());
        verifyRecordService.save(verifyRecordDTO);

    }

    /**
     * 电子提货码过期定时
     *
     * @return
     * @date 2020-04-30 15:22
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void codeExpireTiming() {
        String key = "code_expire_timing";
        if (redisUtils.tryLock(key)) {
            try {
                Date now = new Date();
                // 未核销,部分核销；不等于-1（-1表示永久有效）；过期时间小于当前时间
                UpdateWrapper<FetchCodeEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                        .le("expiry_date", now)
                        .in("code_status", CodeStatusEnum.WATTING_CHECK.value(), CodeStatusEnum.PART_CHECK.value())
                        .ne("valid_day", -1);
                FetchCodeEntity entity = new FetchCodeEntity();
                // 修改为过期状态
                entity.setCodeStatus(CodeStatusEnum.EXPIRED.value());

                baseDao.update(entity, updateWrapper);

            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("定时电子码过期异常回滚尝试：{0}", e);
            }
            redisUtils.releaseLock(key);
        }

    }

}
