/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.order.VerifyRecordDao;
import com.leimingtech.modules.dto.order.AdminOrderDetailDTO;
import com.leimingtech.modules.dto.virtual.VerifyRecordDTO;
import com.leimingtech.modules.dto.virtual.VerifyRecordDetailDTO;
import com.leimingtech.modules.entity.order.VerifyRecordEntity;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.order.VerifyRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 核销记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */
@Service
@Transactional

public class VerifyRecordServiceImpl extends BaseServiceImpl<VerifyRecordDao, VerifyRecordEntity> implements VerifyRecordService {

    @Resource
    OrderService orderService;

    @Override

    public PageData<VerifyRecordDTO> page(@RequestParam Map<String, Object> params) {
        IPage<VerifyRecordEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, VerifyRecordDTO.class);
    }

    @Override

    public List<VerifyRecordDTO> list(Map<String, Object> params) {
        List<VerifyRecordEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, VerifyRecordDTO.class);
    }

    private QueryWrapper<VerifyRecordEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String buyerName = (String) params.get("buyerName");
        String goodsSerial = (String) params.get("goodsSerial");
        String orderSn = (String) params.get("orderSn");
        String goodsName = (String) params.get("goodsName");
        String storeName = (String) params.get("storeName");
        String storeId = (String) params.get("storeId");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");

        QueryWrapper<VerifyRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(buyerName), "member_name", buyerName);
        wrapper.eq(StringUtils.isNotBlank(goodsSerial), "goods_serial", goodsSerial);
        wrapper.eq(StringUtils.isNotBlank(orderSn), "order_sn", orderSn);
        wrapper.like(StringUtils.isNotBlank(goodsName), "goods_name", goodsName);
        wrapper.like(StringUtils.isNotBlank(storeName), "store_name", storeName);
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        wrapper.gt(StringUtils.isNotBlank(startTime), "create_date", startTime);
        wrapper.lt(StringUtils.isNotBlank(endTime), "create_date", endTime);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public VerifyRecordDTO get(Long id) {
        VerifyRecordEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, VerifyRecordDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody VerifyRecordDTO dto) {
        VerifyRecordEntity entity = ConvertUtils.sourceToTarget(dto, VerifyRecordEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody VerifyRecordDTO dto) {
        VerifyRecordEntity entity = ConvertUtils.sourceToTarget(dto, VerifyRecordEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, VerifyRecordEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 查询核销记录详情
     *
     * @param id      核销记录id
     * @param storeId 店铺id
     * @return
     * @date 2020-04-29 15:48
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public VerifyRecordDetailDTO sellerDetail(Long id, @RequestParam(value = "storeId", required = false) Long storeId) {
        VerifyRecordDetailDTO verifyRecordDetailDTO = new VerifyRecordDetailDTO();

        QueryWrapper<VerifyRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);
        wrapper.eq(storeId != null, "store_id", storeId);
        VerifyRecordEntity entity = baseDao.selectOne(wrapper);
        if (null != entity) {
            verifyRecordDetailDTO = ConvertUtils.sourceToTarget(entity, VerifyRecordDetailDTO.class);
            AdminOrderDetailDTO adminOrderDetailDTO = orderService.getAdminOrderDetail(verifyRecordDetailDTO.getOrderId(), null, storeId);
            verifyRecordDetailDTO.setAdminOrderDetailDTO(adminOrderDetailDTO);
        }

        return verifyRecordDetailDTO;
    }

}
