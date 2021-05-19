/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;
import com.leimingtech.modules.dto.reduce.ReduceRuleDTO;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.moudle.search.modules.service.ReduceSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 满减搜索
 *
 * @author chengqian
 */
@Slf4j
@Service
public class ReduceSearchServiceImpl implements ReduceSearchService {

    private static final MonitorLogger LOGGER = MonitorLoggerFactory.getMonitorLogger(ReduceSearchServiceImpl.class);

    @Autowired
    private EsDataUtils esDataUtils;

    @Override
    public FrontReduceActivityPageDTO goodsDetailReduceList(Long goodsId, Long brandId, Long storeId, Long storeGoodsClass) {

        List<String> resultList = getGoodsAllReduce(goodsId, brandId, storeId, storeGoodsClass);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        return convertAndSortCoupons(resultList);
    }

    private FrontReduceActivityPageDTO convertAndSortCoupons(List<String> resultList) {
        List<FrontReduceActivityPageDTO> frontReduceActivityPageList = new ArrayList<>();
        for (String result : resultList) {
            ReduceActivityIndexDTO reduceActivityIndexDTO = JSONObject.parseObject(result, ReduceActivityIndexDTO.class);

            FrontReduceActivityPageDTO frontReduceActivityPageDTO = new FrontReduceActivityPageDTO();
            frontReduceActivityPageDTO.setId(reduceActivityIndexDTO.getId());
            frontReduceActivityPageDTO.setRuleType(reduceActivityIndexDTO.getRuleType());
            frontReduceActivityPageDTO.setEndDate(reduceActivityIndexDTO.getEndDate());
            frontReduceActivityPageDTO.setSelectFlag(ReduceActivityEnum.REDUCE_FLAG_NO.value());

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
        if (frontReduceActivityPageList.size() > 0) {
            return frontReduceActivityPageList.get(0);
        } else {
            return null;
        }
    }

    private List<String> getGoodsAllReduce(Long goodsId, Long brandId, Long storeId, Long storeGoodsClass) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("storeId", storeId);

        List<String> resultList = new ArrayList<>();
        resultList.addAll(pageModelDTO.getJsonRsList());
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
        if (null != storeGoodsClass) {
            relationIdList.add(storeGoodsClass);
        }
        map.put("relationId", relationIdList);
        Map<String, Map<String, Object>> subInSearchCondition = pageModelDTO.getSubInFilterSearchCondition();
        subInSearchCondition.put("goodsList", map);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.REDUCE_ACTIVITY_INDEX);
        resultList.addAll(pageModelDTO.getJsonRsList());
        return resultList;
    }
}
