/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.RangConditionDTO;
import com.leimingtech.modules.dto.cart.ActivityRuleDTO;
import com.leimingtech.modules.dto.cart.CartDTO;
import com.leimingtech.modules.dto.coupons.FrontCouponsActivityPageDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.cart.CartEnum;
import com.leimingtech.modules.enums.flashsale.FlashSaleActivityEnum;
import com.leimingtech.modules.enums.group.GroupsEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.service.cart.CartService;
import com.leimingtech.modules.service.coupons.CouponsActivitySearchService;
import com.leimingtech.modules.service.reduce.ReduceActivitySearchService;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.moudle.search.constant.SearchServiceCode;
import com.leimingtech.moudle.search.modules.dto.*;
import com.leimingtech.moudle.search.modules.dto.optimize.*;
import com.leimingtech.moudle.search.modules.service.GoodsSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kuangweiguo·
 * @version 1.0
 * @date 2019/6/29 2:58 PM
 */
@Slf4j
@Service
public class GoodsSearchServiceImpl implements GoodsSearchService {

    private static final MonitorLogger LOGGER = MonitorLoggerFactory.getMonitorLogger(GoodsSearchServiceImpl.class);

    private static final String SPEC_SALE_NUM = "specSaleNum";

    private static final String SPEC_SELL_PRICE = "specSellPrice";


    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private EsDataUtils esDataUtils;
    @Autowired
    private CouponsActivitySearchService couponsActivitySearchService;
    @Autowired
    private ReduceActivitySearchService reduceActivitySearchService;
    @Autowired
    private CartService cartService;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public GoodsSearchVO goodsSerarch(GoodsSearchDTO goodsSearchDTO) {
        try {
            SortOrder sortOrder;
            if (goodsSearchDTO.getSortType() != null && SortOrder.DESC.toString().equals(goodsSearchDTO.getSortType().toLowerCase())) {
                sortOrder = SortOrder.DESC;
            } else {
                sortOrder = SortOrder.ASC;
            }
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            SearchRequest searchRequest = new SearchRequest(ElasticSearchConstant.GOODS_INDEX);

            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

            if (StringUtils.isNotBlank(goodsSearchDTO.getKeyword())) {

                queryBuilder.must(
                        QueryBuilders.boolQuery()
                                //分词后匹配
                                .should(QueryBuilders.termQuery("goodsName", goodsSearchDTO.getKeyword()).boost(6.5f))
                                .should(QueryBuilders.matchPhraseQuery("goodsName", goodsSearchDTO.getKeyword()).slop(4).boost(2.5f))
                                //匹配完整词
                                .should(QueryBuilders.termQuery("brandName", goodsSearchDTO.getKeyword()))
                                //匹配完整词(标签名称)
//                                .should(QueryBuilders.termQuery("labelName", goodsSearchDTO.getKeyword()))
                                //完全匹配
                                .should(QueryBuilders.termQuery("gcName", goodsSearchDTO.getKeyword())));

            }

            if (goodsSearchDTO.getStoreId() != null) {
                QueryBuilder queryBuilderStoreId = QueryBuilders.matchQuery("storeId", goodsSearchDTO.getStoreId());
                queryBuilder.must(queryBuilderStoreId);
            }
            //根据标签id查询商品信息
            if (ArrayUtils.isNotEmpty(goodsSearchDTO.getLabelIds())) {
                // 前端传递为String 需要转化为Long与索引保持一致
                long[] labelIds = (long[]) ConvertUtils.convert(goodsSearchDTO.getLabelIds(), long.class);
                QueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("goodsLabels.labelId", labelIds);
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(termsQueryBuilder);
                NestedQueryBuilder labelSearchDTO = QueryBuilders.nestedQuery("goodsLabels", boolQueryBuilder, ScoreMode.Total);
                queryBuilder.filter(labelSearchDTO);
            }

            // 添加商品分类ID检索
            if (ArrayUtils.isNotEmpty(goodsSearchDTO.getGcIds())) {
                // 前端传递为String 需要转化为Long与索引保持一致
                long[] gcIds = (long[]) ConvertUtils.convert(goodsSearchDTO.getGcIds(), long.class);
                QueryBuilder queryBuilderGcId = QueryBuilders.termsQuery("gcId", gcIds);
                QueryBuilder queryBuilderFirstGcId = QueryBuilders.termsQuery("firstGcId", gcIds);
                QueryBuilder queryBuilderSecondGcId = QueryBuilders.termsQuery("secondGcId", gcIds);
                queryBuilder.filter(QueryBuilders.boolQuery()
                        .should(queryBuilderGcId)
                        .should(queryBuilderFirstGcId)
                        .should(queryBuilderSecondGcId));
            }
            // 添加店铺二级商品分类ID检索
            if (ArrayUtils.isNotEmpty(goodsSearchDTO.getStoreGcIds())) {
                // 前端传递为String 需要转化为Long与索引保持一致
                long[] storeGcIds = (long[]) ConvertUtils.convert(goodsSearchDTO.getStoreGcIds(), long.class);
                QueryBuilder queryBuilderStoreGcId = QueryBuilders.termsQuery("secondStoreGoodsGcId", storeGcIds);
                queryBuilder.filter(queryBuilderStoreGcId);
            }

            // 添加店铺一级商品分类ID检索
            if (ArrayUtils.isNotEmpty(goodsSearchDTO.getStoreFirstGcIds())) {
                // 前端传递为String 需要转化为Long与索引保持一致
                long[] storeFirstGcIds = (long[]) ConvertUtils.convert(goodsSearchDTO.getStoreFirstGcIds(), long.class);
                QueryBuilder queryBuilderStoreFirstGcId = QueryBuilders.termsQuery("firstStoreGoodsGcId", storeFirstGcIds);
                queryBuilder.filter(queryBuilderStoreFirstGcId);
            }

            QueryBuilder queryBuilderGoodsShow = QueryBuilders.termQuery("goodsShow", 1);
            queryBuilder.filter(queryBuilderGoodsShow);

            // 进行品牌查询聚合操作
            AggregationBuilder aggregateBrandName = AggregationBuilders.terms("brandName").field("brandName").size(200);
            //品牌id聚合
            AggregationBuilder aggregateBrandId = AggregationBuilders.terms("brandId").field("brandId").size(200);
            aggregateBrandId.subAggregation(aggregateBrandName);
            //进行分类聚合操作
            AggregationBuilder aggregateGoodGcName = AggregationBuilders.terms("gcName").field("gcName").size(200);
            //分类id聚合
            AggregationBuilder aggregateGoodGcId = AggregationBuilders.terms("gcId").field("gcId").size(200);
            aggregateGoodGcId.subAggregation(aggregateGoodGcName);
            //一级分类聚合
            AggregationBuilder aggregateFirstGcName = AggregationBuilders.terms("firstGcName").field("firstGcName").size(200);
            AggregationBuilder aggregateFirstGcId = AggregationBuilders.terms("firstGcId").field("firstGcId").size(200);
            aggregateFirstGcId.subAggregation(aggregateFirstGcName);
            //二级级分类聚合
            AggregationBuilder aggregateSecondGcName = AggregationBuilders.terms("secondGcName").field("secondGcName").size(200);
            AggregationBuilder aggregateSecondGcId = AggregationBuilders.terms("secondGcId").field("secondGcId").size(200);
            aggregateSecondGcId.subAggregation(aggregateSecondGcName);
            //店铺一级分类聚合
            AggregationBuilder aggregateStoreFirstGcName = AggregationBuilders.terms("firstStoreGoodsGcName").field("firstStoreGoodsGcName").size(200);
            AggregationBuilder aggregateStoreFirstGcId = AggregationBuilders.terms("firstStoreGoodsGcId").field("firstStoreGoodsGcId").size(200);
            aggregateStoreFirstGcId.subAggregation(aggregateStoreFirstGcName);

            //标签聚合 (name自定义字段 path :es存的list名称)
            AggregationBuilder nestedLabel = AggregationBuilders.nested("label", "goodsLabels");
            AggregationBuilder aggregationLabelId = AggregationBuilders.terms("labelId").field("goodsLabels.labelId").size(20);
            AggregationBuilder aggregationLabelName = AggregationBuilders.terms("labelName").field("goodsLabels.labelName.keyword");
            aggregationLabelId.subAggregation(aggregationLabelName);
            nestedLabel.subAggregation(aggregationLabelId);
            //属性 聚合
            AggregationBuilder nestedAgg = AggregationBuilders.nested("agg", "attrValues");
            AggregationBuilder aggregationAttrValue = AggregationBuilders.terms("attrValue").field("attrValues.attrValue.keyword");
            AggregationBuilder aggregationAttrName = AggregationBuilders.terms("attrName").field("attrValues.attrName.keyword");
            AggregationBuilder aggregationAttrId = AggregationBuilders.terms("attributeId").field("attrValues.attributeId").size(20);
            aggregationAttrId.subAggregation(aggregationAttrName);
            aggregationAttrId.subAggregation(aggregationAttrValue);
            nestedAgg.subAggregation(aggregationAttrId);

            searchSourceBuilder
                    .query(queryBuilder)
                    .from(goodsSearchDTO.getPageSize() * (goodsSearchDTO.getPageNo() - 1))
                    .size(goodsSearchDTO.getPageSize())
                    .aggregation(aggregateBrandId)
                    .aggregation(aggregateGoodGcId)
                    .aggregation(aggregateFirstGcId)
                    .aggregation(aggregateSecondGcId)
                    .aggregation(aggregateStoreFirstGcId)
                    .aggregation(nestedLabel)
                    .aggregation(nestedAgg)
                    .highlighter(this.getHighlightBuilder("goodsName"));

            // 如果排序字段为空  默认使用匹配度进行排序
            if (StringUtils.isNotBlank(goodsSearchDTO.getSortField())) {
                searchSourceBuilder.sort(goodsSearchDTO.getSortField(), sortOrder);
            }


            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            //商品信息
            SearchHits hits = searchResponse.getHits();
            long count = hits.getTotalHits().value;


            Terms brandIdList = searchResponse.getAggregations().get("brandId");

            List<BrandVO> brandList = Lists.newArrayList();

            //品牌聚合的Name,id值
            for (Terms.Bucket bt : brandIdList.getBuckets()) {
                Terms terms1 = bt.getAggregations().get("brandName");
                List<Terms.Bucket> buckets1 = (List<Terms.Bucket>) terms1.getBuckets();
                BrandVO brandVO = new BrandVO();
                brandVO.setBrandId(bt.getKeyAsNumber().longValue());
                brandVO.setBrandName(buckets1.get(0).getKeyAsString());
                brandList.add(brandVO);
            }
            // 分类聚合的Name.id值
            Terms gcListAgg = searchResponse.getAggregations().get("gcId");
            List<GoodsClassVO> goodsClassList = Lists.newArrayList();
            for (Terms.Bucket btGc : gcListAgg.getBuckets()) {
                Terms terms1 = btGc.getAggregations().get("gcName");
                List<Terms.Bucket> buckets1 = (List<Terms.Bucket>) terms1.getBuckets();
                GoodsClassVO goodsClassVO = new GoodsClassVO();
                goodsClassVO.setGcId(btGc.getKeyAsNumber().longValue());
                goodsClassVO.setGcName(buckets1.get(0).getKeyAsString());
                goodsClassList.add(goodsClassVO);
            }
            // 分类聚合的Name.id值
            Terms firstGcListAgg = searchResponse.getAggregations().get("firstGcId");
            List<GoodsClassVO> firstGoodsClassList = Lists.newArrayList();
            for (Terms.Bucket btGc : firstGcListAgg.getBuckets()) {
                Terms terms1 = btGc.getAggregations().get("firstGcName");
                List<Terms.Bucket> buckets1 = (List<Terms.Bucket>) terms1.getBuckets();
                GoodsClassVO goodsClassVO = new GoodsClassVO();
                goodsClassVO.setGcId(btGc.getKeyAsNumber().longValue());
                goodsClassVO.setGcName(buckets1.get(0).getKeyAsString());
                firstGoodsClassList.add(goodsClassVO);
            }
            // 分类聚合的Name.id值
            Terms secondGcListAgg = searchResponse.getAggregations().get("secondGcId");
            List<GoodsClassVO> secondGoodsClassList = Lists.newArrayList();
            for (Terms.Bucket btGc : secondGcListAgg.getBuckets()) {
                Terms terms1 = btGc.getAggregations().get("secondGcName");
                List<Terms.Bucket> buckets1 = (List<Terms.Bucket>) terms1.getBuckets();
                GoodsClassVO goodsClassVO = new GoodsClassVO();
                goodsClassVO.setGcId(btGc.getKeyAsNumber().longValue());
                goodsClassVO.setGcName(buckets1.get(0).getKeyAsString());
                secondGoodsClassList.add(goodsClassVO);
            }
            // 分类聚合的店铺商品分类Name.id值
            Terms storeGcListAgg = searchResponse.getAggregations().get("firstStoreGoodsGcId");
            List<GoodsClassVO> storeGoodsClassList = Lists.newArrayList();
            for (Terms.Bucket btGc : storeGcListAgg.getBuckets()) {
                Terms terms1 = btGc.getAggregations().get("firstStoreGoodsGcName");
                List<Terms.Bucket> buckets1 = (List<Terms.Bucket>) terms1.getBuckets();
                GoodsClassVO storeGoodsClassVO = new GoodsClassVO();
                storeGoodsClassVO.setGcId(btGc.getKeyAsNumber().longValue());
                storeGoodsClassVO.setGcName(buckets1.get(0).getKeyAsString());
                storeGoodsClassList.add(storeGoodsClassVO);
            }

            //标签聚合 name ,id
            Nested labelAgg = searchResponse.getAggregations().get("label");
            Terms labelListAgg = labelAgg.getAggregations().get("labelId");
            List<GoodsLabelNameVO> goodsLabelVOList = Lists.newArrayList();
            for (Terms.Bucket btLabel : labelListAgg.getBuckets()) {
                Terms terms1 = btLabel.getAggregations().get("labelName");
                List<Terms.Bucket> buckets1 = (List<Terms.Bucket>) terms1.getBuckets();
                GoodsLabelNameVO labelNameVO = new GoodsLabelNameVO();
                labelNameVO.setLabelName(buckets1.get(0).getKeyAsString());
                labelNameVO.setLabelId(btLabel.getKeyAsNumber().longValue());
                goodsLabelVOList.add(labelNameVO);
            }

            // 属性聚合的Name.id值
            Nested agg = searchResponse.getAggregations().get("agg");
            Terms attributeIdListAgg = agg.getAggregations().get("attributeId");
            List<GoodsAttrVO> goodsAttrVOList = Lists.newArrayList();
            for (Terms.Bucket btAttr : attributeIdListAgg.getBuckets()) {
                Terms terms1 = btAttr.getAggregations().get("attrName");
                List<Terms.Bucket> buckets1 = (List<Terms.Bucket>) terms1.getBuckets();
                GoodsAttrVO goodsAttrVO = new GoodsAttrVO();

                goodsAttrVO.setAttrName(buckets1.get(0).getKeyAsString());
                goodsAttrVO.setAttrId(btAttr.getKeyAsNumber().longValue());
                Terms terms2 = btAttr.getAggregations().get("attrValue");
                List<Map<String, String>> attrValueList = new ArrayList<>();
                for (Terms.Bucket btt : terms2.getBuckets()) {
                    Map<String, String> map = new HashMap<>(16);
                    map.put("attrValueName", btt.getKey().toString());
                    attrValueList.add(map);
                }
                goodsAttrVO.setGoodsAttrValueVOList(attrValueList);
                goodsAttrVOList.add(goodsAttrVO);
            }

            GoodsSearchVO goodsSearchVO = new GoodsSearchVO();
            goodsSearchVO.setBrandVOList(brandList);
            goodsSearchVO.setGoodsClassVOList(goodsClassList);
            goodsSearchVO.setFirstGoodsClassVOList(firstGoodsClassList);
            goodsSearchVO.setSecondGoodsClassVOList(secondGoodsClassList);
            goodsSearchVO.setStoreGoodsClassVOList(storeGoodsClassList);
            goodsSearchVO.setGoodsAttrVOList(goodsAttrVOList);
            goodsSearchVO.setGoodsLabelVOList(goodsLabelVOList);
            goodsSearchVO.setTotalCount(count);
            goodsSearchVO.setPageNo(goodsSearchDTO.getPageNo());
            goodsSearchVO.setPageSize(goodsSearchDTO.getPageSize());

            return goodsSearchVO;
        } catch (Exception e) {
            LOGGER.error(SearchServiceCode.GOODS_SEARCH_ERROR.getCode(), SearchServiceCode.GOODS_SEARCH_ERROR.getMessage(), e);
        }

        return null;
    }


