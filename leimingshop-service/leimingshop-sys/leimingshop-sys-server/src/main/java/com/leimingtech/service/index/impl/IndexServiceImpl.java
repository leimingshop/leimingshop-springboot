/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.index.impl;

import cn.hutool.core.date.DateUtil;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.constants.IndexRedisConstants;
import com.leimingtech.dto.index.IndexBaseDataDTO;
import com.leimingtech.modules.dto.goods.index.IndexGoodsDataDTO;
import com.leimingtech.modules.dto.member.IndexMemberDataDTO;
import com.leimingtech.modules.dto.order.IndexOrderDataDTO;
import com.leimingtech.modules.dto.store.IndexStoreDataDTO;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.service.index.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author xuzhch
 * @className IndexServiceImpk
 * @description 首页业务类
 * @date 2020/3/16/016
 */
@Slf4j
@Service

public class IndexServiceImpl implements IndexService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 管理端基础概况
     *
     * @param params map
     * @return IndexBaseDataDTO
     * @date 2020/3/16/016 16:25
     * @author xuzhch
     **/
    @Override

    public IndexBaseDataDTO baseManage(@RequestParam Map<String, Object> params) {

        Integer refresh = Integer.valueOf((String) params.get("refresh"));
        Object dateObj = params.get("date");
        Integer dateType = Integer.valueOf((String) params.get("dateType"));
        Long userId = Long.valueOf(params.get("userId").toString());
        Date endDate = null;
        if (dateObj == null || refresh == 1) {
            endDate = new Date();
        } else {
            endDate = DateUtils.parse(dateObj.toString(), DateUtils.DATE_TIME_PATTERN);
        }

        if (refresh == 1) {
            redisUtils.deleteByPattern(IndexRedisConstants.INDEX_BASE + userId + ":*");
        } else {
            Object o = redisUtils.get(IndexRedisConstants.INDEX_BASE + userId + ":" + dateType);
            if (o != null) {
                return (IndexBaseDataDTO) o;
            }
        }
        Date startDate = null;
        if (dateType != 0) {
            startDate = DateUtils.getFixedDate(endDate, dateType);
        } else {
            startDate = DateUtil.beginOfDay(endDate);
        }
        String startDateStr = DateUtils.format(startDate, DateUtils.DATE_TIME_PATTERN);
        String endDateStr = DateUtils.format(endDate, DateUtils.DATE_TIME_PATTERN);
        IndexOrderDataDTO orderData = orderService.indexOrderData(startDateStr, endDateStr);
        IndexGoodsDataDTO goodsData = goodsService.indexGoodsData(startDateStr, endDateStr);
        IndexMemberDataDTO memberData = memberService.indexMemberData(startDateStr, endDateStr);
        IndexStoreDataDTO storeData = storeService.indexStoreData(startDateStr, endDateStr);

        IndexBaseDataDTO baseDataDTO = new IndexBaseDataDTO();
        baseDataDTO.setUpdateDate(endDate);
        baseDataDTO.setOrderCount(null != orderData.getOrderCount() ? orderData.getOrderCount() : 0);
        baseDataDTO.setSubOrderAmount(null != orderData.getSubOrderAmount() ? orderData.getSubOrderAmount() : BigDecimal.ZERO);
        baseDataDTO.setGoodsCount(null != goodsData.getGoodsCount() ? goodsData.getGoodsCount() : 0);
        baseDataDTO.setNewGoodsCount(null != goodsData.getNewGoodsCount() ? goodsData.getNewGoodsCount() : 0);
        baseDataDTO.setMemberCount(null != memberData.getMemberCount() ? memberData.getMemberCount() : 0);
        baseDataDTO.setNewMemberCount(null != memberData.getNewMemberCount() ? memberData.getNewMemberCount() : 0);
        baseDataDTO.setStoreCount(null != storeData.getStoreCount() ? storeData.getStoreCount() : 0);
        baseDataDTO.setNewStoreCount(null != storeData.getNewStoreCount() ? storeData.getNewStoreCount() : 0);
        redisUtils.set(IndexRedisConstants.INDEX_BASE + userId + ":" + dateType, baseDataDTO, IndexRedisConstants.JEDIS_EXPIRE);
        return baseDataDTO;
    }
}
