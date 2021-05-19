/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.coupons.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.RangConditionDTO;
import com.leimingtech.modules.dto.RangConditionsToTimeModelDTO;
import com.leimingtech.modules.dto.coupons.*;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.service.coupons.CouponsActivitySearchService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <优惠券惠活动es搜索>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */
@Slf4j
@Service

public class CouponsActivitySearchServiceImpl implements CouponsActivitySearchService {

    private static final int HOURS = 24;
    @Autowired
    private EsDataUtils esDataUtils;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 功能描述：
     * <查询会员优惠券详情>
     *
     * @param id 会员优惠券id
     * @return
     * @date 2020/1/13 12:32
     * @author 刘远杰
     **/

    @Override
    public MemberCouponsIndexDTO getMemberCoupons(Long id) {
        // 1.查询es活动信息
        String json = esDataUtils.getDateById(ElasticSearchConstant.MEMBER_COUPONS_INDEX, id.toString());
        // 2.转化为前台展示数据格式
        return JSONObject.parseObject(json, MemberCouponsIndexDTO.class);
    }

    /**
     * 功能描述:
     * 〈推荐优惠券列表查询〉
     *
     * @author : 刘远杰
     */

    @Override
    public List<FrontCouponsActivityPageDTO> recommendCouponsList(@RequestParam(value = "memberId", required = false) Long memberId) {
        // 1.封装es查询条件
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        // 1-1.领取结束时间正序排序
        sortFileds.put("getEndDate", "asc");
        // 1-2.查询已开始活动
        equalsSearchCondition.put("activityState", CouponsEnum.ACTIVITY_STATE_START.value());
        // 1-3.查询剩余优惠券数量大于0 或者 优惠券总数为0（不限量）
        List<Map<String, Object>> orSearchConditionList = pageModelDTO.getOrSearchConditionList();
        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("totalNum", 0);
        orSearchConditionList.add(mapCondition);
        RangConditionDTO rangConditionDTO = new RangConditionDTO();
        rangConditionDTO.setBeginValue("1");
        Map<String, Object> rangConditionMap = new HashMap<>();
        rangConditionMap.put("surplusNum", rangConditionDTO);
        orSearchConditionList.add(rangConditionMap);

        // 2.查询进行中的优惠券活动正序排序
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX);
        List<String> resultList = pageModelDTO.getJsonRsList();

