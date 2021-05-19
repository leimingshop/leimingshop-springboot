/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.reduce.impl;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.RangConditionDTO;
import com.leimingtech.modules.dto.coupons.CouponsToRelationDTO;
import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.dto.reduce.ReduceRuleDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.service.reduce.ReduceActivitySearchService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * <满减活动es搜索>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */
@Slf4j
@Service

public class ReduceActivitySearchServiceImpl implements ReduceActivitySearchService {

    @Autowired
    private EsDataUtils esDataUtils;

    /**
     * 功能描述:
     * 〈商品详情页满减活动列表〉
     *
     * @author : 刘远杰
     */

    @Override
    public List<FrontReduceActivityPageDTO> goodsDetailCouponsList(@RequestParam Map<String, Object> params) {
        if (params.get("goodsId") == null || StringUtils.isBlank(params.get("goodsId").toString())) {
            throw new ServiceException(ActivityStatusCode.SPUID_IS_NULL);
        }
        Long relationId = Long.parseLong(params.get("goodsId").toString());

        // 查询商品所有满减活动
        List<String> resultList = getGoodsAllReduce(relationId);

        // 展示所有商品关联优惠券
        if (params.get("activityType") != null && StringUtils.isNotBlank(params.get("activityType").toString())
                && ActivityTypeEnum.REDUCE_ACTIVITY.value() == Integer.parseInt(params.get("activityType").toString())
                && params.get("activityId") != null && StringUtils.isNotBlank(params.get("activityId").toString())) {
            // 满减活动且活动id存在，指定活动勾选
            return convertAndSortCoupons(resultList, Long.parseLong(params.get("activityId").toString()));
        } else {
            // 活动不勾选
            return convertAndSortCoupons(resultList, null);
        }
    }


    @Override
    public FrontReduceActivityPageDTO frontActivityDetail(Long activityId) {
        String activityJson = esDataUtils.getDateById(ElasticSearchConstant.REDUCE_ACTIVITY_INDEX, activityId.toString());
        ArrayList<String> resultList = new ArrayList<>();
        resultList.add(activityJson);
        List<FrontReduceActivityPageDTO> frontReduceActivityPageDTOList = convertAndSortCoupons(resultList, null);
        if (CollectionUtils.isNotEmpty(frontReduceActivityPageDTOList)) {
            return frontReduceActivityPageDTOList.get(0);
        } else {
            throw new ServiceException(ActivityStatusCode.NOTFUND_ACTIVITY_EXCEPTION);
        }
    }

    /**
     * 功能描述:
     * 〈满减活动列表〉
     *
     * @author : 刘远杰
     */

