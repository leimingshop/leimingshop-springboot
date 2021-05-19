/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.coupons.*;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.moudle.search.modules.service.CouponsSearchService;
import com.leimingtech.security.SecurityCurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 优惠券搜索
 *
 * @author chengqian
 */
@Slf4j
@Service
public class CouponsSearchServiceImpl implements CouponsSearchService {

    private static final MonitorLogger LOGGER = MonitorLoggerFactory.getMonitorLogger(CouponsSearchServiceImpl.class);

    @Autowired
    private EsDataUtils esDataUtils;


    @Override
    public GoodsDetailCouponsListDTO goodsDetailCouponsList(Long goodsId, Long brandId, Long storeId, Long storeGoodsClassId) {

        // 获取登录信息
        Long memberId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        }

        GoodsDetailCouponsListDTO goodsDetailCouponsListDTO = new GoodsDetailCouponsListDTO();

        // 获得商品所有处于活动中的优惠券（目前包含可领取数量为0的，下面要处理掉）
        List<String> canResultList = getGoodsAllCoupons(goodsId, brandId, storeId, storeGoodsClassId);
        if (CollectionUtils.isEmpty(canResultList)) {
            //如果可用优惠券为空则直接返回
            return goodsDetailCouponsListDTO;
        }
        // 判断如果没有登录则返回
        if (null == memberId) {
            List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = convertAndSortCoupons(canResultList);
            goodsDetailCouponsListDTO.getCanRecList().addAll(frontCouponsActivityPageDTOList);
        } else {
            // 已登录 区分已领取优惠券和未领取优惠券
            GoodsDetailCouponsDTO goodsDetailCouponsDTO = this.separateCoupons(memberId, canResultList);

            if (null != goodsDetailCouponsDTO.getFrontCouponsActivityPageDTO()) {
                List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = new ArrayList<>();
                frontCouponsActivityPageDTOList.add(goodsDetailCouponsDTO.getFrontCouponsActivityPageDTO());
                goodsDetailCouponsListDTO.getCanRecList().addAll(frontCouponsActivityPageDTOList);
            }

            if (null != goodsDetailCouponsDTO.getFrontMyCouponsPageDTO()) {
                List<FrontMyCouponsPageDTO> recedList = new ArrayList<>();
                recedList.add(goodsDetailCouponsDTO.getFrontMyCouponsPageDTO());
                goodsDetailCouponsListDTO.getRecedList().addAll(recedList);
            }


        }