    /**
     * 拼接高亮字段
     *
     * @param filedName: 需要高亮
     * @return 高亮Builder
     */
    private HighlightBuilder getHighlightBuilder(String filedName) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field(filedName);
        return highlightBuilder;
    }

    /**
     * 商品筛选条件搜索（只返回商品搜素接口）
     *
     * @param filterDTO: 商品搜索筛选条件
     * @return: 商品搜索结果集
     * @date 2019/7/26 15:01
     * @author lixiang
     **/
    @Override
    public GoodsSearchFilterVO goodsFilterSerarch(GoodsSearchFilterDTO filterDTO) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String memberSource = request.getHeader("memberSource");
        SortOrder sortOrder;
        if (filterDTO.getSortType() != null && SortOrder.DESC.toString().equals(filterDTO.getSortType().toLowerCase())) {
            sortOrder = SortOrder.DESC;
        } else {
            sortOrder = SortOrder.ASC;
        }

        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

            if (StringUtils.isNotBlank(filterDTO.getKeyword())) {

                queryBuilder.must(
                        QueryBuilders.boolQuery()
                                //分词后匹配
                                .should(QueryBuilders.matchQuery("goodsName", filterDTO.getKeyword()).boost(6.5f))
                                //匹配完整词
                                .should(QueryBuilders.termQuery("brandName", filterDTO.getKeyword()))
                                //匹配完整词(标签名称)
                                .should(QueryBuilders.termQuery("labelName", filterDTO.getKeyword()))
                                //完全匹配
                                .should(QueryBuilders.termQuery("gcName", filterDTO.getKeyword())));
            }

            // 添加商品分类ID检索
            if (ArrayUtils.isNotEmpty(filterDTO.getGcIds())) {
                // 前端传递为String 需要转化为Long与索引保持一致
                long[] gcIds = (long[]) ConvertUtils.convert(filterDTO.getGcIds(), long.class);
                QueryBuilder queryBuilderGcId = QueryBuilders.termsQuery("gcId", gcIds);
                QueryBuilder queryBuilderFirstGcId = QueryBuilders.termsQuery("firstGcId", gcIds);
                QueryBuilder queryBuilderSecondGcId = QueryBuilders.termsQuery("secondGcId", gcIds);
                queryBuilder.filter(QueryBuilders.boolQuery()
                        .should(queryBuilderGcId)
                        .should(queryBuilderFirstGcId)
                        .should(queryBuilderSecondGcId));
            }

            if (filterDTO.getStoreId() != null) {
                QueryBuilder queryBuilderStoreId = QueryBuilders.matchQuery("storeId", filterDTO.getStoreId());
                queryBuilder.must(queryBuilderStoreId);
            }

            // 添加店铺二级商品分类ID检索
            if (ArrayUtils.isNotEmpty(filterDTO.getStoreGcIds())) {
                // 前端传递为String 需要转化为Long与索引保持一致
                long[] storeGcIds = (long[]) ConvertUtils.convert(filterDTO.getStoreGcIds(), long.class);
                QueryBuilder queryBuilderStoreGcId = QueryBuilders.termsQuery("secondStoreGoodsGcId", storeGcIds);
                queryBuilder.filter(queryBuilderStoreGcId);
            }

            // 添加店铺一级商品分类ID检索
            if (ArrayUtils.isNotEmpty(filterDTO.getStoreFirstGcIds())) {
                // 前端传递为String 需要转化为Long与索引保持一致
                long[] storeFirstGcIds = (long[]) ConvertUtils.convert(filterDTO.getStoreFirstGcIds(), long.class);
                QueryBuilder queryBuilderStoreFirstGcId = QueryBuilders.termsQuery("firstStoreGoodsGcId", storeFirstGcIds);
                queryBuilder.filter(queryBuilderStoreFirstGcId);
            }


            // 添加商品品牌ID检索
            if (ArrayUtils.isNotEmpty(filterDTO.getBrandIds())) {
                // 前端传递为String 需要转化为Long与索引保持一致
                long[] brandIds = (long[]) ConvertUtils.convert(filterDTO.getBrandIds(), long.class);
                QueryBuilder queryBuilderBrandId = QueryBuilders.termsQuery("brandId", brandIds);
                queryBuilder.filter(queryBuilderBrandId);
            }

            // 商品属性搜素
            if (CollectionUtils.isNotEmpty(filterDTO.getAttrListList())) {
                for (AttrListDTO attrList : filterDTO.getAttrListList()) {
                    // 由于attrValues的type为nested类型（文档内嵌） 检索attrValues下内嵌的文档必须使用如下方式查询
                    if (attrList.getAttrId() != null) {
                        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("attrValues.attributeId", String.valueOf(attrList.getAttrId()));
                        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("attrValues.attrValue.keyword", attrList.getAttrValueList());
                        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(matchQueryBuilder).must(termsQueryBuilder);
                        NestedQueryBuilder attrValues = QueryBuilders.nestedQuery("attrValues", boolQueryBuilder, ScoreMode.Total);
                        queryBuilder.must(attrValues);
                    }

                }
            }

            //添加标签检索条件

            if (ArrayUtils.isNotEmpty(filterDTO.getLabelIds())) {
                // 由于labelSearchDTOS的type为nested类型（文档内嵌） 检索goodsLabelVOList下内嵌的文档必须使用如下方式查询
                TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("goodsLabels.labelId", filterDTO.getLabelIds());
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(termsQueryBuilder);
                NestedQueryBuilder labelSearchDTO = QueryBuilders.nestedQuery("goodsLabels", boolQueryBuilder, ScoreMode.Total);
                queryBuilder.must(labelSearchDTO);
            }

            // 添加价格检索条件
            if (filterDTO.getMinPrice() != null && filterDTO.getMaxPrice() != null) {
                QueryBuilder queryBuilderSpecSellPricee = QueryBuilders.rangeQuery(SPEC_SELL_PRICE).gte(filterDTO.getMinPrice()).lte(filterDTO.getMaxPrice());
                queryBuilder.must(queryBuilderSpecSellPricee);
            } else {
                if (filterDTO.getMinPrice() != null) {
                    QueryBuilder queryBuildergoodsName = QueryBuilders.rangeQuery(SPEC_SELL_PRICE).gte(filterDTO.getMinPrice());
                    queryBuilder.must(queryBuildergoodsName);
                }
                if (filterDTO.getMaxPrice() != null) {
                    QueryBuilder queryBuildergoodsName = QueryBuilders.rangeQuery(SPEC_SELL_PRICE).lte(filterDTO.getMaxPrice());
                    queryBuilder.must(queryBuildergoodsName);
                }
            }

            QueryBuilder queryBuilderGoodsShow = QueryBuilders.termQuery("goodsShow", 1);
            queryBuilder.filter(queryBuilderGoodsShow);


            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


            searchSourceBuilder
                    .query(queryBuilder)
                    .from(filterDTO.getPageSize() * (filterDTO.getPageNo() - 1))
                    .size(filterDTO.getPageSize())
                    .highlighter(this.getHighlightBuilder("goodsName"));

            // 如果排序字段为空  默认使用匹配度进行排序
            if (StringUtils.isNotBlank(filterDTO.getSortField())) {
                searchSourceBuilder.sort(filterDTO.getSortField(), sortOrder);
            }
            SearchRequest searchRequest = new SearchRequest(ElasticSearchConstant.GOODS_INDEX);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


            //商品信息
            List<GoodsVO> goodsList = new ArrayList<>();
            SearchHits hits = searchResponse.getHits();
            long count = hits.getTotalHits().value;

            for (SearchHit hit : hits) {
                String goods = hit.getSourceAsString();
                // 处理高亮字段
                GoodsVO goodsVO = JSONObject.parseObject(goods, GoodsVO.class);
                if (memberSource != null && "0".equals(memberSource)) {
                    //0:pc,1:h5,2:android,3:ios,4:微信,5:小程序
                    //PC高亮展示搜索关键字
                    Optional.ofNullable(hit.getHighlightFields().get("goodsName")).ifPresent(t -> {
                        goodsVO.setGoodsName(t.fragments()[0].toString());
                    });
                }
                goodsList.add(goodsVO);
            }

            GoodsSearchFilterVO goodsSearchFilterVO = new GoodsSearchFilterVO();
            goodsSearchFilterVO.setGoodsVOList(goodsList);
            goodsSearchFilterVO.setTotalCount(count);
            goodsSearchFilterVO.setPageNo(filterDTO.getPageNo());
            goodsSearchFilterVO.setPageSize(filterDTO.getPageSize());

            return goodsSearchFilterVO;

        } catch (IOException e) {
            LOGGER.error(SearchServiceCode.GOODS_FILTER_SEARCH_ERROR.getCode(),
                    SearchServiceCode.GOODS_FILTER_SEARCH_ERROR.getMessage(), e);
        }

        return null;
    }

    /**
     * 功能描述:
     * 〈优惠券去凑单商品列表页面〉
     *
     * @param goCollectBillsDTO 去凑单实体
     * @param memberId          会员id
     * @author : 刘远杰
     */
    @Override
    public CouponsGoodsPageDTO collectBillsGoods(GoCollectBillsDTO goCollectBillsDTO, Long memberId, HttpServletRequest request) {
        // 获得所有活动下商品
        List<Long> goodsIds = couponsActivitySearchService.getAllGoodsByActivityId(goCollectBillsDTO.getActivityId());

        // 获得活动商品分页数据
        CouponsGoodsPageDTO couponsGoodsPageDTO = new CouponsGoodsPageDTO();
        List<FrontCouponsGoodsVO> frontCouponsGoodsVOList = getGoodsActivityPageByIds(goCollectBillsDTO, goodsIds);
        couponsGoodsPageDTO.setTotalCount(goCollectBillsDTO.getTotalNum());
        couponsGoodsPageDTO.setFrontCouponsGoodsVOList(frontCouponsGoodsVOList);

        // 计算购物车活动商品总金额
        BigDecimal goodsAmount = countActivityGoodsAmint(goCollectBillsDTO, memberId, request, goodsIds);
        couponsGoodsPageDTO.setGoodsAmount(goodsAmount);

        // 获取活动信息
        String activityJson = esDataUtils.getDateById(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, goCollectBillsDTO.getActivityId().toString());
        FrontCouponsActivityPageDTO frontCouponsActivityPageDTO = JSONObject.parseObject(activityJson, FrontCouponsActivityPageDTO.class);

        couponsGoodsPageDTO.setActivityId(goCollectBillsDTO.getActivityId());
        couponsGoodsPageDTO.setFaceValue(frontCouponsActivityPageDTO.getFaceValue());
        couponsGoodsPageDTO.setLimitAmount(frontCouponsActivityPageDTO.getLimitAmount());
        if (couponsGoodsPageDTO.getGoodsAmount().compareTo(couponsGoodsPageDTO.getLimitAmount()) >= 0) {
            // 凑单金额大于等于门槛
            couponsGoodsPageDTO.setLackAmount(BigDecimal.ZERO);
        } else {
            // 凑单金额小于等于门槛
            couponsGoodsPageDTO.setLackAmount(couponsGoodsPageDTO.getLimitAmount().subtract(couponsGoodsPageDTO.getGoodsAmount()));
        }
        return couponsGoodsPageDTO;
    }

    /**
     * 功能描述:
     * 〈满减活动去凑单商品列表页面〉
     *
     * @param goCollectBillsDTO 去凑单实体
     * @param memberId          会员id
     * @author : 刘远杰
     */
    @Override
    public ReduceGoodsPageDTO collectBillsReduceGoods(GoCollectBillsDTO goCollectBillsDTO, Long memberId, HttpServletRequest request) {
        // 获得活动下所有商品
        List<Long> goodsIds = reduceActivitySearchService.getAllGoodsByActivityId(goCollectBillsDTO.getActivityId());

        // 获得活动商品分页数据
        // 获得活动商品分页数据 todo 这里慢
        ReduceGoodsPageDTO reduceGoodsPageDTO = new ReduceGoodsPageDTO();
        List<FrontCouponsGoodsVO> frontCouponsGoodsVOList = getGoodsActivityPageByIds(goCollectBillsDTO, goodsIds);
        reduceGoodsPageDTO.setTotalCount(goCollectBillsDTO.getTotalNum());
        reduceGoodsPageDTO.setFrontCouponsGoodsVOList(frontCouponsGoodsVOList);

        // 计算购物车活动商品总金额 todo 这里慢
        BigDecimal goodsAmount = countActivityGoodsAmint(goCollectBillsDTO, memberId, request, goodsIds);
        reduceGoodsPageDTO.setGoodsAmount(goodsAmount);

        // 获取活动规则信息
        ReduceActivityIndexDTO reduceActivityIndexDTO = reduceActivitySearchService.activityDetail(goCollectBillsDTO.getActivityId());
        List<ActivityRuleDTO> activityRuleDTOList = com.leimingtech.commons.tools.utils.ConvertUtils.sourceToTarget(reduceActivityIndexDTO.getRuleList(), ActivityRuleDTO.class);
        reduceGoodsPageDTO.setRuleDTOList(activityRuleDTOList);

        reduceGoodsPageDTO.setActivityId(goCollectBillsDTO.getActivityId());

        // 遍历活动规则，计算凑单金额
        if (CollectionUtils.isNotEmpty(activityRuleDTOList)) {
            if (activityRuleDTOList.size() == 1) {
                // 单规则满减活动显示描述
                ActivityRuleDTO activityRuleDTO = activityRuleDTOList.get(0);
                if (goodsAmount.compareTo(BigDecimal.ZERO) == 0) {
                    // 无勾选商品
                    reduceGoodsPageDTO.setNextLimitAmount(activityRuleDTO.getLimitAmount());
                    reduceGoodsPageDTO.setNextReduceAmount(activityRuleDTO.getReduceAmount());
                    reduceGoodsPageDTO.setLackAmount(activityRuleDTO.getLimitAmount());
                    reduceGoodsPageDTO.setOperationType(0);
                } else if (ReduceActivityEnum.RULE_TYPE_NORMAL.value() == activityRuleDTO.getRuleType() || ReduceActivityEnum.RULE_TYPE_LADDER.value() == activityRuleDTO.getRuleType()) {
                    // 普通满减活动，单规则阶梯满减
                    if (goodsAmount.compareTo(activityRuleDTO.getLimitAmount()) >= 0) {
                        // 满足活动条件
                        reduceGoodsPageDTO.setLimitAmount(activityRuleDTO.getLimitAmount());
                        reduceGoodsPageDTO.setReduceAmount(activityRuleDTO.getReduceAmount());
                        reduceGoodsPageDTO.setLackAmount(BigDecimal.ZERO);
                        reduceGoodsPageDTO.setOperationType(2);
                    } else {
                        // 不满足活动条件
                        reduceGoodsPageDTO.setNextLimitAmount(activityRuleDTO.getLimitAmount());
                        reduceGoodsPageDTO.setNextReduceAmount(activityRuleDTO.getReduceAmount());
                        reduceGoodsPageDTO.setLackAmount(activityRuleDTO.getLimitAmount());
                        reduceGoodsPageDTO.setOperationType(0);
                    }
                } else if (ReduceActivityEnum.RULE_TYPE_AVG.value() == activityRuleDTO.getRuleType()) {
                    // 每满减
                    if (goodsAmount.compareTo(activityRuleDTO.getLimitAmount()) >= 0) {

                        // 满足活动条件,计算每满减次数
                        BigDecimal avgLevel = goodsAmount.divide(activityRuleDTO.getLimitAmount(), 0, BigDecimal.ROUND_DOWN);
                        // 满减门槛条件 = 每满减门槛条件 * 每满减次数
                        BigDecimal limitAmount = activityRuleDTO.getLimitAmount().multiply(avgLevel);
                        // 满减优惠金额 = 每满减优惠金额 * 每满减次数
                        BigDecimal reduceAmount = activityRuleDTO.getReduceAmount().multiply(avgLevel);
                        BigDecimal lackAmount = limitAmount.add(activityRuleDTO.getLimitAmount()).subtract(goodsAmount);

                        reduceGoodsPageDTO.setLimitAmount(limitAmount);
                        reduceGoodsPageDTO.setReduceAmount(reduceAmount);
                        reduceGoodsPageDTO.setNextLimitAmount(limitAmount.add(activityRuleDTO.getLimitAmount()));
                        reduceGoodsPageDTO.setNextReduceAmount(reduceAmount.add(activityRuleDTO.getReduceAmount()));
                        reduceGoodsPageDTO.setLackAmount(lackAmount);
                        reduceGoodsPageDTO.setOperationType(1);
                    } else {
                        // 不满足活动条件
                        reduceGoodsPageDTO.setNextLimitAmount(activityRuleDTO.getLimitAmount());
                        reduceGoodsPageDTO.setNextReduceAmount(activityRuleDTO.getReduceAmount());
                        reduceGoodsPageDTO.setLackAmount(activityRuleDTO.getLimitAmount());
                        reduceGoodsPageDTO.setOperationType(0);
                    }
                }
            } else {
                // 多规则满减活动显示描述
                ActivityRuleDTO firstActivityRuleDTO = activityRuleDTOList.get(0);
                ActivityRuleDTO lastActivityRuleDTO = activityRuleDTOList.get(activityRuleDTOList.size() - 1);
                if (goodsAmount.compareTo(BigDecimal.ZERO) == 0) {
                    // 无勾选商品
                    reduceGoodsPageDTO.setNextLimitAmount(firstActivityRuleDTO.getLimitAmount());
                    reduceGoodsPageDTO.setNextReduceAmount(firstActivityRuleDTO.getReduceAmount());
                    reduceGoodsPageDTO.setLackAmount(firstActivityRuleDTO.getLimitAmount());
                    reduceGoodsPageDTO.setOperationType(0);
                } else if (goodsAmount.compareTo(firstActivityRuleDTO.getLimitAmount()) < 0) {
                    // 未满足最低满减
                    reduceGoodsPageDTO.setNextLimitAmount(firstActivityRuleDTO.getLimitAmount());
                    reduceGoodsPageDTO.setNextReduceAmount(firstActivityRuleDTO.getReduceAmount());
                    reduceGoodsPageDTO.setLackAmount(firstActivityRuleDTO.getLimitAmount());
                    reduceGoodsPageDTO.setOperationType(0);
                } else if (goodsAmount.compareTo(lastActivityRuleDTO.getLimitAmount()) >= 0) {
                    // 满足最高满减
                    reduceGoodsPageDTO.setLimitAmount(lastActivityRuleDTO.getLimitAmount());
                    reduceGoodsPageDTO.setReduceAmount(lastActivityRuleDTO.getReduceAmount());
                    reduceGoodsPageDTO.setLackAmount(BigDecimal.ZERO);
                    reduceGoodsPageDTO.setOperationType(2);
                } else {
                    for (int i = 0; i < activityRuleDTOList.size(); i++) {
                        if (goodsAmount.compareTo(activityRuleDTOList.get(i).getLimitAmount()) >= 0 && goodsAmount.compareTo(activityRuleDTOList.get(i + 1).getLimitAmount()) < 0) {
                            // 满足当前满减
                            BigDecimal lackAmount = activityRuleDTOList.get(i + 1).getLimitAmount().subtract(goodsAmount);
                            reduceGoodsPageDTO.setLimitAmount(activityRuleDTOList.get(i).getLimitAmount());
                            reduceGoodsPageDTO.setReduceAmount(activityRuleDTOList.get(i).getReduceAmount());
                            reduceGoodsPageDTO.setNextLimitAmount(activityRuleDTOList.get(i + 1).getLimitAmount());
                            reduceGoodsPageDTO.setNextReduceAmount(activityRuleDTOList.get(i + 1).getReduceAmount());
                            reduceGoodsPageDTO.setLackAmount(lackAmount);
                            reduceGoodsPageDTO.setOperationType(1);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return reduceGoodsPageDTO;
    }

    /**
     * 功能描述：
     * <凑单商品总数>
     *
     * @param goodsIds 商品ids
     * @return
     * @date 2020/1/9 10:16
     * @author 刘远杰
     **/
//    private Long getGoodsActivityPageTotal(List<Long> goodsIds) {
//        Long totalCount = 0L;
//        // 查询所有上架有库存规格
//        PageModelDTO pageModelDTO = new PageModelDTO();
//        pageModelDTO.setIsPage(false);
//        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
//        inSearchCondition.put("goodsId", goodsIds);
//        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
//        // 上架
//        equalsSearchCondition.put("specShow", 1);
//        // 商品库存大于0
//        Map<String, RangConditionDTO> rangConditionMap = pageModelDTO.getRangConditionMap();
//        RangConditionDTO rangConditionDTO = new RangConditionDTO();
//        rangConditionDTO.setBeginValue("1");
//        rangConditionMap.put("specStorage", rangConditionDTO);
//        pageModelDTO.setCollapseField("goodsId");
//        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);
//        if (CollectionUtils.isNotEmpty(pageModelDTO.getJsonRsList())) {
//            // 存在凑单商品
//            totalCount = Integer.valueOf(pageModelDTO.getJsonRsList().size()).longValue();
//        }
//        return totalCount;
//    }

    /**
     * 根据商品id获取活动商品分页集合
     *
     * @param goCollectBillsDTO 凑单参数
     * @param goodsIds          商品spu ids
     * @return
     * @date
     * @author 刘远杰
     **/
    private List<FrontCouponsGoodsVO> getGoodsActivityPageByIds(GoCollectBillsDTO goCollectBillsDTO, List<Long> goodsIds) {
        goCollectBillsDTO.setTotalNum(0L);

        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);

        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
        inSearchCondition.put("goodsId", goodsIds);

        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        sortFileds.put("mainFlag", "desc");
        sortFileds.put("id", "asc");

        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        // 上架
        equalsSearchCondition.put("specShow", 1);
        equalsSearchCondition.put("delFlag", 0);
        // 商品库存大于0
        Map<String, RangConditionDTO> rangConditionMap = pageModelDTO.getRangConditionMap();
        RangConditionDTO rangConditionDTO = new RangConditionDTO();
        rangConditionDTO.setBeginValue("1");
        rangConditionMap.put("specStorage", rangConditionDTO);

        // 根据goodsId折叠
        pageModelDTO.setCollapseField("goodsId");

        // 查询 上架，库存大于0的商品规格
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);
        List<String> jsonRsList = pageModelDTO.getJsonRsList();
        List<FrontCouponsGoodsVO> frontCouponsGoodsVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(jsonRsList)) {
            goCollectBillsDTO.setTotalNum(Long.parseLong(String.valueOf(jsonRsList.size())));
            for (String json : jsonRsList) {
                FrontCouponsGoodsVO frontCouponsGoodsVO = JSONObject.parseObject(json, FrontCouponsGoodsVO.class);
                // 查询商品评价销量
                String esData = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, frontCouponsGoodsVO.getGoodsId().toString());
                JSONObject goodsDetail = JSONObject.parseObject(esData);
                Object goodsSaleNum = goodsDetail.get("goodsSaleNum");
                if (goodsDetail != null && goodsSaleNum != null) {
                    frontCouponsGoodsVO.setSpecSaleNum((Integer) goodsSaleNum);
                } else {
                    frontCouponsGoodsVO.setSpecSaleNum(0);
                }

                if (goodsDetail != null && goodsDetail.get("evaluateCount") != null) {
                    frontCouponsGoodsVO.getGoodsVO().setEvaluateCount((Integer) goodsDetail.get("evaluateCount"));
                }
                frontCouponsGoodsVOList.add(frontCouponsGoodsVO);
            }

            if (StringUtils.equalsIgnoreCase(goCollectBillsDTO.getSortType(), Constant.DESC)) {
                if (StringUtils.equals(goCollectBillsDTO.getSortField(), SPEC_SALE_NUM)) {
                    // 按销量降序排序
                    frontCouponsGoodsVOList.sort(Comparator.comparing(FrontCouponsGoodsVO::getSpecSaleNum).reversed());
                } else if (StringUtils.equals(goCollectBillsDTO.getSortField(), SPEC_SELL_PRICE)) {
                    // 按价格降序排序
                    frontCouponsGoodsVOList.sort(Comparator.comparing(FrontCouponsGoodsVO::getSpecSellPrice).reversed());
                }
            } else {
                if (StringUtils.equals(goCollectBillsDTO.getSortField(), SPEC_SALE_NUM)) {
                    // 按销量正序排序
                    frontCouponsGoodsVOList.sort(Comparator.comparing(FrontCouponsGoodsVO::getSpecSaleNum));
                } else if (StringUtils.equals(goCollectBillsDTO.getSortField(), SPEC_SELL_PRICE)) {
                    // 按价格正序排序
                    frontCouponsGoodsVOList.sort(Comparator.comparing(FrontCouponsGoodsVO::getSpecSellPrice));
                }
            }
        }

        // 分页
        Integer pageNo = goCollectBillsDTO.getPageNo();
        Integer pageSize = goCollectBillsDTO.getPageSize();
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = fromIndex + pageSize;

        if (pageSize <= 0 || pageNo <= 0 || frontCouponsGoodsVOList.size() - 1 < fromIndex) {
            // 查询记录小于0条  查询起始索引超过最后一条索引
            return Collections.emptyList();
        } else if (frontCouponsGoodsVOList.size() - 1 < toIndex && frontCouponsGoodsVOList.size() - 1 >= fromIndex) {
            // 查询截止索引超过最后一条记录的索引  起始索引不超过最后一条索引
            toIndex = frontCouponsGoodsVOList.size();
        }
        return frontCouponsGoodsVOList.subList(fromIndex, toIndex);
    }

    /**
     * 功能描述:
     * 〈获得购物车活动商品价格〉
     *
     * @param memberId 会员id
     * @param goodsIds 活动商品集合
     * @author : 刘远杰
     */
    private BigDecimal getCartActivityGoodsAmount(Long memberId, List<Long> goodsIds, HttpServletRequest request) {
        BigDecimal goodsAmount = BigDecimal.ZERO;
        List<CartDTO> cartDTOList = new ArrayList<>();
        // 获取购物车勾选活动商品金额
        if (memberId != null) {
            cartDTOList = cartService.getList(memberId);
        } else {
            String redisKey = request.getHeader("redisKey");
            if (StringUtils.isNotBlank(redisKey)) {
                String frontCartKey = RedisKeys.getFrontCartKey(redisKey);
                Map<String, Object> map = redisUtils.hGetAll(frontCartKey);
                cartDTOList = new ArrayList(map.values());
            }
        }
        if (CollectionUtils.isNotEmpty(cartDTOList)) {
            for (CartDTO cartDTO : cartDTOList) {
                // 获得勾选商品
                if (CartEnum.IS_SELECT_YES.value() == cartDTO.getIsSelect()) {
                    for (Long goodsId : goodsIds) {
                        // 获得活动商品
                        if (cartDTO.getGoodsId().equals(goodsId)) {
                            goodsAmount = goodsAmount.add(cartDTO.getSpecSellPrice().multiply(new BigDecimal(cartDTO.getGoodsNum())));
                        }
                    }
                }
            }
        }
        return goodsAmount;
    }

    /**
     * 计算购物车活动商品总金额
     *
     * @param goCollectBillsDTO 凑单实体
     * @param memberId          会员id
     * @param request
     * @param goodsIds          商品ids
     * @return
     * @date
     * @author 刘远杰
     **/
    private BigDecimal countActivityGoodsAmint(GoCollectBillsDTO goCollectBillsDTO, Long memberId, HttpServletRequest request, List<Long> goodsIds) {
        BigDecimal goodsAmount = BigDecimal.ZERO;
        // 获取商品总金额
        if (goCollectBillsDTO.getCollectBillType() == 0) {
            // 立即购买去结算页凑单
            // 1.清空购物车勾选
            cartService.updateIsSelect(memberId, null, CartEnum.IS_SELECT_NO.value());
            // 2.获取立即购买结算商品金额
            goodsAmount = goCollectBillsDTO.getBeforeCollectAmount();
        } else if (goCollectBillsDTO.getCollectBillType() == 1) {
            // 立即购买结算页勾选商品
            goodsAmount = getCartActivityGoodsAmount(memberId, goodsIds, request);
            // 立即购买结算价格
            goodsAmount = goodsAmount.add(goCollectBillsDTO.getBeforeCollectAmount());
        } else {
            // 其他方式结算
            // 获取购物车勾选活动商品金额
            if (memberId != null) {
                goodsAmount = getCartActivityGoodsAmount(memberId, goodsIds, request);
            } else {
                goodsAmount = getCartActivityGoodsAmount(null, goodsIds, request);
            }
        }
        return goodsAmount;
    }

//    @Override
//    public void findSkuAll(Long specId) throws IOException {
//        SpecGoodsDetailVO specGoodsDetailVO = new SpecGoodsDetailVO();
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        SearchRequest searchRequest = new SearchRequest(ElasticSearchConstant.GOODS_SPEC_INDEX);
//
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        QueryBuilder queryBuilderGoodsName = QueryBuilders.matchQuery("id", specId);
//        queryBuilder.must(queryBuilderGoodsName);
//
//        // 规格id聚合
//        AggregationBuilder aggregationSpecId = AggregationBuilders.terms("goodsSpecAttrValueVOListSpecAttrId").field("goodsVO.goodsSpecAttrVOList.attrSpecId").size(100);
//
//        searchSourceBuilder
//                .query(queryBuilder)
//                .aggregation(aggregationSpecId);
//        searchSourceBuilder.from(0);
//        searchSourceBuilder.size(100);
//
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//
//        SearchHits hits = searchResponse.getHits();
//        List aggSpecId = new ArrayList<>();
//        Terms goodsSpecAttrValueSpecAttrIdVOList = searchResponse.getAggregations().get("goodsSpecAttrValueVOListSpecAttrId");
//        for (Terms.Bucket bucket : goodsSpecAttrValueSpecAttrIdVOList.getBuckets()) {
//            aggSpecId.add(bucket.getKeyAsString());
//        }
//        GoodsDetailsVO goodsDetailsVO = null;
//        List<GoodsSpecPicVO> specGoodsPic = null;
//        List<SpecGoodsPicVO> goodsPic = null;
//        SpecGoodsVO specGoodsVO = null;
//        List<SpecGoodsAttrVO> specGoodsAttrVO = null;
//        List<GoodsSpecAttrVO> goodsSpecAttrVO = null;
//        List<GoodsSpecAttr> goodsSpecAttrList = new ArrayList<>();
//        for (SearchHit hit : hits) {
//            String goodsDetails = hit.getSourceAsString();
//            goodsDetailsVO = JSONObject.parseObject(goodsDetails, GoodsDetailsVO.class);
//
//            // 产品确定从商品列表进入商品详情显示SPU价格、规格切换显示SKU的价格
//            //图片
//            specGoodsPic = goodsDetailsVO.getSpecAttrValuePicList();
//            // 商品图片
//            goodsPic = goodsDetailsVO.getGoodsPicList();
//            //商品信息
//            specGoodsVO = goodsDetailsVO.getGoodsVO();
//            //商品属性
//            specGoodsAttrVO = specGoodsVO.getGoodsAttrVOList();
//            //商品规格属性名集合
//            goodsSpecAttrVO = specGoodsVO.getGoodsSpecAttrVOList();
//            List<SpecAttrValueRefVO> specAttrValueRefVO = specGoodsVO.getSpecAttrValueRefVO();
//            List<Long> collect2 = specAttrValueRefVO.stream().filter(t -> t.getDelFlag() == 0).map(SpecAttrValueRefVO::getSpecId).collect(Collectors.toList());
//
//            Map<Long, List<GoodsSpecAttrVO>> collect1 = goodsSpecAttrVO.stream().filter(t -> collect2.contains(t.getAttrSpecId())).collect(Collectors.groupingBy(GoodsSpecAttrVO::getAttrSpecId));
//
//            Map<String, Object> map = new HashMap<>(10);
//
//            collect1.forEach((k, v) -> {
//                StringBuilder sb = new StringBuilder();
//                v.forEach(l -> {
//                    Long id = l.getGoodsSpecAttrValueVOList().get(0).getId();
//                    sb.append(id);
//                    sb.append("-");
//                });
//                // 查询规格信息
//
//                map.put(StringUtils.removeEnd(sb.toString(), "-"), null);
//
//            });
//            System.out.println(map);
//        }
//    }


    /**
     * 商品详情页接口（优化版本）
     *
     * @param specId:  规格ID
     * @param goodsId: 商品ID
     * @return 商品详情页数据
     * @date 2020/5/19 10:26
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public OptimizeSpecGoodsDetailVO goodsSerarchSpecOptimize(Long specId, Long goodsId) {

        SearchResponse searchResponse;
        try {
            searchResponse = packSearchResponse(specId, goodsId);
        } catch (IOException e) {
            throw new ServiceException(SearchServiceCode.GOODS_SPEC_ID_SEARCH_ERROR);
        }

        SearchHits hits = searchResponse.getHits();
        GoodsDetailsVO goodsDetailsVO = null;
        SpecGoodsVO specGoodsVO = null;

        // 创建需要返回的对象
        OptimizeSpecGoodsDetailVO optimizeSpecGoodsDetailVO = new OptimizeSpecGoodsDetailVO();
        OptimizeGoodsDetailsVO optimizeGoodsDetailsVO = new OptimizeGoodsDetailsVO();
        OptimizeSpecGoodsVO optimizeSpecGoodsVO = new OptimizeSpecGoodsVO();

        if (hits.getHits().length < 1) {
            throw new ServiceException(SearchServiceCode.GOODS_SPEC_ID_SEARCH_ERROR);
        }

        // 获取结果集JSON
        goodsDetailsVO = JSONObject.parseObject(hits.getAt(0).getSourceAsString(), GoodsDetailsVO.class);

        BeanCopier.create(GoodsDetailsVO.class, OptimizeGoodsDetailsVO.class, false)
                .copy(goodsDetailsVO, optimizeGoodsDetailsVO, null);

        //
        PageModelDTO pageModelDTO = new PageModelDTO();
        Map<String, Object> condition = pageModelDTO.getEqualsSearchCondition();
        condition.put("goodsId", goodsDetailsVO.getGoodsId());
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);
        List<String> rsList = pageModelDTO.getJsonRsList();
        List<GoodsDetailsVO> goodsDetailsVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rsList)) {
            goodsDetailsVOList = JSONArray.parseArray(rsList.toString(), GoodsDetailsVO.class);
        }

        // 规格图片信息
        List<GoodsSpecPicVO> specGoodsPic = goodsDetailsVO.getSpecAttrValuePicList();
        // 商品图片
        List<SpecGoodsPicVO> goodsPic = goodsDetailsVO.getGoodsPicList();
        // 商品信息
        specGoodsVO = goodsDetailsVO.getGoodsVO();
        BeanCopier.create(SpecGoodsVO.class, OptimizeSpecGoodsVO.class, false)
                .copy(specGoodsVO, optimizeSpecGoodsVO, null);
        // 商品规格属性名集合
        List<GoodsSpecAttrVO> goodsSpecAttrVO = specGoodsVO.getGoodsSpecAttrVOList();
        // 获取规格属性值关联集合
        List<SpecAttrValueRefVO> specAttrValueRefVOList = specGoodsVO.getSpecAttrValueRefVO();

        Map<Long, SpecAttrMsgVO> specAttrMap = Maps.newLinkedHashMap();

        // 获取商品所有规格属性值  颜色、尺寸等
        List<GoodsSpecAttrValueVO> allGoodsSpecAttrValueList = goodsSpecAttrVO.stream().map(t -> t.getGoodsSpecAttrValueVOList().get(0)).distinct().collect(Collectors.toList());

        //根据规格ID去重
        List<GoodsSpecAttrVO> goodsSpecNameList = goodsSpecAttrVO.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GoodsSpecAttrVO::getId))), ArrayList::new)
        );
        List<Long> attrvalueId = new ArrayList<>();
        List<GoodsDetailsVO> finalGoodsDetailsVO = goodsDetailsVOList;

        goodsSpecNameList.forEach(goodsSpec -> {

            // 循环规格数据 填充数据 商品规格属性集合，key规格属性id，value规格属性值集合
            SpecAttrMsgVO specAttrMsgVO = new SpecAttrMsgVO();
            specAttrMsgVO.setSpecAttrName(goodsSpec.getSpecAttrName());
            List<SpecAttrValueVO> specAttrValueVOList = new ArrayList<>();
            specAttrValueRefVOList.forEach(specAttrValueRefVO -> {
                if (specAttrValueRefVO.getSpecAttrId().equals(goodsSpec.getId()) && specAttrValueRefVO.getDelFlag() == 0) {
                    if (!attrvalueId.contains(specAttrValueRefVO.getSpecAttrValueId())) {
                        SpecAttrValueVO specAttrValueVO = new SpecAttrValueVO();
                        specAttrValueVO.setAttrValueId(specAttrValueRefVO.getSpecAttrValueId());

                        allGoodsSpecAttrValueList.forEach(l -> {
                            if (l.getId().equals(specAttrValueRefVO.getSpecAttrValueId())) {
                                specAttrValueVO.setAttrValueName(l.getSpecAttrValue());
                            }
                        });


                        specAttrValueVO.setIsMain(specAttrValueRefVO.getIsMain());

                        // 选择规格商品库存、活动库存处理
                        for (GoodsDetailsVO detailsVO : finalGoodsDetailsVO) {
                            //相同的规格拼接属性
                            if (specAttrValueRefVO.getSpecId().equals(detailsVO.getId()) && specAttrValueRefVO.getIsMain() == 1) {
                                List<GoodsSpecPicVO> attrValuePicList = detailsVO.getSpecAttrValuePicList();
                                attrValuePicList.forEach(pic -> {
                                    if (pic.getIsMainPicture() == 1) {
                                        specAttrValueVO.setPictureUrl(pic.getPictureUrl());
                                    }
                                });
                            }
                        }
                        specAttrValueVOList.add(specAttrValueVO);
                        attrvalueId.add(specAttrValueRefVO.getSpecAttrValueId());
                    }
                }
            });
            specAttrMsgVO.setSpecAttrValueVOList(specAttrValueVOList);
            specAttrMap.put(goodsSpec.getId(), specAttrMsgVO);
        });

        optimizeSpecGoodsDetailVO.setSpecAttrMap(specAttrMap);

        Map<String, OptimizeSpecVO> goodsSpecList = Maps.newLinkedHashMap();

        Set<Long> collect2 = specAttrValueRefVOList.stream().filter(t -> t.getDelFlag() == 0).map(SpecAttrValueRefVO::getSpecId).collect(Collectors.toSet());

        Map<Long, List<GoodsSpecAttrVO>> collect1 = goodsSpecAttrVO.stream().filter(t -> collect2.contains(t.getAttrSpecId())).collect(Collectors.groupingBy(GoodsSpecAttrVO::getAttrSpecId));

        collect1.forEach((k, v) -> {
            StringBuilder sb = new StringBuilder();
            OptimizeSpecVO optimizeSpecVO = new OptimizeSpecVO();
            optimizeSpecVO.setSpecId(v.get(0).getAttrSpecId());
            for (SpecAttrValueRefVO specAttrValueRefVO : specAttrValueRefVOList) {
                if (specAttrValueRefVO.getSpecId().equals(optimizeSpecVO.getSpecId())) {
                    optimizeSpecVO.setSpecShow(specAttrValueRefVO.getSpecShow());
                    optimizeSpecVO.setDelFlag(specAttrValueRefVO.getDelFlag());
                    optimizeSpecVO.setSpecStorage(specAttrValueRefVO.getSpecStorage());
                    // 选择规格商品库存、活动库存处理
                    for (GoodsDetailsVO detailsVO : finalGoodsDetailsVO) {
                        if (detailsVO.getId().equals(optimizeSpecVO.getSpecId())) {
                            //相同的规格拼接属性
                            if (CollectionUtils.isNotEmpty(detailsVO.getSpecActivityList())) {
                                optimizeSpecVO.setSpecStorage(detailsVO.getSpecActivityList().get(0).getActivitySurplusStorage());
                                optimizeSpecVO.setActivityType(detailsVO.getSpecActivityList().get(0).getActivityType());
                                optimizeSpecVO.setActivityState(detailsVO.getSpecActivityList().get(0).getActivityState());
                                if (detailsVO.getId().equals(optimizeGoodsDetailsVO.getId())) {
                                    optimizeGoodsDetailsVO.setSpecStorage(detailsVO.getSpecActivityList().get(0).getActivitySurplusStorage());
                                }
                            }
                        }
                    }
                    break;
                }
            }
            v.forEach(t -> {
                GoodsSpecAttrValueVO goodsSpecAttrValueVO = t.getGoodsSpecAttrValueVOList().get(0);
                sb.append(goodsSpecAttrValueVO.getId()).append("-");
            });
            goodsSpecList.put(StringUtils.removeEnd(sb.toString(), "-"), optimizeSpecVO);
        });
        optimizeSpecGoodsVO.setGoodsSpecList(goodsSpecList);
        optimizeGoodsDetailsVO.setSpecGoodsVO(optimizeSpecGoodsVO);
        optimizeSpecGoodsDetailVO.setGoodsDetailsVO(optimizeGoodsDetailsVO);

        // TODO lixiang 店铺禁用应该直接禁用店铺下面商品
        // 查询店铺状态
        PageModelDTO pageModel = new PageModelDTO();
        Map<String, Object> equalsSearchCondition = pageModel.getEqualsSearchCondition();
        equalsSearchCondition.put("id", specGoodsVO.getStoreId());
        String[] strings = new String[1];
        strings[0] = "isEnable";
        pageModel.setFetchSourceIncludes(strings);
        pageModel.setIsPage(false);
        esDataUtils.queryData(pageModel, ElasticSearchConstant.STORE_INDEX);
        List<String> jsonRsList = pageModel.getJsonRsList();
        List<StoreSearchVO> collect = jsonRsList.stream().map(p -> JSONObject.parseObject(p, StoreSearchVO.class)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(jsonRsList)) {
            specGoodsVO.setStoreIsEnable(Integer.valueOf(collect.get(0).getIsEnable()));
        }
        //查询当前商品总销量
        String esData = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, optimizeSpecGoodsDetailVO.getGoodsDetailsVO().getGoodsId().toString());
        JSONObject goodsDetail = JSONObject.parseObject(esData);
        Object goodsSaleNum = goodsDetail.get("goodsSaleNum");
        if (goodsDetail != null && goodsSaleNum != null) {
            optimizeSpecGoodsDetailVO.getGoodsDetailsVO().setSpecSaleNum((Integer) goodsSaleNum);
        } else {
            optimizeSpecGoodsDetailVO.getGoodsDetailsVO().setSpecSaleNum(0);
        }

        //商品属性所有规格和规格对应的规格值,可以是多个规格，这个是商品的所有规格
        goodsDetailsVO.setSpecAttrValuePicList(specGoodsPic);
        // 设置商品默认图集合
        goodsDetailsVO.setGoodsPicList(goodsPic);

        // 活动商品库存展示
        if (CollectionUtils.isNotEmpty(goodsDetailsVO.getSpecActivityList())) {
            SpecActivityVO specActivityVO = goodsDetailsVO.getSpecActivityList().get(0);
            if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == specActivityVO.getActivityType()
                    && SeckillActivityEnum.ACTIVITY_STATE_START.value() == specActivityVO.getActivityState()) {
                // 秒杀活动库存 = 活动索引库存
                goodsDetailsVO.setSpecStorage(specActivityVO.getActivitySurplusStorage());
            } else if (ActivityTypeEnum.GROUP_ACTIVITY.value() == specActivityVO.getActivityType()
                    && GroupsEnum.ACTIVITY_STATUS_ONGOING.value() == specActivityVO.getActivityState()) {
                // 拼团活动库存 = 活动索引库存 + 普通商品库存
                goodsDetailsVO.setSpecStorage(specActivityVO.getActivitySurplusStorage() + goodsDetailsVO.getSpecStorage());
            }
        }
        return optimizeSpecGoodsDetailVO;
    }

    /**
     * ES 查询规格信息
     *
     * @param specId:  规格ID
     * @param goodsId: 商品ID
     * @date 2020/5/22 12:02
     * @author lixiangx@leimingtech.com
     **/
    private SearchResponse packSearchResponse(Long specId, Long goodsId) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        SearchRequest searchRequest = new SearchRequest(ElasticSearchConstant.GOODS_SPEC_INDEX);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (goodsId != null) {
            // 传递商品ID 增加根据商品ID筛序的过滤条件
            queryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("id", specId))
                    .should(QueryBuilders.termQuery("goodsId", goodsId)));
        } else {
            QueryBuilder queryBuilderGoodsName = QueryBuilders.matchQuery("id", specId);
            queryBuilder.must(queryBuilderGoodsName);
        }

        // 规格id聚合
        AggregationBuilder aggregationSpecId = AggregationBuilders.terms("goodsSpecAttrValueVOListSpecAttrId").field("goodsVO.goodsSpecAttrVOList.attrSpecId").size(100);
        searchSourceBuilder.query(queryBuilder).aggregation(aggregationSpecId).from(0).size(100);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 规格id搜索
     *
     * @param specId: 规格ID
     * @return
     */
    @Override
    public SpecGoodsDetailVO goodsSerarchSpec(Long specId, Long goodsId) {
        //属性值id聚合

        try {
            SpecGoodsDetailVO specGoodsDetailVO = new SpecGoodsDetailVO();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            SearchRequest searchRequest = new SearchRequest(ElasticSearchConstant.GOODS_SPEC_INDEX);

            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            if (goodsId != null) {
                queryBuilder.must(
                        QueryBuilders.boolQuery()
                                .should(QueryBuilders.matchQuery("id", specId))
                                .should(QueryBuilders.termQuery("goodsId", goodsId)));
            } else {
                QueryBuilder queryBuilderGoodsName = QueryBuilders.matchQuery("id", specId);
                queryBuilder.must(queryBuilderGoodsName);
            }

            // 规格id聚合
            AggregationBuilder aggregationSpecId = AggregationBuilders.terms("goodsSpecAttrValueVOListSpecAttrId").field("goodsVO.goodsSpecAttrVOList.attrSpecId").size(100);
            searchSourceBuilder
                    .query(queryBuilder)
                    .aggregation(aggregationSpecId);
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(100);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


            SearchHits hits = searchResponse.getHits();
            List aggSpecId = new ArrayList<>();
            Terms goodsSpecAttrValueSpecAttrIdVOList = searchResponse.getAggregations().get("goodsSpecAttrValueVOListSpecAttrId");
            for (Terms.Bucket bucket : goodsSpecAttrValueSpecAttrIdVOList.getBuckets()) {
                aggSpecId.add(bucket.getKeyAsString());
            }
            GoodsDetailsVO goodsDetailsVO = null;
            List<GoodsSpecPicVO> specGoodsPic = null;
            List<SpecGoodsPicVO> goodsPic = null;
            SpecGoodsVO specGoodsVO = null;
            List<SpecGoodsAttrVO> specGoodsAttrVO = null;
            List<GoodsSpecAttrVO> goodsSpecAttrVO = null;
            List<GoodsSpecAttr> goodsSpecAttrList = new ArrayList<>();
            Map<Long, String> attrValuesMap = new HashMap<>(10);
            for (SearchHit hit : hits) {
                String goodsDetails = hit.getSourceAsString();
                goodsDetailsVO = JSONObject.parseObject(goodsDetails, GoodsDetailsVO.class);

                // 产品确定从商品列表进入商品详情显示SPU价格、规格切换显示SKU的价格
                //图片
                specGoodsPic = goodsDetailsVO.getSpecAttrValuePicList();
                // 商品图片
                goodsPic = goodsDetailsVO.getGoodsPicList();
                //商品信息
                specGoodsVO = goodsDetailsVO.getGoodsVO();
                //商品属性
                specGoodsAttrVO = specGoodsVO.getGoodsAttrVOList();
                //商品规格属性名集合
                goodsSpecAttrVO = specGoodsVO.getGoodsSpecAttrVOList();

                List<SpecAttrValueRefVO> specAttrValueRefVOList = specGoodsVO.getSpecAttrValueRefVO();
                // 获取商品所有规格属性值  颜色、尺寸等
                List<GoodsSpecAttrValueVO> allGoodsSpecAttrValueList = goodsSpecAttrVO.stream().map(t -> t.getGoodsSpecAttrValueVOList().get(0)).distinct().collect(Collectors.toList());

                //根据规格ID去重
                List<GoodsSpecAttrVO> goodsSpecNameList = goodsSpecAttrVO.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GoodsSpecAttrVO::getId))), ArrayList::new)
                );
                List<Long> attrvalueId = new ArrayList<>();

                Map<Long, SpecAttrMsgVO> specAttrMap = Maps.newLinkedHashMap();

                goodsSpecNameList.forEach(goodsSpec -> {

                    // 循环规格数据 填充数据 商品规格属性集合，key规格属性id，value规格属性值集合
                    SpecAttrMsgVO specAttrMsgVO = new SpecAttrMsgVO();
                    specAttrMsgVO.setSpecAttrName(goodsSpec.getSpecAttrName());
                    List<SpecAttrValueVO> specAttrValueVOList = new ArrayList<>();
                    specAttrValueRefVOList.forEach(specAttrValueRefVO -> {
                        if (specAttrValueRefVO.getSpecAttrId().equals(goodsSpec.getId()) && specAttrValueRefVO.getDelFlag() == 0) {
                            if (!attrvalueId.contains(specAttrValueRefVO.getSpecAttrValueId())) {
                                SpecAttrValueVO specAttrValueVO = new SpecAttrValueVO();
                                specAttrValueVO.setAttrValueId(specAttrValueRefVO.getSpecAttrValueId());

                                allGoodsSpecAttrValueList.forEach(l -> {
                                    if (l.getId().equals(specAttrValueRefVO.getSpecAttrValueId())) {
                                        specAttrValueVO.setAttrValueName(l.getSpecAttrValue());
                                        attrValuesMap.put(specAttrValueRefVO.getSpecAttrValueId(), l.getSpecAttrValue());
                                    }
                                });


                                specAttrValueVO.setIsMain(specAttrValueRefVO.getIsMain());

                                specAttrValueVOList.add(specAttrValueVO);
                                attrvalueId.add(specAttrValueRefVO.getSpecAttrValueId());
                            }
                        }
                    });
                    specAttrMsgVO.setSpecAttrValueVOList(specAttrValueVOList);
                    specAttrMap.put(goodsSpec.getId(), specAttrMsgVO);
                });
                specGoodsDetailVO.setSpecAttrMap(specAttrMap);
                // 查询店铺状态
                PageModelDTO pageModel = new PageModelDTO();
                Map<String, Object> equalsSearchCondition = pageModel.getEqualsSearchCondition();
                equalsSearchCondition.put("id", specGoodsVO.getStoreId());
                String[] strings = new String[1];
                strings[0] = "isEnable";
                pageModel.setFetchSourceIncludes(strings);
                pageModel.setIsPage(false);
                esDataUtils.queryData(pageModel, ElasticSearchConstant.STORE_INDEX);
                List<String> jsonRsList = pageModel.getJsonRsList();
                List<StoreSearchVO> collect = jsonRsList.stream().map(p -> JSONObject.parseObject(p, StoreSearchVO.class)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(jsonRsList)) {
                    specGoodsVO.setStoreIsEnable(Integer.valueOf(collect.get(0).getIsEnable()));
                }

                for (GoodsSpecAttrVO goodsSpecAttr : goodsSpecAttrVO) {
                    GoodsSpecAttr goodsSpecAttrCollection = new GoodsSpecAttr();
                    BeanCopier.create(GoodsSpecAttrVO.class, GoodsSpecAttr.class, false).copy(goodsSpecAttr, goodsSpecAttrCollection, null);
                    goodsSpecAttrList.add(goodsSpecAttrCollection);
                }
                LinkedHashSet h = new LinkedHashSet(goodsSpecAttrList);
                goodsSpecAttrList.clear();
                goodsSpecAttrList.addAll(h);
                //商品属性所有规格和规格对应的规格值,可以是多个规格，这个是商品的所有规格
                goodsDetailsVO.setSpecAttrValuePicList(specGoodsPic);
                // 设置商品默认图集合
                goodsDetailsVO.setGoodsPicList(goodsPic);
                //商品属性
                specGoodsVO.setGoodsAttrVOList(specGoodsAttrVO);

                // 活动商品库存展示
                if (CollectionUtils.isNotEmpty(goodsDetailsVO.getSpecActivityList())) {
                    SpecActivityVO specActivityVO = goodsDetailsVO.getSpecActivityList().get(0);
                    if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == specActivityVO.getActivityType()
                            && SeckillActivityEnum.ACTIVITY_STATE_START.value() == specActivityVO.getActivityState()) {
                        // 秒杀活动库存 = 活动索引库存
                        goodsDetailsVO.setSpecStorage(specActivityVO.getActivitySurplusStorage());
                    } else if (ActivityTypeEnum.GROUP_ACTIVITY.value() == specActivityVO.getActivityType()
                            && GroupsEnum.ACTIVITY_STATUS_ONGOING.value() == specActivityVO.getActivityState()) {
                        // 拼团活动库存 = 活动索引库存 + 普通商品库存
                        goodsDetailsVO.setSpecStorage(specActivityVO.getActivitySurplusStorage() + goodsDetailsVO.getSpecStorage());
                    } else if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == specActivityVO.getActivityType()
                            && FlashSaleActivityEnum.ACTIVITY_STATE_START.value() == specActivityVO.getActivityState()) {
                        // 限时购活动库存 = 活动索引库存
                        goodsDetailsVO.setSpecStorage(specActivityVO.getActivitySurplusStorage());
                    }
                }
            }
            List<SpecGoodsNameAttrValueVO> specList = new ArrayList<>();
            //拼接商品所有规格信息