    @Override
    public List<ReduceActivityIndexDTO> goodsReduceList(@RequestParam Map<String, Object> params) {
        if (params.get("goodsId") == null || StringUtils.isBlank(params.get("goodsId").toString())) {
            throw new ServiceException(ActivityStatusCode.SPUID_IS_NULL);
        }
        Long relationId = Long.parseLong(params.get("goodsId").toString());

        // 查询商品所有满减活动
        List<String> resultList = getGoodsAllReduce(relationId);
        // 活动不勾选
        List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resultList)) {
            resultList.forEach(result -> {
                ReduceActivityIndexDTO reduceActivityIndexDTO = JSONObject.parseObject(result, ReduceActivityIndexDTO.class);

                reduceActivityIndexDTOList.add(reduceActivityIndexDTO);
            });
        }

        return reduceActivityIndexDTOList;
    }

    /**
     * 功能描述:
     * 〈满减活动详情数据〉
     *
     * @param activityId 活动id
     * @author : 刘远杰
     */

    @Override
    public ReduceActivityIndexDTO activityDetail(@RequestParam("activityId") Long activityId) {
        // 获取优惠券活动数据
        String activityJson = esDataUtils.getDateById(ElasticSearchConstant.REDUCE_ACTIVITY_INDEX, activityId.toString());
        ReduceActivityIndexDTO reduceActivityIndexDTO = JSONObject.parseObject(activityJson, ReduceActivityIndexDTO.class);
        if (reduceActivityIndexDTO == null) {
            return null;
        }
        reduceActivityIndexDTO.getRuleList().sort(Comparator.comparing(ReduceRuleDTO::getReduceAmount));
        return reduceActivityIndexDTO;
    }

    /**
     * 功能描述:
     * 〈获得商品所有满减活动〉
     *
     * @param relationId 商品id
     * @author : 刘远杰
     */
    private List<String> getGoodsAllReduce(Long relationId) {
        // 1.获得商品信息
        String esData = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, relationId.toString());
        JSONObject goodsDetail = JSONObject.parseObject(esData);
        if (goodsDetail == null) {
            throw new ServiceException(ActivityStatusCode.NOTFUND_GOODS_EXCEPTION);
        }
        Long storeId = Long.parseLong(String.valueOf(goodsDetail.get("storeId")));

        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModelDTO.getSubEqualsSearchCondition();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("storeId", storeId);

        List<String> resultList = new ArrayList<>();
        // 1.查询店铺级别满减活动
        Map<String, Object> map = new HashMap<>();
        map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value());
        subEqualsSearchCondition.put("goodsList", map);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.REDUCE_ACTIVITY_INDEX);
        resultList.addAll(pageModelDTO.getJsonRsList());

        // 2.查询品牌级别满减活动
        if (goodsDetail.get("brandId") != null && StringUtils.isNotBlank(goodsDetail.get("brandId").toString())) {
            Long brandId = Long.parseLong(String.valueOf(goodsDetail.get("brandId")));
            subEqualsSearchCondition.clear();
            pageModelDTO.getJsonRsList().clear();
            map.clear();
            map.put("relationId", brandId);
            map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value());
            subEqualsSearchCondition.put("goodsList", map);
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.REDUCE_ACTIVITY_INDEX);
            resultList.addAll(pageModelDTO.getJsonRsList());
        }

        // 3.查询商品级别优惠券
        pageModelDTO.getJsonRsList().clear();
        subEqualsSearchCondition.clear();
        map.clear();
        map.put("relationId", relationId);
        map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value());
        subEqualsSearchCondition.put("goodsList", map);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.REDUCE_ACTIVITY_INDEX);
        resultList.addAll(pageModelDTO.getJsonRsList());

        // 4.查询分类级别满减活动
        if (goodsDetail.get("secondStoreGoodsGcId") != null && StringUtils.isNotBlank(goodsDetail.get("secondStoreGoodsGcId").toString())) {
            Long secondStoreGoodsGcId = Long.parseLong(String.valueOf(goodsDetail.get("secondStoreGoodsGcId")));
            subEqualsSearchCondition.clear();
            pageModelDTO.getJsonRsList().clear();
            map.clear();
            map.put("relationId", secondStoreGoodsGcId);
            map.put("activityGoodsScope", CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value());
            subEqualsSearchCondition.put("goodsList", map);
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.REDUCE_ACTIVITY_INDEX);
            resultList.addAll(pageModelDTO.getJsonRsList());
        }

        return resultList;
    }

    /**
     * 功能描述:
     * 〈获得满减活动跳转类型〉
     *
     * @param id 满减活动id
     * @author : 刘远杰
     */

    @Override
    public CouponsToRelationDTO goGoodsList(Long id) {
        CouponsToRelationDTO couponsToRelationDTO = new CouponsToRelationDTO();
        couponsToRelationDTO.setActivityId(id);
        // 获得活动数据
        String activityJson = esDataUtils.getDateById(ElasticSearchConstant.REDUCE_ACTIVITY_INDEX, id.toString());
        ReduceActivityIndexDTO reduceActivityIndexDTO = JSONObject.parseObject(activityJson, ReduceActivityIndexDTO.class);
        if (reduceActivityIndexDTO == null) {
            throw new ServiceException(ActivityStatusCode.NOTFUND_ACTIVITY_EXCEPTION);
        }
        couponsToRelationDTO.setStoreId(reduceActivityIndexDTO.getStoreId());

        Integer activityGoodsScope = reduceActivityIndexDTO.getActivityGoodsScope();
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
     * 〈满减活动json转list，按照活动减额倒序排序〉
     *
     * @param resultList 活动json
     * @param activityId 活动id
     * @author : 刘远杰
     */
    private List<FrontReduceActivityPageDTO> convertAndSortCoupons(List<String> resultList, Long activityId) {
        List<FrontReduceActivityPageDTO> frontReduceActivityPageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (String result : resultList) {
                ReduceActivityIndexDTO reduceActivityIndexDTO = JSONObject.parseObject(result, ReduceActivityIndexDTO.class);

                // 设置活动id，是否选中
                FrontReduceActivityPageDTO frontReduceActivityPageDTO = new FrontReduceActivityPageDTO();
                frontReduceActivityPageDTO.setId(reduceActivityIndexDTO.getId());
                frontReduceActivityPageDTO.setRuleType(reduceActivityIndexDTO.getRuleType());
                frontReduceActivityPageDTO.setEndDate(reduceActivityIndexDTO.getEndDate());
                if (reduceActivityIndexDTO.getId().equals(activityId)) {
                    frontReduceActivityPageDTO.setSelectFlag(ReduceActivityEnum.REDUCE_FLAG_YES.value());
                } else {
                    frontReduceActivityPageDTO.setSelectFlag(ReduceActivityEnum.REDUCE_FLAG_NO.value());
                }

                List<ReduceRuleDTO> ruleList = reduceActivityIndexDTO.getRuleList();
                if (CollectionUtils.isNotEmpty(ruleList)) {
                    if (ruleList.size() == 1) {
                        // 单规则，取集合第一个
                        frontReduceActivityPageDTO.setLimitAmount(ruleList.get(0).getLimitAmount());
                        frontReduceActivityPageDTO.setReduceAmount(ruleList.get(0).getReduceAmount());
                        StringBuffer activityDescription = new StringBuffer();
                        if (ReduceActivityEnum.RULE_TYPE_AVG.value() == ruleList.get(0).getRuleType()) {
                            activityDescription.append("每满").append(ruleList.get(0).getLimitAmount().stripTrailingZeros().toPlainString())
                                    .append("元减").append(ruleList.get(0).getReduceAmount().stripTrailingZeros().toPlainString()).append("元");
                            frontReduceActivityPageDTO.setActivityDescription(activityDescription.toString());
                            frontReduceActivityPageList.add(frontReduceActivityPageDTO);
                        } else if (ReduceActivityEnum.RULE_TYPE_NORMAL.value() == ruleList.get(0).getRuleType()
                                || ReduceActivityEnum.RULE_TYPE_LADDER.value() == ruleList.get(0).getRuleType()) {
                            activityDescription.append("满").append(ruleList.get(0).getLimitAmount().stripTrailingZeros().toPlainString())
                                    .append("元减").append(ruleList.get(0).getReduceAmount().stripTrailingZeros().toPlainString()).append("元");
                            frontReduceActivityPageDTO.setActivityDescription(activityDescription.toString());
                            frontReduceActivityPageList.add(frontReduceActivityPageDTO);
                        }

                    } else {
                        // 规则排序
                        ruleList.sort((ReduceRuleDTO o1, ReduceRuleDTO o2) -> {
                            //减额倒序
                            int sort = o2.getReduceAmount().compareTo(o1.getReduceAmount());
                            if (sort != 0) {
                                return sort;
                            } else {
                                // 门槛正序
                                return o1.getLimitAmount().compareTo(o2.getLimitAmount());
                            }
                        });
                        // 多规则，规则排序，去最大优惠规则，拼接展示标题
                        StringBuffer activityDescription = new StringBuffer();
                        for (int i = 0; i < ruleList.size(); i++) {
                            if (i == 0) {
                                frontReduceActivityPageDTO.setLimitAmount(ruleList.get(i).getLimitAmount());
                                frontReduceActivityPageDTO.setReduceAmount(ruleList.get(i).getReduceAmount());
                                activityDescription.append("满")
                                        .append(ruleList.get(i).getLimitAmount().stripTrailingZeros().toPlainString())
                                        .append("元减")
                                        .append(ruleList.get(i).getReduceAmount().stripTrailingZeros().toPlainString()).append("元");
                            } else {
                                activityDescription.append(",满")
                                        .append(ruleList.get(i).getLimitAmount().stripTrailingZeros().toPlainString())
                                        .append("元减")
                                        .append(ruleList.get(i).getReduceAmount().stripTrailingZeros().toPlainString()).append("元");
                            }
                        }
                        frontReduceActivityPageDTO.setActivityDescription(activityDescription.toString());
                        frontReduceActivityPageList.add(frontReduceActivityPageDTO);
                    }
                }
            }
        }
        // 按照满减金额降序排序
        frontReduceActivityPageList.sort((FrontReduceActivityPageDTO o1, FrontReduceActivityPageDTO o2) -> {
            //减额倒序
            int sort = o2.getReduceAmount().compareTo(o1.getReduceAmount());
            if (sort != 0) {
                return sort;
            } else {
                // 门槛正序
                return o1.getLimitAmount().compareTo(o2.getLimitAmount());
            }
        });
        return frontReduceActivityPageList;
    }

    /**
     * 功能描述:
     * 〈获得店铺满减活动商品spuids〉
     *
     * @param activityId 活动id
     * @author : 刘远杰
     */

    @Override
    public List<Long> getAllGoodsByActivityId(@RequestParam("activityId") Long activityId) {
        // 获取活动商品
        String activityJson = esDataUtils.getDateById(ElasticSearchConstant.REDUCE_ACTIVITY_INDEX, activityId.toString());
        ReduceActivityIndexDTO reduceActivityIndexDTO = JSONObject.parseObject(activityJson, ReduceActivityIndexDTO.class);
        if (reduceActivityIndexDTO == null) {
            throw new ServiceException(ActivityStatusCode.NOTFUND_ACTIVITY_EXCEPTION);
        }
        List<ReduceGoodsDTO> relationList = reduceActivityIndexDTO.getGoodsList();

//        List<Long> goodsIds = new ArrayList<>();
        Set<Long> goodsIds = new HashSet<>();
        for (ReduceGoodsDTO relation : relationList) {
            if (CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == relation.getActivityGoodsScope()) {
                // 指定店铺
                PageModelDTO pageModelDTO = new PageModelDTO();
                pageModelDTO.setIsPage(false);
                Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                equalsSearchCondition.put("storeId", reduceActivityIndexDTO.getStoreId());
                // 上架
                equalsSearchCondition.put("goodsShow", 1);
                // 获取店铺该品牌所有商品(上架)数据
                esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_INDEX);
                List<String> jsonRsList = pageModelDTO.getJsonRsList();
                for (String json : jsonRsList) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    goodsIds.add(Long.parseLong(jsonObject.get("id").toString()));
                }
            } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value() == relation.getActivityGoodsScope()) {
                // 指定商品
                goodsIds.add(relation.getRelationId());
            } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value() == relation.getActivityGoodsScope()) {
                // 指定品牌
                PageModelDTO pageModelDTO = new PageModelDTO();
                pageModelDTO.setIsPage(false);
                Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                equalsSearchCondition.put("storeId", reduceActivityIndexDTO.getStoreId());
                equalsSearchCondition.put("brandId", relation.getRelationId());
                // 获取店铺该品牌所有商品数据
                esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_INDEX);
                List<String> jsonRsList = pageModelDTO.getJsonRsList();
                for (String json : jsonRsList) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    goodsIds.add(Long.parseLong(jsonObject.get("id").toString()));
                }
            } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value() == relation.getActivityGoodsScope()) {
                PageModelDTO pageModelDTO = new PageModelDTO();
                pageModelDTO.setIsPage(false);
                Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                equalsSearchCondition.put("storeId", reduceActivityIndexDTO.getStoreId());
                equalsSearchCondition.put("secondStoreGoodsGcId", relation.getRelationId());
                // 获取店铺该品牌所有商品数据
                esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_INDEX);
                List<String> jsonRsList = pageModelDTO.getJsonRsList();
                for (String json : jsonRsList) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    goodsIds.add(Long.parseLong(jsonObject.get("id").toString()));
                }

            }
        }
        return new ArrayList<>(goodsIds);
    }

}
