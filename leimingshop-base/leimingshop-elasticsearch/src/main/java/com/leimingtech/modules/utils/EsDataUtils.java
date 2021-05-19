/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.EsConflictException;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.RangConditionDTO;
import com.leimingtech.modules.dto.RangConditionsToTimeModelDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Stats;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ES操作工具类（RestClient版）
 * 由于ES7.X移除了索引Type，此工具类只兼容ES7.X版本
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/11 9:50
 **/
@Slf4j
@Component
public class EsDataUtils {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 同步同义词调用的接口
     */
    @Value("${data.elasticsearch.synonyms.path}")
    private String SYNONYMS_PATH;

    /**
     * ES 保存数据
     *
     * @param indexName: 索引名称
     * @param oid:       主键
     * @param paramJson: 参数Json
     * @param clazz:     保存实体的Class
     * @return 保存结果
     */
    public boolean saveData(String indexName, String oid, String paramJson, Class clazz) {

        // 判断索引是否存在
        boolean result = isIndexExists(indexName);

        if (!result) {
            boolean createResult = createIndexAndCreateMapping(indexName, FieldMappingUtils.getFieldInfo(clazz));
            if (!createResult) {
                log.info("索引[{}],主键[{}]创建失败", indexName, oid);
                return false;
            }
        }

        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(oid);
        indexRequest.source(paramJson, XContentType.JSON);
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("索引[{}],主键[{}]保存异常:{}", indexName, oid, e);
            return false;
        }

