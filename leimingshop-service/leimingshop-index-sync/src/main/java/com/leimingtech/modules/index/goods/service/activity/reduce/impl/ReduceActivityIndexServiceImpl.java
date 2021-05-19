/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.goods.service.activity.reduce.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.activity.reduce.ReduceActivityIndexVo;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.index.goods.service.activity.reduce.ReduceActivityIndexService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <满减活动索引管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/26
 */
@Slf4j
@Service
public class ReduceActivityIndexServiceImpl implements ReduceActivityIndexService {

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private ReduceActivityService reduceActivityService;

    /**
     * 功能描述:
     * 〈同步满减活动数据〉
     *
     * @param
     * @author : 刘远杰
     */
    @Override
    public void syncReduceActivity() {
        // 获取进行中的满减活动
        List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = reduceActivityService.selectStartReduceActivity();
        // 删除原es
        esDataUtils.deleteIndex(IndexContant.REDUCE_ACTIVITY_NAME);

        // 保存满减活动
        esDataUtils.saveDataBatch(IndexContant.REDUCE_ACTIVITY_NAME, "id",
                JSONArray.toJSONString(reduceActivityIndexDTOList), ReduceActivityIndexVo.class);

        // 商品es活动标签维护
        this.updateGoodsEsReduceTap(ConvertUtils.sourceToTarget(reduceActivityIndexDTOList, ReduceActivityIndexVo.class),
                ReduceActivityEnum.REDUCE_FLAG_YES.value());
    }

    /**
     * 功能描述:
     * 〈满减活动商品es满减标签维护〉
     *
     * @param reduceActivityIndexVoList 满减活动信息
     * @param reduceFlag                0非满减商品 1满减商品
     * @author : 刘远杰
     */
    private void updateGoodsEsReduceTap(List<ReduceActivityIndexVo> reduceActivityIndexVoList, Integer reduceFlag) {
        Set<Long> goodsIds = new HashSet<>();
        // 遍历活动集合，查询对应商品id
        for (ReduceActivityIndexVo reduceActivityIndexVo : reduceActivityIndexVoList) {
            PageModelDTO pageModelDTO = new PageModelDTO();
            pageModelDTO.setIsPage(false);
            Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
            equalsSearchCondition.put("storeId", reduceActivityIndexVo.getStoreId());
            // 获取不同条件商品id
            if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == reduceActivityIndexVo.getActivityGoodsScope()) {
                // 店铺活动，更新店铺所有商品es满减活动标识
                equalsSearchCondition.put("storeId", reduceActivityIndexVo.getStoreId());
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_CLASS.value() == reduceActivityIndexVo.getActivityGoodsScope()) {
                // TODO: 2019/12/26 店铺分类敬请期待
                return;
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_GOODS.value() == reduceActivityIndexVo.getActivityGoodsScope()) {
                // 指定商品，更新店铺指定id商品es满减活动标识
                Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
                List<Long> relationIds = reduceActivityIndexVo.getGoodsList().stream().map(ReduceGoodsDTO::getRelationId).collect(Collectors.toList());
                inSearchCondition.put("id", relationIds);
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_BRAND.value() == reduceActivityIndexVo.getActivityGoodsScope()) {
                // 指定品牌，更新店铺指定品牌商品es满减活动标识
                Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
                List<Long> relationIds = reduceActivityIndexVo.getGoodsList().stream().map(ReduceGoodsDTO::getRelationId).collect(Collectors.toList());
                inSearchCondition.put("brandId", relationIds);
            }
            esDataUtils.queryData(pageModelDTO, IndexContant.GOODS_INDEX_NAME);
            List<String> jsonRsList = pageModelDTO.getJsonRsList();
            if (CollectionUtils.isNotEmpty(jsonRsList)) {
                for (String jsonRs : jsonRsList) {
                    JSONObject jsonObject = JSONObject.parseObject(jsonRs);
                    goodsIds.add(Long.parseLong(jsonObject.get("id").toString()));
                }
            }
        }
        List<Map<String, Object>> goodsEsUpdateList = new ArrayList<>();
        for (Long goodsId : goodsIds) {
            Map<String, Object> goodsEsUpdate = new HashMap<>();
            goodsEsUpdate.put("id", goodsId);
            goodsEsUpdate.put("reduceFlag", reduceFlag);
            goodsEsUpdateList.add(goodsEsUpdate);
        }
        esDataUtils.updateDataBatch(IndexContant.GOODS_INDEX_NAME, "id", JSONArray.toJSONString(goodsEsUpdateList));
    }

}