        // 3.移除已领取优惠券
        List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOS = this.removeReceivedCoupons(memberId, resultList);
        // 保存优惠券pv统计
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String ipAddr = IpUtils.getIpAddr(request);
        Map<String, Object> map = new HashMap<>();
        map.put("list", frontCouponsActivityPageDTOS);
        map.put("ip", ipAddr);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_COUPONS_PV_QUEUE, JSON.toJSONString(map));
        return frontCouponsActivityPageDTOS;
    }

    /**
     * 功能描述:
     * 〈店铺分组优惠券列表查询〉
     *
     * @author : 刘远杰
     */
    @Override
    public List<FrontCouponsActivityStoreGroupDTO> storeGroupCouponsList(@RequestParam Map<String, Object> params) {
        // 1.封装查询条件
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        // 领取结束时间正序排序
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        sortFileds.put("getEndDate", "asc");
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        // 查询已开始活动
        equalsSearchCondition.put("activityState", CouponsEnum.ACTIVITY_STATE_START.value());

        // 1-3.查询剩余优惠券数量大于0 或者 优惠券总数为0（不限量）
        List<Map<String, Object>> orSearchConditionList = pageModelDTO.getOrSearchConditionList();
        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("totalNum", 0);
        orSearchConditionList.add(mapCondition);
        RangConditionDTO rangConditionDTO = new RangConditionDTO();
        rangConditionDTO.setBeginValue("1");
        Map<String, Object> rangConditionMap = new HashMap<>();
        rangConditionMap.put("surplusNum", rangConditionDTO);
        orSearchConditionList.add(rangConditionMap);

        // 店铺名称模糊搜索
        if (params.get("storeName") != null && StringUtils.isNotBlank(params.get("storeName").toString())) {
            Map<String, Object> likeSearchCondition = pageModelDTO.getLikeSearchCondition();
            likeSearchCondition.put("storeName", params.get("storeName").toString());
        }

        // 2.获取所有进行中优惠券活动
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX);
        List<String> resultList = pageModelDTO.getJsonRsList();

        // 3.移除已领取优惠券
        List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList;
        if (params.get("memberId") == null || StringUtils.isBlank(params.get("memberId").toString())) {
            frontCouponsActivityPageDTOList = this.removeReceivedCoupons(null, resultList);
        } else {
            frontCouponsActivityPageDTOList = this.removeReceivedCoupons(Long.parseLong(params.get("memberId").toString()), resultList);
        }

        // 4.按照店铺分组
        List<FrontCouponsActivityStoreGroupDTO> frontCouponsActivityStoreGroupDTOList = getFrontCouponsActivityStoreGroupDTOS(frontCouponsActivityPageDTOList);

        // 5.分组按照拼音排序
        frontCouponsActivityStoreGroupDTOList.sort((FrontCouponsActivityStoreGroupDTO o1, FrontCouponsActivityStoreGroupDTO o2) -> {
            Comparator<Object> com = Collator.getInstance(Locale.CHINA);
            return com.compare(o1.getStoreName(), o2.getStoreName());
        });

        List<FrontCouponsActivityPageDTO> list = new ArrayList<>();
        frontCouponsActivityStoreGroupDTOList.stream().forEach(c -> {
            list.addAll(c.getFrontCouponsActivityPageDTOList());
        });
        String ipAddr = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("ip", ipAddr);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_COUPONS_PV_QUEUE, JSON.toJSONString(map));
        return frontCouponsActivityStoreGroupDTOList;
    }

    /**
     * 功能描述:
     * 〈我的优惠券列表〉
     *
     * @author : 刘远杰
     */

    @Override
    public List<FrontMyCouponsPageDTO> myCouponsList(@RequestParam Map<String, Object> params) {
        List<FrontMyCouponsPageDTO> frontMyCouponsPageDTOList = new ArrayList<>();

        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
        Map<String, RangConditionsToTimeModelDTO> rangConditionsToTimeModelMap = pageModelDTO.getRangConditionsToTimeModelMap();

        sortFileds.put("couponsState", "asc");
        if (params.get("memberId") != null && StringUtils.isNotBlank(params.get("memberId").toString())) {
            // 查询指定会员的优惠券
            equalsSearchCondition.put("memberId", Long.parseLong(params.get("memberId").toString()));
        }

        if (params.get("couponsState") == null || StringUtils.isBlank(params.get("couponsState").toString())) {
            // 查询所有状态，按照领取时间倒序排序
            sortFileds.put("createDate", "desc");
        } else if (CouponsEnum.COUPON_CANNT_USE.value() == Integer.parseInt(params.get("couponsState").toString())) {
            // 查询未使用优惠券，按照使用结束时间正序排序
            sortFileds.put("endDate", "asc");
            inSearchCondition.put("couponsState", Arrays.asList(CouponsEnum.COUPON_CANNT_USE.value(), CouponsEnum.COUPON_CAN_USE.value()));
        } else if (CouponsEnum.COUPON_CAN_USE.value() == Integer.parseInt(params.get("couponsState").toString())) {
            // 查询已使用优惠券，按照使用时间倒序排序
            sortFileds.put("useDate", "desc");
            equalsSearchCondition.put("couponsState", CouponsEnum.COUPONS_USED.value());
            // 获得前30天使用券
            RangConditionsToTimeModelDTO rangConditionsToTimeModelDTO = new RangConditionsToTimeModelDTO();
            Date nowDay = DateUtils.parse(DateUtils.format(new Date(), DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN);
            Date beginDay = DateUtils.addDateDays(nowDay, -30);
            Timestamp timestamp = new Timestamp(beginDay.getTime());
            rangConditionsToTimeModelDTO.setBeginTime(timestamp);
            rangConditionsToTimeModelMap.put("useDate", rangConditionsToTimeModelDTO);
        } else if (CouponsEnum.COUPONS_USED.value() == Integer.parseInt(params.get("couponsState").toString())) {
            // 查询已过期优惠券，按照使用结束时间倒序排序
            sortFileds.put("endDate", "desc");
            equalsSearchCondition.put("couponsState", CouponsEnum.COUPONS_INVALID.value());
            // 获得前30天过期券
            RangConditionsToTimeModelDTO rangConditionsToTimeModelDTO = new RangConditionsToTimeModelDTO();
            Date nowDay = DateUtils.parse(DateUtils.format(new Date(), DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN);
            Date beginDay = DateUtils.addDateDays(nowDay, -30);
            Timestamp timestamp = new Timestamp(beginDay.getTime());
            rangConditionsToTimeModelDTO.setBeginTime(timestamp);
            rangConditionsToTimeModelMap.put("endDate", rangConditionsToTimeModelDTO);
        } else {
            return Collections.emptyList();
        }

        if (params.get("storeId") != null && StringUtils.isNotBlank(params.get("storeId").toString())) {
            // 查询指定店铺的优惠券
            equalsSearchCondition.put("storeId", Long.parseLong(params.get("storeId").toString()));
        }

        if (params.get("id") != null && StringUtils.isNotBlank(params.get("id").toString())) {
            // 查询指定优惠券
            equalsSearchCondition.put("id", Long.parseLong(params.get("id").toString()));
        }

        // 获取我的优惠券列表
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);
        List<String> resultList = pageModelDTO.getJsonRsList();

        if (CollectionUtils.isNotEmpty(resultList)) {
            resultList.forEach(result -> {
                FrontMyCouponsPageDTO frontMyCouponsPageDTO = JSONObject.parseObject(result, FrontMyCouponsPageDTO.class);
                // 计算是否是新到  优惠券自领取6小时内的显示新到标识，超过6小时显示该优惠券类型标识
                if (frontMyCouponsPageDTO.getCreateDate() != null) {
                    long betweenHour = DateUtil.between(new Date(), frontMyCouponsPageDTO.getCreateDate(), DateUnit.HOUR);
                    frontMyCouponsPageDTO.setCouponsFlag(betweenHour > 6 ? null : 0);
                }

                // 计算是不是将过期  距离有效期24小时内的显示此标识
                long betweenEndHour = DateUtil.between(frontMyCouponsPageDTO.getEndDate(), new Date(), DateUnit.HOUR);
                if (betweenEndHour < HOURS) {
                    frontMyCouponsPageDTO.setCouponsFlag(1);
                }

                frontMyCouponsPageDTOList.add(frontMyCouponsPageDTO);
            });
        }
        return frontMyCouponsPageDTOList;
    }

    /**
     * 功能描述:
     * 〈优惠券详情数据〉
     *
     * @param activityId 活动id
     * @param memberId   会员id
     * @author : 刘远杰
     */

    @Override
    public FrontCouponsActivityDetailDTO activityDetail(@RequestParam("activityId") Long activityId,
                                                        @RequestParam(value = "couponsId", required = false) Long couponsId,
                                                        @RequestParam(value = "memberId", required = false) Long memberId) {
        // 获取优惠券活动数据
        String activityJson = esDataUtils.getDateById(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, activityId.toString());
        FrontCouponsActivityDetailDTO frontCouponsActivityDetailDTO = JSONObject.parseObject(activityJson, FrontCouponsActivityDetailDTO.class);
        if (frontCouponsActivityDetailDTO == null) {
            return null;
        }
        // 设置活动用户领取状态
        if (frontCouponsActivityDetailDTO != null && frontCouponsActivityDetailDTO.getId() != null) {
            // 填充默认状态为未领取
            frontCouponsActivityDetailDTO.setReceivedFlag(0);
            if (memberId != null) {
                // 获取该优惠券用户领取信息
                PageModelDTO pageModelDTO = new PageModelDTO();
                pageModelDTO.setIsPage(false);
                // 查询用户是否存在未使用的优惠券
                Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                equalsSearchCondition.put("activityId", activityId);
                equalsSearchCondition.put("memberId", memberId);
                Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
                inSearchCondition.put("couponsState", Arrays.asList(CouponsEnum.COUPON_CANNT_USE.value(), CouponsEnum.COUPON_CAN_USE.value()));
                esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);

                List<String> jsonRsList = pageModelDTO.getJsonRsList();
                if (CollectionUtils.isNotEmpty(jsonRsList)) {
                    // 用户已领取该优惠券，设置优惠券领取状态为已领取
                    frontCouponsActivityDetailDTO.setReceivedFlag(1);
                    if (couponsId != null) {
                        String date = esDataUtils.getDateById(ElasticSearchConstant.MEMBER_COUPONS_INDEX, couponsId.toString());
                        MemberCouponsIndexDTO memberCouponsIndexDTO = JSONObject.parseObject(date, MemberCouponsIndexDTO.class);
                        if (memberCouponsIndexDTO == null) {
                            memberCouponsIndexDTO = JSONObject.parseObject(jsonRsList.get(0), MemberCouponsIndexDTO.class);
                        }
                        frontCouponsActivityDetailDTO.setEndDate(memberCouponsIndexDTO.getEndDate());
                    } else {
                        MemberCouponsIndexDTO memberCouponsIndexDTO = JSONObject.parseObject(jsonRsList.get(0), MemberCouponsIndexDTO.class);
                        frontCouponsActivityDetailDTO.setEndDate(memberCouponsIndexDTO.getEndDate());
                    }
                }
            }
        }
        return frontCouponsActivityDetailDTO;
    }

    /**
     * 功能描述:
     * 〈获得优惠券活动跳转类型〉
     *
     * @param id 优惠券活动id
     * @author : 刘远杰
     */

    @Override
    public CouponsToRelationDTO goGoodsList(Long id) {
        CouponsToRelationDTO couponsToRelationDTO = new CouponsToRelationDTO();
        couponsToRelationDTO.setActivityId(id);
        // 获得活动数据
        String activityJson = esDataUtils.getDateById(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, id.toString());
        CouponsActivityIndexDTO couponsActivityIndexDTO = JSONObject.parseObject(activityJson, CouponsActivityIndexDTO.class);
        if (couponsActivityIndexDTO == null) {
            throw new ServiceException(ActivityStatusCode.NOTFUND_ACTIVITY_EXCEPTION);
        }
        couponsToRelationDTO.setStoreId(couponsActivityIndexDTO.getStoreId());

        Integer activityGoodsScope = couponsActivityIndexDTO.getActivityGoodsScope();
        if (CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == activityGoodsScope) {
            // 店铺活动，跳转店铺首页
            couponsToRelationDTO.setRedType(0);
            return couponsToRelationDTO;
        }
        // 获得所有活动下商品
        List<Long> goodsIds = this.getAllGoodsByActivityId(id);

        // 查询活动商品是否可售
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
        inSearchCondition.put("goodsId", goodsIds);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        // 上架
        equalsSearchCondition.put("specShow", 1);
        // 商品库存大于0
        Map<String, RangConditionDTO> rangConditionMap = pageModelDTO.getRangConditionMap();
        RangConditionDTO rangConditionDTO = new RangConditionDTO();
        rangConditionDTO.setBeginValue("1");
        rangConditionMap.put("specStorage", rangConditionDTO);

        // 查询 上架，库存大于0的商品规格
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);
        if (CollectionUtils.isEmpty(pageModelDTO.getJsonRsList()) || pageModelDTO.getJsonRsList().size() > 1) {
            // 商品数量大于1，跳转商品列表
            couponsToRelationDTO.setRedType(1);
        } else {
            // 跳转商品列表
            couponsToRelationDTO.setRedType(2);
            JSONObject jsonObject = JSONObject.parseObject(pageModelDTO.getJsonRsList().get(0));
            couponsToRelationDTO.setSpecId(Long.parseLong(jsonObject.get("id").toString()));
        }
        return couponsToRelationDTO;
    }

    /**
     * 功能描述:
     * 〈商品详情页优惠券列表〉
     *
     * @author : 刘远杰
     */

    @Override
    public GoodsDetailCouponsListDTO goodsDetailCouponsList(@RequestParam Map<String, Object> params) {
        GoodsDetailCouponsListDTO goodsDetailCouponsListDTO = new GoodsDetailCouponsListDTO();
        if (params.get("goodsId") == null || StringUtils.isBlank(params.get("goodsId").toString())) {
            throw new ServiceException(ActivityStatusCode.SPUID_IS_NULL);
        }
        Long relationId = Long.parseLong(params.get("goodsId").toString());
        Long memberId = null;
        if (params.get("memberId") != null && StringUtils.isNotBlank(params.get("memberId").toString())) {
            memberId = Long.parseLong(params.get("memberId").toString());
        }

        // 获得商品所有可领取优惠券
        List<String> canResultList = getGoodsAllCoupons(relationId);

        if (params.get("memberId") == null || StringUtils.isBlank(params.get("memberId").toString())) {
            // 未登录，展示所有商品关联优惠券
            List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = convertAndSortCoupons(canResultList);
            goodsDetailCouponsListDTO.getCanRecList().addAll(frontCouponsActivityPageDTOList);
        } else {
            // 已登录，移除已领取优惠券
            List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = this.removeReceivedCoupons(memberId, canResultList);
            // 根据优惠券面额倒序排序
            frontCouponsActivityPageDTOList.sort(Comparator.comparing(FrontCouponsActivityPageDTO::getFaceValue).reversed());
            goodsDetailCouponsListDTO.getCanRecList().addAll(frontCouponsActivityPageDTOList);

            // 获取用户已领取优惠券
            List<FrontMyCouponsPageDTO> frontMyCouponsPageDTOList = getGoodsRecedCoupons(relationId, memberId);
            goodsDetailCouponsListDTO.getRecedList().addAll(frontMyCouponsPageDTOList);
        }
        return goodsDetailCouponsListDTO;
    }


    /**
     * 功能描述:
     * 〈购物车店铺优惠券列表〉
     *
     * @author : 刘远杰
     */

    @Override
    public GoodsDetailCouponsListDTO cartStoreCouponsList(@RequestParam Map<String, Object> params) {
        GoodsDetailCouponsListDTO goodsDetailCouponsListDTO = new GoodsDetailCouponsListDTO();
        if (params.get("storeId") == null || StringUtils.isBlank(params.get("storeId").toString())) {
            throw new ServiceException(ActivityStatusCode.STOREID_IS_NULL);
        }
        Long relationId = Long.parseLong(params.get("storeId").toString());
        Long memberId = null;
        if (params.get("memberId") != null && StringUtils.isNotBlank(params.get("memberId").toString())) {
            memberId = Long.parseLong(params.get("memberId").toString());
        }

        // 查询店铺所有可领优惠券
        List<String> canResultList = getStoreAllCoupons(relationId);

        if (params.get("memberId") == null || StringUtils.isBlank(params.get("memberId").toString())) {
            // 未登录，展示所有商品关联优惠券
            List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = convertAndSortCoupons(canResultList);
            goodsDetailCouponsListDTO.getCanRecList().addAll(frontCouponsActivityPageDTOList);
        } else {
            // 已登录，移除已领取优惠券
            List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = this.removeReceivedCoupons(memberId, canResultList);

            frontCouponsActivityPageDTOList.sort(Comparator.comparing(FrontCouponsActivityPageDTO::getFaceValue).reversed());

            goodsDetailCouponsListDTO.getCanRecList().addAll(frontCouponsActivityPageDTOList);

            // 获得店铺已领取优惠券集合
            List<FrontMyCouponsPageDTO> frontMyCouponsPageDTOList = getStoreRecedCoupons(relationId, memberId);
            goodsDetailCouponsListDTO.getRecedList().addAll(frontMyCouponsPageDTOList);
        }
        return goodsDetailCouponsListDTO;
    }

    /**
     * 功能描述:
     * 〈获得店铺优惠券活动商品spuids〉
     *
     * @param activityId
     * @author : 刘远杰
     */

    @Override
    public List<Long> getAllGoodsByActivityId(@RequestParam("activityId") Long activityId) {
        // 获取活动商品
        String activityJson = esDataUtils.getDateById(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, activityId.toString());
        CouponsActivityIndexDTO couponsActivityIndexDTO = JSONObject.parseObject(activityJson, CouponsActivityIndexDTO.class);
        if (couponsActivityIndexDTO == null) {
            throw new ServiceException(ActivityStatusCode.NOTFUND_ACTIVITY_EXCEPTION);
        }
        List<CouponsGoodsDTO> relationList = couponsActivityIndexDTO.getGoodsList();

        Set<Long> goodsIds = new HashSet<>();
        for (CouponsGoodsDTO relation : relationList) {

            PageModelDTO pageModelDTO = new PageModelDTO();
            pageModelDTO.setIsPage(false);
            Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
            equalsSearchCondition.put("storeId", couponsActivityIndexDTO.getStoreId());

            if (CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value() == relation.getActivityGoodsScope()) {
                // 指定商品
                goodsIds.add(relation.getRelationId());
            } else {
                if (CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value() == relation.getActivityGoodsScope()) {
                    // 指定店铺分类
                    equalsSearchCondition.put("secondStoreGoodsGcId", relation.getRelationId());
                } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value() == relation.getActivityGoodsScope()) {
                    // 指定品牌
                    equalsSearchCondition.put("brandId", relation.getRelationId());
                }
                // 店铺通用优惠券直接查询所有商品
                // 获取商品数据
                esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_INDEX);
                List<String> jsonRsList = pageModelDTO.getJsonRsList();
                for (String json : jsonRsList) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    goodsIds.add(Long.parseLong(jsonObject.get("id").toString()));
                }
            }
        }
        return new ArrayList<Long>(goodsIds);
    }

    /**
     * 功能描述:
     * 〈获得商品所有可领取优惠券〉
     *
     * @param relationId 商品id
     * @author : 刘远杰
     */
    private List<String> getGoodsAllCoupons(Long relationId) {
        // 1.获得商品信息
        String esData = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, relationId.toString());
        JSONObject goodsDetail = JSONObject.parseObject(esData);
        if (goodsDetail == null) {
            throw new ServiceException(ActivityStatusCode.NOTFUND_GOODS_EXCEPTION);
        }
        Long storeId = Long.parseLong(String.valueOf(goodsDetail.get("storeId")));
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModelDTO.getSubEqualsSearchCondition();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        sortFileds.put("faceValue", "desc");
        equalsSearchCondition.put("storeId", storeId);
        equalsSearchCondition.put("activityState", CouponsEnum.ACTIVITY_STATE_START.value());
        // 查询剩余优惠券数量大于0 或者 优惠券总数为0（不限量）
        List<Map<String, Object>> orSearchConditionList = pageModelDTO.getOrSearchConditionList();
        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("totalNum", 0);
        orSearchConditionList.add(mapCondition);
        RangConditionDTO rangConditionDTO = new RangConditionDTO();
        rangConditionDTO.setBeginValue("1");
        Map<String, Object> rangConditionMap = new HashMap<>();
        rangConditionMap.put("surplusNum", rangConditionDTO);
        orSearchConditionList.add(rangConditionMap);

        List<String> resultList = new ArrayList<>();
        // 1.查询店铺级别优惠券
        Map<String, Object> map = new HashMap<>();
        map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value());
        subEqualsSearchCondition.put("goodsList", map);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX);
        resultList.addAll(pageModelDTO.getJsonRsList());

        // 2.查询品牌级别优惠券
        if (goodsDetail.get("brandId") != null && StringUtils.isNotBlank(goodsDetail.get("brandId").toString())) {
            Long brandId = Long.parseLong(String.valueOf(goodsDetail.get("brandId")));
            subEqualsSearchCondition.clear();
            pageModelDTO.getJsonRsList().clear();
            map.clear();
            map.put("relationId", brandId);
            map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value());
            subEqualsSearchCondition.put("goodsList", map);
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX);
            resultList.addAll(pageModelDTO.getJsonRsList());
        }

        // 3.查询商品级别优惠券
        pageModelDTO.getJsonRsList().clear();
        subEqualsSearchCondition.clear();
        map.clear();
        map.put("relationId", relationId);
        map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value());
        subEqualsSearchCondition.put("goodsList", map);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX);
        resultList.addAll(pageModelDTO.getJsonRsList());
        // 4.查询店铺分类优惠券
        if (goodsDetail.get("secondStoreGoodsGcId") != null && StringUtils.isNotBlank(goodsDetail.get("secondStoreGoodsGcId").toString())) {
            Long secondStoreGoodsGcId = Long.parseLong(String.valueOf(goodsDetail.get("secondStoreGoodsGcId")));
            subEqualsSearchCondition.clear();
            pageModelDTO.getJsonRsList().clear();
            map.clear();
            map.put("relationId", secondStoreGoodsGcId);
            map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value());
            subEqualsSearchCondition.put("goodsList", map);
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX);
            resultList.addAll(pageModelDTO.getJsonRsList());
        }

        return resultList;
    }

    /**
     * 功能描述:
     * 〈获得店铺所有优惠券〉
     *
     * @param relationId 店铺id
     * @author : 刘远杰
     */
    private List<String> getStoreAllCoupons(Long relationId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Object> filterSearchCondition = pageModelDTO.getEqualsFilterSearchCondition();
        sortFileds.put("faceValue", "desc");
        filterSearchCondition.put("storeId", relationId);
        filterSearchCondition.put("activityState", CouponsEnum.ACTIVITY_STATE_START.value());
        // 查询剩余优惠券数量大于0 或者 优惠券总数为0（不限量）
        List<Map<String, Object>> orSearchConditionList = pageModelDTO.getOrSearchConditionList();
        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("totalNum", 0);
        orSearchConditionList.add(mapCondition);
        RangConditionDTO rangConditionDTO = new RangConditionDTO();
        rangConditionDTO.setBeginValue("1");
        Map<String, Object> rangConditionMap = new HashMap<>();
        rangConditionMap.put("surplusNum", rangConditionDTO);
        orSearchConditionList.add(rangConditionMap);

        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX);
        return pageModelDTO.getJsonRsList();
    }

    /**
     * 功能描述:
     * 〈获得商品已领取优惠券集合〉
     *
     * @param relationId 关联商品id
     * @param memberId   会员id
     * @author : 刘远杰
     */
    private List<FrontMyCouponsPageDTO> getGoodsRecedCoupons(Long relationId, Long memberId) {
        // 1.获得商品信息
        String esData = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, relationId.toString());
        JSONObject goodsDetail = JSONObject.parseObject(esData);
        if (goodsDetail == null) {
            throw new ServiceException(ActivityStatusCode.NOTFUND_GOODS_EXCEPTION);
        }
        Long storeId = Long.parseLong(String.valueOf(goodsDetail.get("storeId")));
        // todo 获取店铺分类

        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModelDTO.getSubEqualsSearchCondition();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        sortFileds.put("faceValue", "desc");
        // 商品店铺、领取会员、未使用
        equalsSearchCondition.put("storeId", storeId);
        equalsSearchCondition.put("memberId", memberId);
        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
        inSearchCondition.put("couponsState", Arrays.asList(CouponsEnum.COUPON_CANNT_USE.value(), CouponsEnum.COUPON_CAN_USE.value()));

        List<String> memberCouponsResultList = new ArrayList<>();
        // 1.查询店铺级别优惠券
        Map<String, Object> map = new HashMap<>();
        map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value());
        subEqualsSearchCondition.put("goodsList", map);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);
        memberCouponsResultList.addAll(pageModelDTO.getJsonRsList());

        // 2.查询品牌级别优惠券
        if (goodsDetail.get("brandId") != null && StringUtils.isNotBlank(goodsDetail.get("brandId").toString())) {
            Long brandId = Long.parseLong(String.valueOf(goodsDetail.get("brandId")));
            subEqualsSearchCondition.clear();
            pageModelDTO.getJsonRsList().clear();
            map.clear();
            map.put("relationId", brandId);
            map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value());
            subEqualsSearchCondition.put("goodsList", map);
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);
            memberCouponsResultList.addAll(pageModelDTO.getJsonRsList());
        }

        // 3.查询商品级别优惠券
        pageModelDTO.getJsonRsList().clear();
        subEqualsSearchCondition.clear();
        map.clear();
        map.put("relationId", relationId);
        map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value());
        subEqualsSearchCondition.put("goodsList", map);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);
        memberCouponsResultList.addAll(pageModelDTO.getJsonRsList());

        // 4.查询店铺分类优惠券
        if (goodsDetail.get("secondStoreGoodsGcId") != null && StringUtils.isNotBlank(goodsDetail.get("secondStoreGoodsGcId").toString())) {
            Long secondStoreGoodsGcId = Long.parseLong(String.valueOf(goodsDetail.get("secondStoreGoodsGcId")));
            subEqualsSearchCondition.clear();
            pageModelDTO.getJsonRsList().clear();
            map.clear();
            map.put("relationId", secondStoreGoodsGcId);
            map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value());
            subEqualsSearchCondition.put("goodsList", map);
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);
            memberCouponsResultList.addAll(pageModelDTO.getJsonRsList());
        }

        // 排序
        List<FrontMyCouponsPageDTO> frontMyCouponsPageDTOList = convertAndSortMyCoupons(memberCouponsResultList);
        return frontMyCouponsPageDTOList;
    }

    /**
     * 功能描述:
     * 〈获得店铺已领取优惠券集合〉
     *
     * @param relationId 关联店铺id
     * @param memberId   会员id
     * @author : 刘远杰
     */
    private List<FrontMyCouponsPageDTO> getStoreRecedCoupons(Long relationId, Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
        sortFileds.put("faceValue", "desc");
        // 商品店铺、领取会员、未使用优惠券
        equalsSearchCondition.put("storeId", relationId);
        equalsSearchCondition.put("memberId", memberId);
        inSearchCondition.put("couponsState", Arrays.asList(CouponsEnum.COUPON_CANNT_USE.value(), CouponsEnum.COUPON_CAN_USE.value()));

        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);
        List<String> memberCouponsResultList = pageModelDTO.getJsonRsList();

        // 排序
        List<FrontMyCouponsPageDTO> frontMyCouponsPageDTOList = convertAndSortMyCoupons(memberCouponsResultList);
        return frontMyCouponsPageDTOList;
    }

    /**
     * 功能描述:
     * 〈移除已领取优惠券〉
     *
     * @param memberId   会员id
     * @param resultList es查询结果
     * @author : 刘远杰
     */
    private List<FrontCouponsActivityPageDTO> removeReceivedCoupons(Long memberId, List<String> resultList) {
        List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(resultList)) {
            // es查询结果为空
            return frontCouponsActivityPageDTOList;
        }

        for (String result : resultList) {
            // 转化es数据为实体
            FrontCouponsActivityPageDTO frontCouponsActivityPageDTO = JSONObject.parseObject(result, FrontCouponsActivityPageDTO.class);
            // 如果优惠券不是不限量并且优惠券剩余数量小于1  不放入集合中
            if (frontCouponsActivityPageDTO.getTotalNum() != 0 && frontCouponsActivityPageDTO.getSurplusNum() < 1) {
                continue;
            }
            if (memberId == null) {
                // 用户未登录展示所有优惠券
                frontCouponsActivityPageDTOList.add(frontCouponsActivityPageDTO);
            } else {
                // 用户已登录过滤已领取优惠券及领取上限优惠券
                CouponsActivityIndexDTO couponsActivityIndexDTO = JSONObject.parseObject(result, CouponsActivityIndexDTO.class);

                // 获取用户优惠券信息
                PageModelDTO pageModelDTO = new PageModelDTO();
                pageModelDTO.setIsPage(false);
                // 查询用户已领取的当前优惠券
                Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                equalsSearchCondition.put("memberId", memberId);
                equalsSearchCondition.put("activityId", couponsActivityIndexDTO.getId());

                // 用户优惠券查询
                esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.MEMBER_COUPONS_INDEX);
                List<String> memberCouponsResultList = pageModelDTO.getJsonRsList();

                if (CollectionUtils.isNotEmpty(memberCouponsResultList)) {
                    // 已领取该优惠券
                    if (!(couponsActivityIndexDTO.getPersonLimit() > 0 && memberCouponsResultList.size() >= couponsActivityIndexDTO.getPersonLimit())) {
                        // 领取数量不超出领取限制
                        frontCouponsActivityPageDTOList.add(frontCouponsActivityPageDTO);
                        for (String memberCouponsResult : memberCouponsResultList) {
                            MemberCouponsIndexDTO memberCouponsIndexDTO = JSONObject.parseObject(memberCouponsResult, MemberCouponsIndexDTO.class);
                            if (CouponsEnum.COUPON_CANNT_USE.value() == memberCouponsIndexDTO.getCouponsState()
                                    || CouponsEnum.COUPON_CAN_USE.value() == memberCouponsIndexDTO.getCouponsState()) {
                                // 存在未使用优惠券，移除列表不展示
                                frontCouponsActivityPageDTOList.remove(frontCouponsActivityPageDTO);
                                break;
                            }
                        }
                    }
                } else {
                    frontCouponsActivityPageDTOList.add(frontCouponsActivityPageDTO);
                }
            }
        }

        return frontCouponsActivityPageDTOList;
    }

    /**
     * 功能描述:
     * 〈按照店铺对优惠券分组〉
     *
     * @param frontCouponsActivityPageDTOList 优惠券集合
     * @author : 刘远杰
     */
    private List<FrontCouponsActivityStoreGroupDTO> getFrontCouponsActivityStoreGroupDTOS(List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList) {
        Map<Long, List<FrontCouponsActivityPageDTO>> map = frontCouponsActivityPageDTOList.stream().collect(Collectors.groupingBy(FrontCouponsActivityPageDTO::getStoreId));

        List<FrontCouponsActivityStoreGroupDTO> frontCouponsActivityStoreGroupDTOList = new ArrayList<>();
        for (Long key : map.keySet()) {
            FrontCouponsActivityStoreGroupDTO frontCouponsActivityStoreGroupDTO = new FrontCouponsActivityStoreGroupDTO();
            // 获取店铺优惠券
            List<FrontCouponsActivityPageDTO> storeCouponsActivityPageDTOList = map.get(key);
            frontCouponsActivityStoreGroupDTO.setStoreId(storeCouponsActivityPageDTOList.get(0).getStoreId());
            frontCouponsActivityStoreGroupDTO.setStoreName(storeCouponsActivityPageDTOList.get(0).getStoreName().trim());
            frontCouponsActivityStoreGroupDTO.setStoreLogo(storeCouponsActivityPageDTOList.get(0).getStoreLogo());
            frontCouponsActivityStoreGroupDTO.setFrontCouponsActivityPageDTOList(storeCouponsActivityPageDTOList);
            frontCouponsActivityStoreGroupDTOList.add(frontCouponsActivityStoreGroupDTO);
        }
        return frontCouponsActivityStoreGroupDTOList;
    }

    /**
     * 功能描述:
     * 〈我的优惠券json转list，按照面额排序〉
     *
     * @param memberCouponsResultList
     * @author : 刘远杰
     */
    private List<FrontMyCouponsPageDTO> convertAndSortMyCoupons(List<String> memberCouponsResultList) {
        List<FrontMyCouponsPageDTO> frontMyCouponsPageDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(memberCouponsResultList)) {
            for (String memberCouponsResult : memberCouponsResultList) {
                FrontMyCouponsPageDTO frontMyCouponsPageDTO = JSONObject.parseObject(memberCouponsResult, FrontMyCouponsPageDTO.class);
                frontMyCouponsPageDTOList.add(frontMyCouponsPageDTO);
            }
        }
        // 按照优惠券面额降序排序
        frontMyCouponsPageDTOList.sort(Comparator.comparing(FrontMyCouponsPageDTO::getFaceValue).reversed());

        return frontMyCouponsPageDTOList;
    }

    /**
     * 功能描述:
     * 〈优惠券活动json转list，按照面额排序〉
     *
     * @param canResultList
     * @author : 刘远杰
     */
    private List<FrontCouponsActivityPageDTO> convertAndSortCoupons(List<String> canResultList) {
        List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(canResultList)) {
            for (String result : canResultList) {
                FrontCouponsActivityPageDTO frontCouponsActivityPageDTO = JSONObject.parseObject(result, FrontCouponsActivityPageDTO.class);
                frontCouponsActivityPageDTOList.add(frontCouponsActivityPageDTO);
            }
        }
        // 按照优惠券面额降序排序
        frontCouponsActivityPageDTOList.sort(Comparator.comparing(FrontCouponsActivityPageDTO::getFaceValue).reversed());

        return frontCouponsActivityPageDTOList;
    }

}