        // 判断索引是新增还是修改 并对数量进行增加
        if (IndexResponse.Result.CREATED.equals(response.getResult())) {
            log.info("索引[{}],主键[{}]保存成功", indexName, oid);
            return true;
        } else if (IndexResponse.Result.UPDATED.equals(response.getResult())) {
            log.info("索引[{}],主键[{}]修改成功", indexName, oid);
            return true;
        }
        return false;
    }

    /**
     * ES 批量保存数据（同步）
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @return 批量保存结果
     * @date 2019/12/10 18:34
     * @author lixiangx@leimingtech.com
     **/
    public boolean saveDataBatch(String indexName, String primaryKeyName, String paramListJson, Class clazz) {

        // 判断索引是否存在
        boolean result = isIndexExists(indexName);

        if (!result) {
            boolean createResult = createIndexAndCreateMapping(indexName, FieldMappingUtils.getFieldInfo(clazz));
            if (!createResult) {
                log.info("索引[{}]创建失败", indexName);
                return false;
            }
        }

        BulkRequest bulkRequest = packBulkIndexRequest(indexName, primaryKeyName, paramListJson);
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        try {
            // 同步执行
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("索引[{}],主键[{}]更新操作失败,状态为:[{}],错误信息:{}", indexName, item.getId(),
                            item.status(), item.getFailureMessage());
                }
                return false;
            }

            // 记录索引新增与修改数量
            Integer createdCount = 0;
            Integer updatedCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (IndexResponse.Result.CREATED.equals(item.getResponse().getResult())) {
                    createdCount++;
                } else if (IndexResponse.Result.UPDATED.equals(item.getResponse().getResult())) {
                    updatedCount++;
                }
            }
            log.info("索引[{}]批量同步更新成功,共新增[{}]个,修改[{}]个", indexName, createdCount, updatedCount);
        } catch (IOException e) {
            log.error("索引[{}]批量同步更新出现异常:{}", indexName, e);
            return false;
        }
        return true;
    }

    /**
     * ES 批量保存数据（异步）
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @return 批量保存结果
     * @date 2019/12/10 18:34
     * @author lixiangx@leimingtech.com
     **/
    public boolean saveDataBatchAsync(String indexName, String primaryKeyName, String paramListJson) {

        BulkRequest bulkRequest = packBulkIndexRequest(indexName, primaryKeyName, paramListJson);
        try {
            //异步执行
            ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkResponse) {
                    if (bulkResponse.hasFailures()) {
                        for (BulkItemResponse item : bulkResponse.getItems()) {
                            log.error("索引[{}],主键[{}]更新操作失败,状态为:[{}],错误信息:{}", indexName, item.getId(),
                                    item.status(), item.getFailureMessage());
                        }
                    }
                }

                // 失败操作
                @Override
                public void onFailure(Exception e) {
                    log.error("索引[{}]批量异步更新出现异常:{}", indexName, e);
                }
            };
            restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, listener);
            log.info("异步批量更新索引[{}]中", indexName);
        } catch (Exception e) {
            log.info("异步批量更新索引[{}]出现异常:{}", indexName, e);
            return false;
        }
        return true;
    }

    /**
     * ES 修改数据
     *
     * @param indexName: 索引名称
     * @param oid:       主键
     * @param paramJson: 参数JSON
     * @return 修改结果
     * @date 2019/12/10 19:10
     * @author lixiangx@leimingtech.com
     **/
    public boolean updateData(String indexName, String oid, String paramJson) {

        UpdateRequest updateRequest = new UpdateRequest(indexName, oid);
        // 如果修改索引中不存在则进行新增
        updateRequest.docAsUpsert(true);
        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        updateRequest.doc(paramJson, XContentType.JSON);
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            log.info("索引[{}],主键:[{}]操作结果:[{}]", indexName, oid, updateResponse.getResult());

            if (UpdateResponse.Result.CREATED.equals(updateResponse.getResult())) {
                // 新增
                log.info("索引:[{}],主键:[{}]新增成功", indexName, oid);
                return true;
            } else if (UpdateResponse.Result.UPDATED.equals(updateResponse.getResult())) {
                // 修改
                log.info("索引:[{}],主键:[{}]修改成功", indexName, oid);
                return true;
            } else if (UpdateResponse.Result.NOOP.equals(updateResponse.getResult())) {
                // 无变化
                log.info("索引:[{}] 主键:[{}] 无变化", indexName, oid);
                return true;
            }
        } catch (IOException e) {
            log.info("索引:[{}],主键:[{}] 更新异常:{}", indexName, oid, e);
            return false;
        }
        return false;
    }

    /**
     * 功能描述：
     * <ES 批量修改数据（同步）,如果索引不存在则创建索引>
     *
     * @param indexName      索引名称
     * @param primaryKeyName 索引主键名称
     * @param paramListJson  数据集合JSON
     * @param clazz
     * @return
     * @date 2020/4/20 16:21
     * @author 刘远杰
     **/
    public boolean updateDataBatch(String indexName, String primaryKeyName, String paramListJson, Class clazz) {
        // 判断索引是否存在
        boolean result = isIndexExists(indexName);

        if (!result) {
            boolean createResult = createIndexAndCreateMapping(indexName, FieldMappingUtils.getFieldInfo(clazz));
            if (!createResult) {
                log.info("索引[{}]创建失败", indexName);
                return false;
            }
        }

        return this.updateDataBatch(indexName, primaryKeyName, paramListJson);
    }

    /**
     * ES 批量修改数据（同步）
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @return 批量修改结果
     * @date 2019/12/10 18:34
     * @author lixiangx@leimingtech.com
     **/
    public boolean updateDataBatch(String indexName, String primaryKeyName, String paramListJson) {
        if (StringUtils.isBlank(paramListJson)) {
            return false;
        }
        BulkRequest bulkRequest = packBulkUpdateRequest(indexName, primaryKeyName, paramListJson);
        if (bulkRequest.requests().isEmpty()) {
            return false;
        }
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        try {
            // 同步执行
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("索引[{}],主键[{}]修改操作失败,状态为:[{}],错误信息:{}", indexName, item.getId(),
                            item.status(), item.getFailureMessage());
                }
                return false;
            }

            // 记录索引新增与修改数量
            Integer createdCount = 0;
            Integer updatedCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (IndexResponse.Result.CREATED.equals(item.getResponse().getResult())) {
                    createdCount++;
                } else if (IndexResponse.Result.UPDATED.equals(item.getResponse().getResult())) {
                    updatedCount++;
                }
            }
            log.info("索引[{}]批量修改更新成功,共新增[{}]个,修改[{}]个", indexName, createdCount, updatedCount);
        } catch (IOException e) {
            log.error("索引[{}]批量修改更新出现异常", indexName);
            return false;
        }
        return true;
    }

    /**
     * 功能描述：
     * <es集合元素新增某个对象：不存在插入数据，存在删除重新添加>
     *
     * @param indexName 索引名称
     * @param id        索引主键
     * @param paramJson 更新对象-（field属性对应集合的修改对象）
     * @param property  更新对象唯一属性名称（根据此属性判断插入对象是否相同）
     * @param field     更新属性名称
     * @return
     * @date 2020/4/10 16:33
     * @author 刘远杰
     **/
    public boolean saveSubListById(String indexName, String id, String paramJson, String property, String field) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(paramJson);
        params.put(field, jsonObject);
        // 如果存在该字段,删除原数据重新添加
        String scriptStr = "if(!ctx._source.containsKey('" + field + "'))" + "{ctx._source." + field + "=[params." + field + "]} "
                + "else { for(int i=0; i<ctx._source." + field + ".size(); i++) "
                + "{ if (ctx._source." + field + "[i]['" + property + "'] == params." + field + "." + property + ")"
                + "{ctx._source." + field + ".remove(i);break;}}"
                + "ctx._source." + field + ".add(params." + field + ");}";
        if (StringUtils.isNotBlank(scriptStr)) {
            Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, scriptStr, params);
            log.debug("script:\n" + script);
            UpdateResponse updateResponse = null;
            try {
                // doc与script不可同时使用
                UpdateRequest updateRequest = new UpdateRequest(indexName, id);

                // 设置es脚本
                updateRequest.script(script);

                updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

                log.info(String.valueOf(updateResponse.status()));
                if (StringUtils.equalsIgnoreCase(updateResponse.status().toString(), "CONFLICT")) {
                    throw new EsConflictException();
                }
            } catch (Exception e) {
                log.error("保存索引：[{}]，主键：[{}]，属性：[{}]，属性值：[{}]出现异常：{}", indexName, id, field, paramJson, e);
                return false;
            }
        }
        return true;
    }

    /**
     * 功能描述：
     * <es集合元素批量新增某个对象 不存在插入，存在删除后重新插入>
     *
     * @param indexName     索引名称
     * @param paramListJson 更新对象-（field属性对应集合的修改对象）
     * @param property      更新对象唯一属性名称（根据此属性判断插入对象是否相同）
     * @param field         更新属性名称
     * @return
     * @date 2020/4/10 16:33
     * @author 刘远杰
     **/
    public boolean saveSubListBatchById(String indexName, String paramListJson, String property, String field) {
        BulkRequest bulkRequest = getJsBulkRequest(indexName, paramListJson, field, property, "save");

        try {
            if (bulkRequest == null) {
                return false;
            }
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("索引[{}],主键[{}]新增操作失败,状态为:[{}],错误信息:{}", indexName, item.getId(),
                            item.status(), item.getFailureMessage());
                    if (StringUtils.equalsIgnoreCase(item.status().toString(), "CONFLICT")) {
                        throw new EsConflictException();
                    }
                }
                return false;
            }

            // 记录索引删除数量
            Integer insertCount = 0;
            Integer updateCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (IndexResponse.Result.UPDATED.equals(item.getResponse().getResult())) {
                    updateCount++;
                } else if (IndexResponse.Result.CREATED.equals(item.getResponse().getResult())) {
                    insertCount++;
                }
            }
            log.info(String.valueOf(bulk.status()));
            log.info("索引[{}]批新增成功,共修改[{}]个,新增[{}]个", indexName, updateCount, insertCount);
        } catch (Exception e) {
            log.error("新增索引：[{}]，属性：[{}]，属性值：[{}]出现异常：{}", indexName, field, paramListJson, e);
            return false;
        }

        return true;
    }

    /**
     * 功能描述：
     * <es集合元素更新某个对象 存在更新>
     *
     * @param indexName 索引名称
     * @param id        索引主键
     * @param paramJson 更新对象-（field属性对应集合的修改对象）
     * @param property  更新对象唯一属性名称（根据此属性判断插入对象是否相同）
     * @param field     更新属性名称
     * @return
     * @date 2020/4/10 16:33
     * @author 刘远杰
     **/
    public boolean updateSubListById(String indexName, String id, String paramJson, String property, String field) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(paramJson);
        params.put(field, jsonObject);
        // 如果存在该字段,会比较传入的对象是否存在于list中存在的对象相等，如果相等就更新，不存在更新失败
        String scriptStr = "if(ctx._source.containsKey('" + field + "')) "
                + "{ for(int i=0; i<ctx._source." + field + ".size(); i++) "
                + "{ if (ctx._source." + field + "[i]['" + property + "']== params." + field + "." + property + ")"
                + "{ctx._source." + field + ".remove(i);ctx._source." + field + ".add(params." + field + ");break;}}}";
        if (StringUtils.isNotBlank(scriptStr)) {
            Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, scriptStr, params);
            log.debug("script:\n" + script);
            UpdateResponse updateResponse = null;
            try {
                // doc与script不可同时使用
                UpdateRequest updateRequest = new UpdateRequest(indexName, id);

                // 设置es脚本
                updateRequest.script(script);

                updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

                log.info(String.valueOf(updateResponse.status()));
                if (StringUtils.equalsIgnoreCase(updateResponse.status().toString(), "CONFLICT")) {
                    throw new EsConflictException();
                }
            } catch (Exception e) {
                log.error("修改索引：[{}]，主键：[{}]，属性：[{}]，属性值：[{}]出现异常：{}", indexName, id, field, paramJson, e);
                return false;
            }
        }
        return true;
    }

    /**
     * 功能描述：
     * <es集合元素批量更新某个对象 存在更新>
     *
     * @param indexName     索引名称
     * @param paramListJson 更新对象-（field属性对应集合的修改对象）
     * @param property      更新对象唯一属性名称（根据此属性判断插入对象是否相同）
     * @param field         更新属性名称
     * @return
     * @date 2020/4/10 16:33
     * @author 刘远杰
     **/
    public boolean updateSubListBatchById(String indexName, String paramListJson, String property, String field) {
        BulkRequest bulkRequest = getJsBulkRequest(indexName, paramListJson, field, property, "update");

        try {
            if (bulkRequest == null) {
                return false;
            }
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("索引[{}],主键[{}]修改操作失败,状态为:[{}],错误信息:{}", indexName, item.getId(),
                            item.status(), item.getFailureMessage());
                    if (StringUtils.equalsIgnoreCase(item.status().toString(), "CONFLICT")) {
                        throw new EsConflictException();
                    }
                }
                return false;
            }

            // 记录索引删除数量
            Integer updateCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (IndexResponse.Result.UPDATED.equals(item.getResponse().getResult())) {
                    updateCount++;
                }
            }
            log.info(String.valueOf(bulk.status()));
            log.info("索引[{}]批量更新成功,共修改[{}]个", indexName, updateCount);
        } catch (Exception e) {
            log.error("修改索引：[{}]，属性：[{}]，属性值：[{}]出现异常：{}", indexName, field, paramListJson, e);
            return false;
        }

        return true;
    }

    /**
     * 功能描述：
     * <es集合元素删除某个对象>
     *
     * @param indexName 索引名称
     * @param id        索引主键
     * @param paramJson 更新对象-（field属性对应集合的修改对象）
     * @param property  更新对象唯一属性名称（根据此属性判断插入对象是否相同）
     * @param field     更新属性名称
     * @return
     * @date 2020/4/10 16:33
     * @author 刘远杰
     **/
    public boolean deleteSubListById(String indexName, String id, String paramJson, String property, String field) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(paramJson);
        params.put(field, jsonObject);
        String scriptStr = "if(ctx._source.containsKey('" + field + "')) "
                + "{for (int i=0;i<ctx._source." + field + ".size();i++)"
                + "{ if(ctx._source." + field + "[i]['" + property + "'] == params." + field + "." + property + ")"
                + "{ctx._source." + field + ".remove(i);break;}}}";
        if (StringUtils.isNotBlank(scriptStr)) {
            Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, scriptStr, params);
            log.debug("script:\n" + script);
            UpdateResponse updateResponse = null;
            try {
                // doc与script不可同时使用
                UpdateRequest updateRequest = new UpdateRequest(indexName, id);

                // 设置es脚本
                updateRequest.script(script);

                updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

                log.info(String.valueOf(updateResponse.status()));
                if (StringUtils.equalsIgnoreCase(updateResponse.status().toString(), "CONFLICT")) {
                    throw new EsConflictException();
                }
            } catch (Exception e) {
                log.error("删除索引：[{}]，主键：[{}]，属性：[{}]，属性值：[{}]出现异常：{}", indexName, id, field, paramJson, e);
                return false;
            }
        }
        return true;
    }

    /**
     * 功能描述：
     * <es集合元素批量删除某个对象>
     *
     * @param indexName     索引名称
     * @param paramListJson 更新对象-（field属性对应集合的修改对象）
     * @param property      更新对象唯一属性名称（根据此属性判断插入对象是否相同）
     * @param field         更新属性名称
     * @return
     * @date 2020/4/10 16:33
     * @author 刘远杰
     **/
    public boolean deleteSubListBatchById(String indexName, String paramListJson, String property, String field) {

        BulkRequest bulkRequest = getJsBulkRequest(indexName, paramListJson, field, property, "delete");

        try {
            if (bulkRequest == null) {
                return false;
            }
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("索引[{}],主键[{}]删除操作失败,状态为:[{}],错误信息:{}", indexName, item.getId(),
                            item.status(), item.getFailureMessage());
                    if (StringUtils.equalsIgnoreCase(item.status().toString(), "CONFLICT")) {
                        throw new EsConflictException();
                    }
                }
                return false;
            }

            // 记录索引删除数量
            Integer deleteCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (IndexResponse.Result.UPDATED.equals(item.getResponse().getResult())) {
                    deleteCount++;
                }
            }
            log.info(String.valueOf(bulk.status()));
            log.info("索引[{}]批量删除成功,共删除[{}]个", indexName, deleteCount);
        } catch (Exception e) {
            log.error("删除索引：[{}]，属性：[{}]，属性值：[{}]出现异常：{}", indexName, field, paramListJson, e);
            return false;
        }

        return true;
    }

    /**
     * 功能描述：
     * <构建es请求>
     *
     * @param indexName     索引名称
     * @param paramListJson 更新结果
     * @param field         更新属性
     * @param property      更新属性
     * @param type          操作类型 save新增 update修改 delete删除
     * @return
     * @date 2020/4/13 11:04
     * @author 刘远杰
     **/
    private BulkRequest getJsBulkRequest(String indexName, String paramListJson, String field, String property, String type) {
        JSONArray jsonArray = JSONArray.parseArray(paramListJson);
        if (CollectionUtils.isEmpty(jsonArray)) {
            return null;
        }

        BulkRequest bulkRequest = new BulkRequest();

        for (Object obj : jsonArray) {
            Map<String, Object> params = new HashMap<>();
            // 删除对象
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(obj));

            // 索引id
            String id = String.valueOf(jsonObject.get("id"));
            if (StringUtils.isBlank(id)) {
                continue;
            }

            // 删除对象
            Object o = jsonObject.get(field);
            if (o == null) {
                continue;
            }
            JSONObject jsonObject1 = JSONObject.parseObject(JSONObject.toJSONString(o));
            if (jsonObject1 == null) {
                continue;
            }
            params.put(field, jsonObject1);

            String scriptStr = "";
            if (StringUtils.equals(type, "save")) {
                scriptStr = "if(!ctx._source.containsKey('" + field + "'))" + "{ctx._source." + field + "=[params." + field + "]} "
                        + "else { for(int i=0; i<ctx._source." + field + ".size(); i++) "
                        + "{ if (ctx._source." + field + "[i]['" + property + "'] == params." + field + "." + property + ")"
                        + "{ctx._source." + field + ".remove(i);break;}}"
                        + "ctx._source." + field + ".add(params." + field + ");}";
            } else if (StringUtils.equals(type, "update")) {
                scriptStr = "if(ctx._source.containsKey('" + field + "')) "
                        + "{ for(int i=0; i<ctx._source." + field + ".size(); i++) "
                        + "{ if (ctx._source." + field + "[i]['" + property + "']== params." + field + "." + property + ")"
                        + "{ctx._source." + field + ".remove(i);ctx._source." + field + ".add(params." + field + ");break;}}}";
            } else if (StringUtils.equals(type, "delete")) {
                scriptStr = "if(ctx._source.containsKey('" + field + "')) "
                        + "{for (int i=0;i<ctx._source." + field + ".size();i++)"
                        + "{ if(ctx._source." + field + "[i]['" + property + "'] == params." + field + "." + property + ")"
                        + "{ctx._source." + field + ".remove(i);break;}}}";
            }

            if (StringUtils.isNotBlank(scriptStr)) {
                Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, scriptStr, params);
                log.debug("script:\n" + script);

                // doc与script不可同时使用
                UpdateRequest updateRequest = new UpdateRequest(indexName, id);

                // 设置es脚本
                updateRequest.script(script);

                bulkRequest.add(updateRequest);
            }
        }
        return bulkRequest;
    }

    /**
     * ES 查询更新操作（更新符合条件的数据）不支持嵌套属性值修改
     *
     * @param indexName:  索引名称
     * @param pageModel:  搜索封装实体
     * @param updateJson: 修改数据的JSON
     * @return 操作结果
     * @date 2020/1/9 18:01
     * @author lixiangx@leimingtech.com
     **/
    public boolean updateByQueryDate(PageModelDTO pageModel, String indexName, String updateJson) {

        // JSON转map
        Map<String, Object> map = JSONObject.parseObject(updateJson);
        // 封装Script中的code 数据格式:ctx._source.goodsName=params.goodsName;
        StringBuilder sb = new StringBuilder();
        map.keySet().stream().forEach(p -> {
            sb.append("ctx._source.").append(p).append("=params.").append(p).append(";");
        });


        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(indexName);
        updateByQueryRequest.setRefresh(true);
        // 设置查询条件
        updateByQueryRequest.setQuery(queryBuilder(pageModel));
        //设置版本冲突时继续
        updateByQueryRequest.setConflicts("proceed");
        Script script = new Script(ScriptType.INLINE, "painless", sb.toString(), map);
        updateByQueryRequest.setScript(script);

        try {
            BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
            log.info("查询更新索引:[{}]操作共耗时:[{}],共修改文档数:[{}]",
                    indexName, bulkByScrollResponse.getTook(), bulkByScrollResponse.getUpdated());
            return true;
        } catch (IOException e) {
            log.error("查询更新索引:[{}]操作出现异常:{}", indexName, e);
            return false;
        }
    }


    /**
     * 删除查询（删除符合条件的数据）
     *
     * @param pageModel: 查询实体
     * @param indexName: 索引名称
     * @return 操作结果
     * @date 2020/1/10 14:36
     * @author lixiangx@leimingtech.com
     **/
    public boolean deleteByQuery(PageModelDTO pageModel, String indexName) {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(indexName);
        deleteByQueryRequest.setRefresh(true);
        deleteByQueryRequest.setQuery(queryBuilder(pageModel));
        try {
            BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
            log.info("查询更新索引:[{}]操作共耗时:[{}],共删除文档数:[{}]",
                    indexName, bulkByScrollResponse.getTook(), bulkByScrollResponse.getDeleted());
            return true;
        } catch (IOException e) {
            log.error("查询更新索引:[{}]操作出现异常:{}", indexName, e);
            return false;
        }

    }


    /**
     * ES 根据主键删除数据
     *
     * @param indexName: 索引名称
     * @param oid:       主键ID
     * @return 操作结果
     * @date 2019/12/11 9:53
     * @author lixiangx@leimingtech.com
     **/
    public boolean deleteDate(String indexName, String oid) {

        DeleteRequest deleteRequest = new DeleteRequest(indexName);
        deleteRequest.id(oid);
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);


        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.error("索引[{}]主键[{}]删除失败", indexName, oid);
                return false;
            } else {
                log.info("索引[{}]主键[{}]删除成功", indexName, oid);
                return true;
            }
        } catch (IOException e) {
            log.error("删除索引[{}]出现异常[{}]", indexName, e);
            return false;
        }
    }

    /**
     * ES 批量删除ES索引
     *
     * @param indexName: 索引名称
     * @param ids:       主键集合
     * @return 操作结果
     * @date 2019/12/10 20:46
     * @author lixiangx@leimingtech.com
     **/
    public boolean bulkDelete(String indexName, List<Long> ids) {

        BulkRequest bulkRequest = packBulkDeleteRequest(indexName, ids);
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        try {
            // 同步执行
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("更新索引：{}，主键：{}失败。信息为：{}", indexName, item.getId(), item.getFailureMessage());
                }
                return false;
            }

            // 记录索引新增与修改数量
            Integer deleteCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (DeleteResponse.Result.DELETED.equals(item.getResponse().getResult())) {
                    deleteCount++;
                }
            }
            log.info("批量删除索引[{}]成功,共删除[{}]个", indexName, deleteCount);
        } catch (IOException e) {
            log.error("删除索引：{}批量保存数据出现异常:{}", indexName, e);
            return false;
        }
        return true;
    }

    /**
     * ES 清空索引内容
     *
     * @param indexName: 索引名称
     * @return 操作结果
     * @date 2019/12/10 21:07
     * @author lixiangx@leimingtech.com
     **/
    public boolean deleteAll(String indexName) {
        // 判断索引是否存在
        boolean result = isIndexExists(indexName);
        if (result) {
            log.error("索引[{}]不存在，删除失败", indexName);
            return false;
        }
        try {

            DeleteRequest deleteRequest = new DeleteRequest(indexName);
            deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.error("索引[{}]删除失败", indexName);
                return false;
            }
            log.info("索引[{}]删除成功", indexName);
            return true;
        } catch (IOException e) {
            log.error("删除索引[{}]，出现异常:[{}]", indexName, e);
            return false;
        }
    }

    /**
     * ES 删除索引
     *
     * @param indexName: 索引名称
     * @return 操作结果
     * @date 2019/12/11 16:59
     * @author lixiangx@leimingtech.com
     **/
    public boolean deleteIndex(String indexName) {
        // 判断索引是否存在
        boolean result = isIndexExists(indexName);
        if (!result) {
            log.error("索引[{}]不存在删除索引失败", indexName);
            return false;
        }
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            if (!acknowledgedResponse.isAcknowledged()) {
                log.error("索引[{}]删除失败", indexName);
            }
            log.info("索引[{}]删除成功", indexName);
            return true;
        } catch (IOException e) {
            log.error("索引[{}]删除异常:{}", indexName, e);
        }
        return false;
    }

    /**
     * ES 根据主键查询索引名称
     *
     * @param indexName: 索引名称
     * @param oid:       主键
     * @return 返回数据JSON
     * @date 2019/12/11 9:48
     * @author lixiangx@leimingtech.com
     **/
    public String getDateById(String indexName, String oid) {

        // 判断索引是否存在
        if (!isIndexExists(indexName)) {
            return "";
        }

        GetRequest getRequest = new GetRequest(indexName, oid);
        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            String resultJson = getResponse.getSourceAsString();
            log.debug("索引[{}]主键[{}]查询结果[{}]", indexName, oid, resultJson);
            return resultJson;
        } catch (IOException e) {
            log.debug("索引[{}]主键[{}]查询异常:{}", indexName, oid, e);
            return "";
        }
    }

    /**
     * ES 搜索 支持多种搜索方式（分页、区间、模糊、OR、IN、过滤）
     *
     * @param pageModel: 搜索对象封装的实体
     * @param indexName: 索引名称
     * @return 查询返回的实体
     * @date 2019/12/11 10:00
     * @author lixiangx@leimingtech.com
     **/
    public PageModelDTO queryData(PageModelDTO pageModel, String indexName) {

        // 判断索引是否存在
        if (!isIndexExists(indexName)) {
            return pageModel;
        }
        // 获取页码、页面大小
        int pageStart = 0;
        int pageSize = 10000;
        if (pageModel.getIsPage()) {
            pageSize = pageModel.getPageSize();
            pageStart = (pageModel.getPageNum() - 1) * pageSize;
        }

        // 封装boolBuilder
        QueryBuilder boolBuilder = queryBuilder(pageModel);

        // 封装SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                // 设置查询关键词
                .query(boolBuilder)
                // 设置查询数据的位置,分页用
                .from(pageStart)
                // 设置查询结果集的最大条数
                .size(pageSize)
                // 不展示分析逻辑
                .explain(false)
                // 配置高亮
                .highlighter(this.getHighlightBuilder(pageModel));

        //指定查询包含的字段，指定查询不包含的字段
        if (pageModel.getFetchSourceIncludes() != null || pageModel.getFetchSourceExcludes() != null) {
            searchSourceBuilder.fetchSource(pageModel.getFetchSourceIncludes(), pageModel.getFetchSourceExcludes());
        }

        //排序list，以安排排序的优先级顺序
        for (Map<String, String> map : pageModel.getSortFiledsList()) {
            for (String sortKey : map.keySet()) {
                if (ElasticSearchConstant.SORT_ASC.equalsIgnoreCase(pageModel.getSortFileds(sortKey))) {
                    searchSourceBuilder.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.ASC));
                } else if (ElasticSearchConstant.DESC_ASC.equalsIgnoreCase(pageModel.getSortFileds(sortKey))) {
                    searchSourceBuilder.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.DESC));
                }
            }
        }

        // 设置排序字段
        for (String sortKey : pageModel.getSortFileds().keySet()) {
            if (ElasticSearchConstant.SORT_ASC.equalsIgnoreCase(pageModel.getSortFileds(sortKey))) {
                searchSourceBuilder.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.ASC));
            } else if (ElasticSearchConstant.DESC_ASC.equalsIgnoreCase(pageModel.getSortFileds(sortKey))) {
                searchSourceBuilder.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.DESC));
            }
        }

        if (StringUtils.isNotBlank(pageModel.getCollapseField())) {
            CollapseBuilder collapseBuilder = new CollapseBuilder(pageModel.getCollapseField());
            InnerHitBuilder innerHitBuilder = new InnerHitBuilder();
            innerHitBuilder.setName("collapse").setIgnoreUnmapped(true).setFrom(0).setSize(1000).setTrackScores(true);
            collapseBuilder.setInnerHits(innerHitBuilder);
            searchSourceBuilder.collapse(collapseBuilder);
        }

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);

        // 设置查询类型
        // 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
        // 2.SearchType.SCAN = 扫描查询,无序
        // 3.SearchType.COUNT = 不设置的话,这个为默认值,
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            pageModel.setTotal(searchHits.getTotalHits().value);
            log.debug("共匹配到:" + searchHits.getTotalHits().value + "条记录!");

            SearchHit[] hits = searchHits.getHits();
            pageModel.setSearchHits(hits);
            for (SearchHit searchHit : hits) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

                if (StringUtils.isNotBlank(pageModel.getCollapseField()) && searchHit.getInnerHits() != null) {
                    Map<String, SearchHits> innerHits = searchHit.getInnerHits();
                    if (innerHits.get("collapse") != null) {
                        SearchHit[] collapseHits = innerHits.get("collapse").getHits();
                        for (SearchHit collapseHit : collapseHits) {
                            Map<String, Object> collapseSourceAsMap = collapseHit.getSourceAsMap();
                            String collapseJson = JSON.toJSONString(collapseSourceAsMap);
                            if (!sourceAsMap.containsKey("collapse")) {
                                sourceAsMap.put("collapse", Collections.singleton(collapseJson));
                            } else {
                                List<String> collapseList = JSONArray.parseArray(sourceAsMap.get("collapse").toString(), String.class);
                                collapseList.add(collapseJson);
                                sourceAsMap.put("collapse", collapseList);
                                pageModel.setTotal(pageModel.getTotal() - 1);
                            }
                        }
                    }

                }
                String json = JSON.toJSONString(sourceAsMap);
                pageModel.setJsonRsList(json);
            }
        } catch (IOException e) {
            log.error("查询索引[{}]数据出现异常{}", indexName, e);
            return pageModel;
        }

        return pageModel;
    }


    /**
     * ES 查询数量
     *
     * @param indexName: 索引名称
     * @return 数量
     * @date 2020/1/13 15:47
     * @author lixiangx@leimingtech.com
     **/
    public Long countData(PageModelDTO pageModel, String indexName, Class clazz) {
        long count = 0L;
        // 判断索引是否存在
        boolean result = isIndexExists(indexName);

        if (!result && clazz != null) {
            boolean createResult = createIndexAndCreateMapping(indexName, FieldMappingUtils.getFieldInfo(clazz));
            if (!createResult) {
                log.info("索引[{}],主键[{}]创建失败", indexName);
                return count;
            }
        }

        CountRequest countRequest = new CountRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder(pageModel));
        countRequest.source(searchSourceBuilder);
        CountResponse countResponse = null;
        try {
            countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            count = countResponse.getCount();
            log.info("查询索引:[{}]数量为:[{}]", indexName, count);
        } catch (IOException e) {
            log.error("查询索引:[{}]数量出现异常:{}", indexName, e);
        }
        return count;
    }

    /**
     * 查询满足条件的sum值 （不支持嵌套查询、多分组查询）
     *
     * @param pageModel: 查询条件封装
     * @param aggParam:  group by 后的字段名称
     * @param sumParam:  sum(*) 需要总和的字段名称
     * @param indexName: 索引名称
     * @return 数值总和
     * @date 2020/1/14 10:35
     * @author lixiangx@leimingtech.com
     **/
    public double sumData(PageModelDTO pageModel, String aggParam, String sumParam, String indexName) {

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 类似于 select sum(sumParam) from table_name group by aggParam
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("groupByResult")
                .field(aggParam);
        aggregation.subAggregation(AggregationBuilders.stats("sumResult")
                .field(sumParam));
        searchSourceBuilder.aggregation(aggregation);

        // 设置搜索条件
        searchSourceBuilder.query(queryBuilder(pageModel));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询索引:[{}]总和异常:{}", indexName, e);
        }

        // 进行结果集解析
        Aggregations aggregations = searchResponse.getAggregations();
        Terms byCompanyAggregation = aggregations.get("groupByResult");
        List<? extends Terms.Bucket> buckets = byCompanyAggregation.getBuckets();
        double sumResult = 0;
        for (Terms.Bucket bucket : buckets) {
            Stats statAge = bucket.getAggregations().get("sumResult");
            if (statAge != null) {
                sumResult = statAge.getSum();
                break;
            }
//            log.info("平均值：{}", statAge.getAvg());
//            log.info("总数：{}", statAge.getSum());
//            log.info("最大值：{}", statAge.getMaxAsString());
//            log.info("最小值：{}", statAge.getMin());
        }

        return sumResult;
    }

    /**
     * 拼接高亮字段
     *
     * @param pageModel
     * @return
     */
    private HighlightBuilder getHighlightBuilder(PageModelDTO pageModel) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");

        List<String> fields = pageModel.getHightFieldList();

        for (String field : fields) {
            highlightBuilder.field(field);
        }
        return highlightBuilder;
    }


    /**
     * 拼接查询条件
     *
     * @param pageModel
     * @return
     */
    private BoolQueryBuilder queryBuilder(PageModelDTO pageModel) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        // 区间查询
        sectionSearch(pageModel, boolBuilder);
        // 模糊查询
        likeSearch(pageModel, boolBuilder);

        // or拼接查询
        BoolQueryBuilder orBoolQueryBuilder = QueryBuilders.boolQuery();
        Map<String, Object> orSearchCondition = pageModel.getOrSearchCondition();
        filterOrQuery(orSearchCondition, orBoolQueryBuilder);
        boolBuilder.must(orBoolQueryBuilder);

        // and (or)拼接查询
        for (Map<String, Object> searchCondition : pageModel.getOrSearchConditionList()) {
            BoolQueryBuilder termBoolQueryBuilder = QueryBuilders.boolQuery();
            filterOrQuery(searchCondition, termBoolQueryBuilder);
            boolBuilder.should(termBoolQueryBuilder);
        }

        // in查询
        for (String key : pageModel.getInSearchCondition().keySet()) {
            BoolQueryBuilder ptermsBoolQueryBuilder = QueryBuilders.boolQuery();
            TermsQueryBuilder termQueryBuilder = QueryBuilders.termsQuery(key, pageModel.getInSearchCondition().get(key));
            ptermsBoolQueryBuilder.should(termQueryBuilder);
            TermsQueryBuilder termQueryBuilder2 = QueryBuilders.termsQuery(key + ".keyword", pageModel.getInSearchCondition().get(key));
            ptermsBoolQueryBuilder.should(termQueryBuilder2);
            boolBuilder.must(ptermsBoolQueryBuilder);
        }
        // not in 查询
        for (String key : pageModel.getNotInSearchCondition().keySet()) {
//            ptermsBoolQueryBuilder.mustNot(QueryBuilders.termQuery(key, pageModel.getNotInSearchCondition().get(key)));
            BoolQueryBuilder ptermsBoolQueryBuilder = QueryBuilders.boolQuery();
            TermsQueryBuilder termQueryBuilder = QueryBuilders.termsQuery(key, pageModel.getNotInSearchCondition().get(key));
            ptermsBoolQueryBuilder.should(termQueryBuilder);
            TermsQueryBuilder termQueryBuilder2 = QueryBuilders.termsQuery(key + ".keyword", pageModel.getNotInSearchCondition().get(key));
            ptermsBoolQueryBuilder.should(termQueryBuilder2);
            boolBuilder.mustNot(ptermsBoolQueryBuilder);

        }

        // 为空匹配
        for (String key : pageModel.getIsNullConditioin()) {
            ExistsQueryBuilder existsQueryBuilder = QueryBuilders.existsQuery(key);
            boolBuilder.mustNot(existsQueryBuilder);
        }
        // 非空匹配
        for (String key : pageModel.getIsNullConditioin()) {
            ExistsQueryBuilder existsQueryBuilder = QueryBuilders.existsQuery(key);
            boolBuilder.must(existsQueryBuilder);
        }

        // filter精准查询
        for (String key : pageModel.getEqualsFilterSearchCondition().keySet()) {
            Object filterValue = pageModel.getEqualsFilterSearchCondition().get(key);
            if (null == filterValue) {
                continue;
            }
            QueryBuilder termQueryBuilder = QueryBuilders.termQuery(key, filterValue);
            BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery().filter(termQueryBuilder);
            boolBuilder.must(filterQueryBuilder);
        }

        // 子元素精确搜索
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModel.getSubEqualsSearchCondition();
        if (subEqualsSearchCondition != null) {
            for (String key : subEqualsSearchCondition.keySet()) {
                Map<String, Object> map = subEqualsSearchCondition.get(key);
                if (map != null) {
                    for (String subKey : map.keySet()) {
                        Object value = map.get(subKey);
                        QueryBuilder termQueryBuilder = QueryBuilders.termQuery(key + "." + subKey, value);
                        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(termQueryBuilder);
                        NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(key, boolQueryBuilder, ScoreMode.Total);
                        boolBuilder.must(nestedQueryBuilder);
                    }
                }
            }
        }

        // 子元素嵌套inFilter查询
        Map<String, Map<String, Object>> subInFilterSearchCondition = pageModel.getSubInFilterSearchCondition();
        if (subInFilterSearchCondition != null) {
            for (String key : subInFilterSearchCondition.keySet()) {
                Map<String, Object> map = subInFilterSearchCondition.get(key);
                if (map != null) {
                    for (String subKey : map.keySet()) {
                        Object value = map.get(subKey);
                        QueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(key + "." + subKey, (List<Long>) value);
                        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(termsQueryBuilder);
                        NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(key, boolQueryBuilder, ScoreMode.Total);
                        boolBuilder.must(nestedQueryBuilder);
                    }
                }
            }
        }

        Map<String, Map<String, Object[]>> subInSearchCondition = pageModel.getSubInSearchCondition();
        if (subEqualsSearchCondition != null) {
            for (String key : subInSearchCondition.keySet()) {
                Map<String, Object[]> map = subInSearchCondition.get(key);
                if (map != null) {
                    for (String subKey : map.keySet()) {
                        Object[] value = map.get(subKey);
                        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                        for (Object obj : value) {
                            QueryBuilder termQueryBuilder = QueryBuilders.termQuery(key + "." + subKey, obj);
                            boolQueryBuilder.should(termQueryBuilder);
                        }
                        NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(key, boolQueryBuilder, ScoreMode.Total);
                        boolBuilder.must(nestedQueryBuilder);
                    }
                }
            }
        }

        return boolBuilder;
    }

    /**
     * 区间查询
     *
     * @param pageModel
     * @param boolBuilder
     * @return
     */
    private BoolQueryBuilder sectionSearch(PageModelDTO pageModel, BoolQueryBuilder boolBuilder) {
        // 时间区间查询
        for (String key : pageModel.getRangConditionsToTimeModelMap().keySet()) {
            RangConditionsToTimeModelDTO rangConditionsToTimeModelMap = pageModel.getRangConditionsToTimeModelMap(key);
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key);
            if (null != rangConditionsToTimeModelMap.getBeginTime()) {
                rangeQueryBuilder.gte(rangConditionsToTimeModelMap.getBeginTime().getTime());
            }
            if (null != rangConditionsToTimeModelMap.getEndTime()) {
                rangeQueryBuilder.lte(rangConditionsToTimeModelMap.getEndTime().getTime());
            }

            // 包括下界
            rangeQueryBuilder.includeLower(true);
            // 包括上界
            rangeQueryBuilder.includeUpper(false);
            boolBuilder.must(rangeQueryBuilder);
            log.debug("rangeQueryBuilder:" + rangeQueryBuilder);

        }

        // 其他数据区间查询
        for (String key : pageModel.getRangConditionMap().keySet()) {
            RangConditionDTO rangConditionMap = pageModel.getRangConditionMap(key);
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key);
            if (StringUtils.isNotBlank(rangConditionMap.getBeginValue())) {
                rangeQueryBuilder.gte(rangConditionMap.getBeginValue());
            }
            if (StringUtils.isNotBlank(rangConditionMap.getEndValue())) {
                rangeQueryBuilder.lte(rangConditionMap.getEndValue());

            }
            // 包括下界
            rangeQueryBuilder.includeLower(true);
            // 包括上界
            rangeQueryBuilder.includeUpper(false);
            boolBuilder.must(rangeQueryBuilder);
            log.debug("rangeQueryBuilder:" + rangeQueryBuilder);
        }
        return boolBuilder;
    }

    /**
     * 模糊匹配
     *
     * @param pageModel
     * @param boolBuilder
     * @return
     */
    private BoolQueryBuilder likeSearch(PageModelDTO pageModel, BoolQueryBuilder boolBuilder) {
        // 模糊匹配查询
        for (String key : pageModel.getLikeSearchCondition().keySet()) {
            Object machValue = pageModel.getLikeSearchCondition().get(key);
            if (null == machValue) {
                continue;
            }
            BoolQueryBuilder wildcardBoolQueryBuilder = QueryBuilders.boolQuery();
            for (String value : machValue.toString().split(ElasticSearchConstant.SPLIT_FLAG)) {

                WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(key, "*" + value + "*");
                wildcardBoolQueryBuilder.should(wildcardQueryBuilder);
            }
            boolBuilder.must(wildcardBoolQueryBuilder);
        }
        // 精确匹配查询
        for (String key : pageModel.getEqualsSearchCondition().keySet()) {
            Object machValue = pageModel.getEqualsSearchCondition().get(key);
            if (null == machValue) {
                continue;
            }
            BoolQueryBuilder ptermBoolQueryBuilder = QueryBuilders.boolQuery();
            filterQuery(key, machValue, ptermBoolQueryBuilder);
            boolBuilder.must(ptermBoolQueryBuilder);

        }
        // 精确过滤查询
        for (String key : pageModel.getNoEqualsSearchConditioin().keySet()) {
            Object noMachValue = pageModel.getNoEqualsSearchConditioin().get(key);
            if (null == noMachValue) {
                continue;
            }
            BoolQueryBuilder noBoolQueryBuilder = QueryBuilders.boolQuery();

            filterQuery(key, noMachValue, noBoolQueryBuilder);

            boolBuilder.mustNot(noBoolQueryBuilder);
        }
        //模糊过滤查询
        for (String key : pageModel.getNoLikeSearchConditioin().keySet()) {
            Object noMachValue = pageModel.getNoLikeSearchConditioin().get(key);
            BoolQueryBuilder noBoolQueryBuilder = QueryBuilders.boolQuery();
            if (null == noMachValue) {
                continue;
            }
            for (String value : noMachValue.toString().split(ElasticSearchConstant.SPLIT_FLAG)) {
                WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(key, value);
                noBoolQueryBuilder.should(wildcardQueryBuilder);
            }
            boolBuilder.mustNot(noBoolQueryBuilder);

        }
        return boolBuilder;
    }


    /**
     * 或条件拼接
     *
     * @param orSearchCondition
     * @param termBoolQueryBuilder
     */
    private void filterOrQuery(Map<String, Object> orSearchCondition, BoolQueryBuilder termBoolQueryBuilder) {
        for (String key : orSearchCondition.keySet()) {
            Object value = orSearchCondition.get(key);
            if (null == value) {
                continue;
            }

            // 判断object是不是RangConditionDTO 范围对象
            if (value instanceof RangConditionDTO) {
                RangConditionDTO rangConditionDTO = (RangConditionDTO) value;
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key);
                if (StringUtils.isNotBlank(rangConditionDTO.getBeginValue())) {
                    rangeQueryBuilder.gte(rangConditionDTO.getBeginValue());
                }
                if (StringUtils.isNotBlank(rangConditionDTO.getEndValue())) {
                    rangeQueryBuilder.lte(rangConditionDTO.getEndValue());

                }
                // 包括下界
                rangeQueryBuilder.includeLower(true);
                // 包括上界
                rangeQueryBuilder.includeUpper(false);
                termBoolQueryBuilder.should(rangeQueryBuilder);
            } else {
                BoolQueryBuilder sueryBuilder = QueryBuilders.boolQuery();
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key + ".keyword", value);
                sueryBuilder.should(termQueryBuilder);
                TermQueryBuilder longtermQueryBuilder = QueryBuilders.termQuery(key, value);
                sueryBuilder.should(longtermQueryBuilder);
                termBoolQueryBuilder.should(sueryBuilder);
            }
        }
    }

    /**
     * 非条件拼接
     *
     * @param key
     * @param noMachValue
     * @param noBoolQueryBuilder
     */
    private void filterQuery(String key, Object noMachValue, BoolQueryBuilder noBoolQueryBuilder) {
        for (String cv : noMachValue.toString().split(ElasticSearchConstant.SPLIT_FLAG)) {
            BoolQueryBuilder termBoolQueryBuilder = QueryBuilders.boolQuery();
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key + ".keyword", cv);
            termBoolQueryBuilder.should(termQueryBuilder);
            TermQueryBuilder longtermQueryBuilder = QueryBuilders.termQuery(key, cv);
            termBoolQueryBuilder.should(longtermQueryBuilder);
            noBoolQueryBuilder.should(termBoolQueryBuilder);
        }
    }

    /**
     * 判断索引是否存在（ES7.X）
     *
     * @param indexName: 索引名称
     * @return 是否存在结果
     * @date 2019/12/10 17:01
     * @author lixiangx@leimingtech.com
     **/
    public boolean isIndexExists(String indexName) {
        boolean exists = false;
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            getIndexRequest.humanReadable(true);
            exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("判断索引:{} 是否存在异常，异常内容:{}", indexName, e);
        }
        return exists;
    }

    /**
     * 根据信息自动创建索引与mapping
     * 构建mapping描述
     *
     * @param indexName        索引名称
     * @param fieldMappingList 字段信息
     * @return 创建结果
     */
    private boolean createIndexAndCreateMapping(String indexName, List<FieldMapping> fieldMappingList) {
        try {
            // 开始封装ES索引的Mapping
            XContentBuilder mapping = packESMapping(fieldMappingList, null);
            mapping.endObject().endObject();
            mapping.close();

            // 进行索引的创建
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            // 商品索引需要指定ES解析器
            if (ElasticSearchConstant.GOODS_INDEX.equals(indexName)) {
                // 配置同义词
                XContentBuilder setting = packSynonymMapping();
                createIndexRequest.settings(setting);
            } else {
                XContentBuilder setting = XContentFactory.jsonBuilder().startObject().field("number_of_shards", 5).field("number_of_replicas", 2).endObject();
                createIndexRequest.settings(setting);
            }
            createIndexRequest.mapping(mapping);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            if (acknowledged) {
                log.info("索引:{}创建成功", indexName);
                return true;
            } else {
                log.error("索引:{}创建失败", indexName);
                return false;
            }
        } catch (IOException e) {
            log.error("创建索引：{}，出现异常：{}", indexName, e);
            return false;
        }
    }

    /**
     * 封装同义词的Builder
     *
     * @date 2019/12/18 14:45
     * @author lixiangx@leimingtech.com
     **/
    private XContentBuilder packSynonymMapping() throws IOException {
        XContentBuilder setting = XContentFactory.jsonBuilder().startObject().field("number_of_shards", 5).field("number_of_replicas", 2).startObject("analysis");

        setting.startObject("filter");
        //同义词的过滤器
        setting.startObject("net_synonym").field("type", "dynamic_synonym")
                .field("synonyms_path", SYNONYMS_PATH)
                .field("interval", 600).endObject();
        setting.endObject();
        //ik分词器、同义词、
        setting.startObject("analyzer");
        setting.startObject("ik-index").field("type", "custom")
                .field("tokenizer", "ik_max_word")
                .field("filter", new String[]{"net_synonym"})
                .field("char_filter", new String[]{"html_strip"}).endObject();
        setting.startObject("ik-search").field("type", "custom")
                .field("tokenizer", "ik_smart")
                .field("filter", new String[]{"net_synonym"})
                .field("char_filter", new String[]{"html_strip"}).endObject();

        // 自定义分析器 设置拼音分析器
//        setting.startObject("pinyin_analyzer").field("tokenizer", "leiming_pinyin").endObject();
        setting.endObject();
        // 自定义分词器  拼音分词器
//        setting.startObject("tokenizer").startObject("leiming_pinyin")
//                .field("type", "pinyin")
//                .field("keep_first_letter", true)
//                .field("keep_separate_first_letter", true)
//                .field("keep_full_pinyin", true)
//                .field("keep_original", true)
//                .field("limit_first_letter_length", 16)
//                .field("lowercase", true)
//                .endObject().endObject();

        setting.endObject().endObject();
        setting.close();
        return setting;
    }

    /**
     * 获取XContentBuilder实体创建ES的Mapping
     *
     * @param fieldMappingList: 实体对象的类型集合
     * @param mapping:          XContentBuilder实体
     * @return XContentBuilder实体
     * @date 2019/12/12 16:50
     * @author lixiangx@leimingtech.com
     **/
    private XContentBuilder packESMapping(List<FieldMapping> fieldMappingList, XContentBuilder mapping) throws IOException {

        if (mapping == null) {
            // 如果对象是空，首次进入，设置开始节点
            mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties");
        }

        // 循环实体对象的类型集合封装ES的Mapping
        for (FieldMapping info : fieldMappingList) {
            String field = info.getField();
            String dateType = info.getType();

            // 类型为空默认设置为string
            if (StringUtils.isBlank(dateType)) {
                dateType = "string";
            }
            dateType = dateType.toLowerCase();
            int participle = info.getParticiple();
            int pinyin = info.getPinyin();

            if ("string".equals(dateType)) {
                if (pinyin == 1) {
                    // 设置拼音Mapper
                    mapping.startObject(field)
                            .field("type", "text")
                            .field("analyzer", "ik_max_word")
                            .startObject("fields").startObject("s-pinyin")
                            .field("type", "completion")
                            .field("analyzer", "ik_max_word")
                            .endObject().endObject().endObject();
                } else {
                    // 设置分词规则
                    if (participle == 0) {
                        mapping.startObject(field)
                                .field("type", "keyword")
                                .endObject();
                    } else if (participle == 1) {
                        mapping.startObject(field)
                                .field("type", "text")
                                .field("analyzer", "ik_smart")
                                .endObject();
                    } else if (participle == 2) {
                        mapping.startObject(field)
                                .field("type", "text")
                                .field("analyzer", "ik_max_word")
                                .endObject();
                    } else if (participle == 3) {
                        mapping.startObject(field)
                                .field("type", "text")
                                .field("analyzer", "ik-index")
                                .field("search_analyzer", "ik-search")
                                .endObject();
                    }
                }
            } else if ("text".equals(dateType)) {
                mapping.startObject(field)
                        .field("type", dateType)
                        .startObject("fields").startObject("keyword")
                        .field("ignore_above", 256)
                        .field("type", "keyword")
                        .endObject().endObject().endObject();
            } else if ("date".equals(dateType)) {
                // 设置时间格式
                mapping.startObject(field)
                        .field("type", dateType)
                        .field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
                        .endObject();
            } else if ("float".equals(dateType) || "double".equals(dateType)) {
                // 设置小数类型
                mapping.startObject(field)
                        .field("type", "scaled_float")
                        .field("scaling_factor", 100)
                        .endObject();
            } else if ("nested".equals(dateType)) {
                // 设置聚合类型
                mapping.startObject(field)
                        .field("type", dateType)
                        .startObject("properties");
                // 由于nested类型内嵌文档，因此需要封装内嵌文档的Mapping
                mapping = packESMapping(info.getFieldMappingList(), mapping);
                // 设置尾部节点
                mapping.endObject().endObject();
            } else {
                // object类型
                if (info.getFieldMappingList() != null) {
                    // 内嵌文档循环封装（Java类对象中嵌套对象）
                    mapping.startObject(field).startObject("properties");
                    mapping = packESMapping(info.getFieldMappingList(), mapping);
                    mapping.endObject().endObject();
                } else {
                    // 常量类型配置
                    mapping.startObject(field)
                            .field("type", dateType)
                            .field("index", true)
                            .endObject();
                }
            }
        }

        return mapping;
    }


    /**
     * 创建mapping
     *
     * @param indexName 索引
     * @param clazz     索引类型
     */
    public boolean createIndexAndCreateMapping(String indexName, Class clazz, boolean dropOldIndex) {

        if (this.isIndexExists(indexName)) {
            if (dropOldIndex) {
                if (deleteIndex(indexName)) {
                    return createIndexAndCreateMapping(indexName, FieldMappingUtils.getFieldInfo(clazz));
                } else {
                    log.error("索引[{}]删除失败", indexName);
                    return false;
                }
            }
            log.info("索引[{}]已经存在，忽略创建索引", indexName);
            return true;
        } else {
            return createIndexAndCreateMapping(indexName, FieldMappingUtils.getFieldInfo(clazz));
        }
    }

    /**
     * 获取批量操作的Request
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @return BulkRequest对象
     * @date 2019/12/10 18:53
     * @author lixiangx@leimingtech.com
     **/
    private BulkRequest packBulkIndexRequest(String indexName, String primaryKeyName, String paramListJson) {
        BulkRequest bulkRequest = new BulkRequest();
        JSONArray jsonArray = JSONArray.parseArray(paramListJson);

        if (jsonArray == null) {
            return bulkRequest;
        }

        // 循环数据封装bulkRequest
        jsonArray.forEach(obj -> {
            Map<String, Object> map = (Map<String, Object>) obj;
            IndexRequest indexRequest = new IndexRequest(indexName);
            indexRequest.id(String.valueOf(map.get(primaryKeyName)));
            indexRequest.source(JSON.toJSONString(obj), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        return bulkRequest;
    }


    /**
     * 获取批量操作的Request
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @return BulkRequest对象
     * @date 2019/12/10 18:53
     * @author lixiangx@leimingtech.com
     **/
    private BulkRequest packBulkUpdateRequest(String indexName, String primaryKeyName, String paramListJson) {
        BulkRequest bulkRequest = new BulkRequest();
        JSONArray jsonArray = JSONArray.parseArray(paramListJson);
        if (jsonArray == null && jsonArray.size() == 0) {
            return bulkRequest;
        }

        jsonArray.forEach(obj -> {
            Map<String, Object> map = (Map<String, Object>) obj;
            UpdateRequest updateRequest = new UpdateRequest(indexName, String.valueOf(map.get(primaryKeyName)));
            // 如果修改索引中不存在则进行新增
            updateRequest.docAsUpsert(true);
            updateRequest.doc(JSON.toJSONString(obj), XContentType.JSON);
            bulkRequest.add(updateRequest);
        });
        return bulkRequest;
    }

    /**
     * 获取批量操作的实体
     *
     * @param indexName: 索引名称
     * @param ids:       主键ID集合
     * @return BulkRequest批量操作对象
     * @date 2019/12/10 20:53
     * @author lixiangx@leimingtech.com
     **/
    private BulkRequest packBulkDeleteRequest(String indexName, List<Long> ids) {
        BulkRequest bulkRequest = new BulkRequest();

        ids.forEach(id -> {
            DeleteRequest deleteRequest = new DeleteRequest(indexName);
            deleteRequest.id(String.valueOf(id));
            bulkRequest.add(deleteRequest);
        });
        return bulkRequest;
    }

}