        return goodsDetailCouponsListDTO;
    }

    private GoodsDetailCouponsDTO separateCoupons(Long memberId, List<String> canResultList) {
        // 最终要返回的实体
        GoodsDetailCouponsDTO couponsDTO = new GoodsDetailCouponsDTO();
        List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = new ArrayList<>();
        List<FrontMyCouponsPageDTO> frontMyCouponsPageDTOList = new ArrayList<>();
        canResultList.stream().forEach(basicString -> {
            FrontCouponsActivityPageDTO couponsActivityIndexDTO = JSONObject.parseObject(basicString, FrontCouponsActivityPageDTO.class);
            // 获取用户优惠券信息
            PageModelDTO pageModelDTO = new PageModelDTO();
            pageModelDTO.setIsPage(false);
            // 查询用户已领取未使用的当前优惠券
            Map<String, Object> condition = pageModelDTO.getEqualsSearchCondition();
            condition.put("memberId", memberId);
            condition.put("activityId", couponsActivityIndexDTO.getId());
            Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
            inSearchCondition.put("couponsState", Arrays.asList(CouponsEnum.COUPON_CANNT_USE.value(), CouponsEnum.COUPON_CAN_USE.value(), CouponsEnum.COUPONS_USED.value()));

            // 用户优惠券查询
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);
            List<String> memberCouponsResultList = pageModelDTO.getJsonRsList();
            frontCouponsActivityPageDTOList.add(couponsActivityIndexDTO);

            // 循环查询
            memberCouponsResultList.stream().forEach(memberCouponIndex -> {
                MemberCouponsIndexDTO memberCouponsIndexDTO = JSONObject.parseObject(memberCouponIndex, MemberCouponsIndexDTO.class);
                if (memberCouponsIndexDTO.getCouponsState().equals(CouponsEnum.COUPON_CANNT_USE.value())
                        || memberCouponsIndexDTO.getCouponsState().equals(CouponsEnum.COUPON_CAN_USE.value())) {
                    frontCouponsActivityPageDTOList.remove(couponsActivityIndexDTO);
                    // 如果未使用则放入已领取集合中 如果已使用则不展示
                    FrontMyCouponsPageDTO frontMyCouponsPageDTO = JSONObject.parseObject(memberCouponIndex, FrontMyCouponsPageDTO.class);
                    frontMyCouponsPageDTOList.add(frontMyCouponsPageDTO);
                } else {
                    //使用了但是达到了限领数量的要求就要给移除
                    if ((couponsActivityIndexDTO.getPersonLimit() > 0 && memberCouponsResultList.size() >= couponsActivityIndexDTO.getPersonLimit())) {
                        frontCouponsActivityPageDTOList.remove(couponsActivityIndexDTO);
                    }
                }
            });

        });
        FrontCouponsActivityPageDTO frontCouponsActivityPageDTO = frontCouponsActivityPageDTOList.stream()
                .filter(activityDTO -> activityDTO.getTotalNum() == 0 || activityDTO.getSurplusNum() >= 1)
                .max(Comparator.comparing(FrontCouponsActivityPageDTO::getFaceValue))
                .orElse(null);
        couponsDTO.setFrontCouponsActivityPageDTO(frontCouponsActivityPageDTO);

        FrontMyCouponsPageDTO frontMyCouponsPageDTO = frontMyCouponsPageDTOList.stream()
                .max(Comparator.comparing(FrontMyCouponsPageDTO::getFaceValue))
                .orElse(null);
        couponsDTO.setFrontMyCouponsPageDTO(frontMyCouponsPageDTO);

        return couponsDTO;

    }


    /**
     * 功能描述：（仅供商品详情页面获取优惠券数据使用）
     * 过滤优惠券列表将可领取数量为0的数据过滤掉
     * 同时获取优惠券金额最大那条数据返回
     *
     * @author 宋文豪
     * @email: songwenhao@leimingtech.com
     * @Date : 2020/3/12
     **/
    private List<FrontCouponsActivityPageDTO> convertAndSortCoupons(List<String> canResultList) {
        FrontCouponsActivityPageDTO frontCouponsActivityPageDTO = canResultList.stream()
                .map(result -> JSONObject.parseObject(result, FrontCouponsActivityPageDTO.class))
                .filter(activityDTO -> activityDTO.getTotalNum() == 0 || activityDTO.getSurplusNum() >= 1)
                .max(Comparator.comparing(FrontCouponsActivityPageDTO::getFaceValue))
                .orElse(null);
        List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = new ArrayList<>();
        frontCouponsActivityPageDTOList.add(frontCouponsActivityPageDTO);
        return frontCouponsActivityPageDTOList;
    }

    private List<String> getGoodsAllCoupons(Long goodsId, Long brandId, Long storeId, Long storeGoodsClassId) {
        List<String> resultList = new ArrayList<>();
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        sortFileds.put("faceValue", "desc");
        equalsSearchCondition.put("storeId", storeId);
        equalsSearchCondition.put("activityState", CouponsEnum.ACTIVITY_STATE_START.value());
        Map<String, Object> map = new HashMap<>(10);
        map.put("activityGoodsScope", Arrays.asList(CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value(), CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value(), CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value(), CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value()));
        List<Long> relationIdList = new ArrayList<>();
        relationIdList.add(goodsId);
        if (null != brandId) {
            relationIdList.add(brandId);
        }
        if (null != storeId) {
            relationIdList.add(storeId);
        }

        if (null != storeGoodsClassId) {
            relationIdList.add(storeGoodsClassId);
        }
        map.put("relationId", relationIdList);
        Map<String, Map<String, Object>> subInSearchCondition = pageModelDTO.getSubInFilterSearchCondition();
        subInSearchCondition.put("goodsList", map);
        // 查询当前店铺所有处于活动期间的所有优惠券信息
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX);
        resultList.addAll(pageModelDTO.getJsonRsList());
        return resultList;
    }
}