//        Map<String, Object> oneSpecMap = null;

            // 判断商品是否存在自定义规格
            if (CollectionUtils.isNotEmpty(specGoodsVO.getSpecAttrValueRefVO())) {

                // 查询其他规格信息 封装库存、上下架状态
                PageModelDTO pageModelDTO = new PageModelDTO();
                Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                equalsSearchCondition.put("goodsId", goodsDetailsVO.getGoodsId());
                esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);
                List<String> jsonRsList = pageModelDTO.getJsonRsList();
                List<GoodsDetailsVO> goodsDetailsVOList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(jsonRsList)) {
                    goodsDetailsVOList = JSONArray.parseArray(jsonRsList.toString(), GoodsDetailsVO.class);
                }

                List<SpecAttrValueRefVO> specAttrValueRefVO = specGoodsVO.getSpecAttrValueRefVO();
                for (int i = 0; i < aggSpecId.size(); i++) {
                    SpecGoodsNameAttrValueVO specGoodsNameAttrValueVO = new SpecGoodsNameAttrValueVO();

                    List<SpecGoodsValueIdVO> goodsSpecList = new ArrayList<>();
                    StringBuffer attrValueBuffer = new StringBuffer();
                    for (int j = 0; j < specAttrValueRefVO.size(); j++) {
                        if (aggSpecId.get(i).toString().equals(specAttrValueRefVO.get(j).getSpecId().toString())) {
                            //相同的规格拼接属性
                            SpecGoodsValueIdVO specGoodsValueIdVO = new SpecGoodsValueIdVO();
                            specGoodsValueIdVO.setSpecAttrId(specAttrValueRefVO.get(j).getSpecAttrId());
                            specGoodsValueIdVO.setSpecAttrValueId(specAttrValueRefVO.get(j).getSpecAttrValueId());
                            attrValueBuffer.append(attrValuesMap.get(specAttrValueRefVO.get(j).getSpecAttrValueId())).append(",");
                            goodsSpecList.add(specGoodsValueIdVO);
                            specGoodsNameAttrValueVO.setSpecShow(specAttrValueRefVO.get(j).getSpecShow());
                            specGoodsNameAttrValueVO.setSpecStorage(specAttrValueRefVO.get(j).getSpecStorage());
                            specGoodsNameAttrValueVO.setDelFlag(specAttrValueRefVO.get(j).getDelFlag());
                        }
                    }
                    String s = attrValueBuffer.toString();
                    specGoodsNameAttrValueVO.setSpecAttrValues(s.substring(0, s.length() - 1));

                    for (int j = 0; j < goodsDetailsVOList.size(); j++) {
                        // 选择规格商品库存、活动库存处理
                        if (aggSpecId.get(i).toString().equals(goodsDetailsVOList.get(j).getId().toString())) {
                            //相同的规格拼接属性
                            if (CollectionUtils.isNotEmpty(goodsDetailsVOList.get(j).getSpecActivityList())) {
                                specGoodsNameAttrValueVO.setActivityStorage(goodsDetailsVOList.get(j).getSpecActivityList().get(0).getActivitySurplusStorage());
                                specGoodsNameAttrValueVO.setActivityType(goodsDetailsVOList.get(j).getSpecActivityList().get(0).getActivityType());
                                specGoodsNameAttrValueVO.setActivityState(goodsDetailsVOList.get(j).getSpecActivityList().get(0).getActivityState());
                            }
                        }
                    }

                    specGoodsNameAttrValueVO.setSpecId(aggSpecId.get(i).toString());
                    specGoodsNameAttrValueVO.setSpecAttrAndAttrValue(goodsSpecList);
                    if (specGoodsNameAttrValueVO.getDelFlag() == 0) {
                        specList.add(specGoodsNameAttrValueVO);
                    }
                }
                specGoodsDetailVO.setGoodsDetailsVO(goodsDetailsVO);
                specGoodsDetailVO.setGoodsSpecList(specList);
                specGoodsDetailVO.setGoodsSpecNameValue(goodsSpecAttrList);
            } else {
                // 商品没有自定义规格  只有默认规格
                specGoodsDetailVO.setGoodsDetailsVO(goodsDetailsVO);
                specGoodsDetailVO.setGoodsSpecList(specList);
                specGoodsDetailVO.setGoodsSpecNameValue(goodsSpecAttrList);
            }
            //查询当前商品总销量
            String esData = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, specGoodsDetailVO.getGoodsDetailsVO().getGoodsId().toString());
            JSONObject goodsDetail = JSONObject.parseObject(esData);
            Object goodsSaleNum = goodsDetail.get("goodsSaleNum");
            Object secondStoreGoodsGcId = goodsDetail.get("secondStoreGoodsGcId");
            if (goodsSaleNum != null) {
                specGoodsDetailVO.getGoodsDetailsVO().setSpecSaleNum((Integer) goodsSaleNum);
            } else {
                specGoodsDetailVO.getGoodsDetailsVO().setSpecSaleNum(0);
            }

            if (secondStoreGoodsGcId != null) {
                specGoodsDetailVO.getGoodsDetailsVO().getGoodsVO().setSecondStoreGoodsGcId(Long.valueOf(String.valueOf(secondStoreGoodsGcId)));
            }
            return specGoodsDetailVO;
        } catch (IOException e) {
            LOGGER.error(SearchServiceCode.GOODS_SPEC_ID_SEARCH_ERROR.getCode(),
                    SearchServiceCode.GOODS_SPEC_ID_SEARCH_ERROR.getMessage(), e);

        }
        return null;
    }


    /**
     * 搜索店铺
     *
     * @param storeSearchDTO
     * @return
     */
    @Override
    public StoreSearchFilterVO storeSerarch(SearchStoreDTO storeSearchDTO) {
        SortOrder sortOrder;
        if (storeSearchDTO.getSortType() != null && SortOrder.DESC.toString().equals(storeSearchDTO.getSortType().toLowerCase())) {
            sortOrder = SortOrder.DESC;
        } else {
            sortOrder = SortOrder.ASC;
        }

        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

            if (StringUtils.isNotBlank(storeSearchDTO.getKeyword())) {

                queryBuilder.must(
                        QueryBuilders.boolQuery()
                                //分词后匹配
                                .should(QueryBuilders.matchQuery("storeName", storeSearchDTO.getKeyword())));
            }

            QueryBuilder queryBuilderGoodsShow = QueryBuilders.termQuery("isEnable", 0);
            queryBuilder.filter(queryBuilderGoodsShow);


            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


            searchSourceBuilder
                    .query(queryBuilder)
                    .from(storeSearchDTO.getPageSize() * (storeSearchDTO.getPageNo() - 1))
                    .size(storeSearchDTO.getPageSize())
                    .sort(storeSearchDTO.getSortField(), sortOrder);

            SearchRequest searchRequest = new SearchRequest(ElasticSearchConstant.STORE_INDEX);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


            //商品信息
            List<StoreSearchVO> storeList = new ArrayList<>();
            SearchHits hits = searchResponse.getHits();
            long count = hits.getTotalHits().value;

            for (SearchHit hit : hits) {
                String store = hit.getSourceAsString();
                StoreSearchVO storeSearchVO = JSONObject.parseObject(store, StoreSearchVO.class);
                // 查询店铺下销量最高的三个商品
                GoodsSearchFilterDTO goodsSearchVO = new GoodsSearchFilterDTO();
                goodsSearchVO.setPageNo(1);
                goodsSearchVO.setPageSize(3);
                goodsSearchVO.setSortField("goodsSaleNum");
                goodsSearchVO.setSortType(SortOrder.DESC.toString());
                goodsSearchVO.setStoreId(storeSearchVO.getId());
                GoodsSearchFilterVO goodsSearchFilterVO = goodsFilterSerarch(goodsSearchVO);
                storeSearchVO.setGoodsVOList(goodsSearchFilterVO.getGoodsVOList());
                // TODO 店铺星级待维护，现在没有这个功能
                storeSearchVO.setStoreStar(5);
                storeList.add(storeSearchVO);

            }

            StoreSearchFilterVO storeSearchFilterVO = new StoreSearchFilterVO();
            storeSearchFilterVO.setStoreVOList(storeList);
            storeSearchFilterVO.setTotalCount(count);
            storeSearchFilterVO.setPageNo(storeSearchDTO.getPageNo());
            storeSearchFilterVO.setPageSize(storeSearchDTO.getPageSize());

            return storeSearchFilterVO;

        } catch (IOException e) {
            LOGGER.error(SearchServiceCode.STORE_FILTER_SEARCH_ERROR.getCode(),
                    SearchServiceCode.STORE_FILTER_SEARCH_ERROR.getMessage(), e);
        }

        return null;
    }


    /**
     * 根据关键字智能提示（支持中文、拼音）
     *
     * @param keyword: 搜索关键字
     * @return 智能提示集合
     * @date 2019/12/17 16:00
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public List<String> intellisenseWord(String keyword) {
        List<String> result = new ArrayList<>();

        // 根据商品索引中的keywordTips查询并去除重复数据
        CompletionSuggestionBuilder stationCode = SuggestBuilders.completionSuggestion("keywordTips.s-pinyin").prefix(keyword).skipDuplicates(true);
        SearchSourceBuilder suggestBuilder = new SearchSourceBuilder().suggest(new SuggestBuilder().addSuggestion("pinyin-suggest", stationCode));
        SearchRequest searchRequest = new SearchRequest().indices(ElasticSearchConstant.GOODS_INDEX).source(suggestBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 获取Suggest数据
            Suggest suggestions = searchResponse.getSuggest();
            List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> results = suggestions.getSuggestion("pinyin-suggest").getEntries();
            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> op : results) {
                List<? extends Suggest.Suggestion.Entry.Option> options = op.getOptions();
                for (Suggest.Suggestion.Entry.Option pp : options) {
                    result.add(pp.getText().toString());
                }
            }
        } catch (IOException e) {
            log.error("根据关键字智能提示出现异常:{}", e);
        }
        return result;
    }
}
