//package com.leimingtech.modules.utils;
//
//import com.leimingtech.logging.MonitorLogger;
//import com.leimingtech.logging.MonitorLoggerFactory;
//import com.leimingtech.modules.constant.IndexSyncServiceCode;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
//import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
//import org.elasticsearch.action.support.IndicesOptions;
//import org.elasticsearch.action.support.master.AcknowledgedResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.Requests;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.elasticsearch.client.indices.CreateIndexResponse;
//import org.elasticsearch.client.indices.GetIndexRequest;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.elasticsearch.common.xcontent.XContentFactory;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @author Created by kuangweiguo on 2019/06/28.
// */
//@Slf4j
//public class ElasticSearchHandler {
//
//
//    private static final MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(ElasticSearchHandler.class);
//
//
//    private static List<FieldMapping> getFieldInfo(Class clazz) {
//        return getFieldInfo(clazz, null);
//    }
//
//    private static List<FieldMapping> getFieldInfo(Class clazz, String fieldName) {
//
//
//        Field[] fields = clazz.getDeclaredFields();
//        List<FieldMapping> fieldMappingList = new ArrayList<>();
//        for (Field field : fields) {
//
//            FieldInfo fieldInfo = field.getAnnotation(FieldInfo.class);
//            if (fieldInfo == null) {
//                continue;
//            }
//            if ("object".equals(fieldInfo.type())) {
//                Class fc = field.getType();
//                if (fc.isPrimitive()) {
//                    String name = field.getName();
//                    if (StringUtils.isNotBlank(fieldName)) {
//                        name = name + "." + fieldName;
//                    }
//                    fieldMappingList.add(new FieldMapping(name, fieldInfo.type(), fieldInfo.participle()));
//                } else {
//                    if (fc.isAssignableFrom(List.class)) { //???????????????List
//                        System.out.println("List?????????" + field.getName());
//                        Type gt = field.getGenericType();    //??????????????????
//                        ParameterizedType pt = (ParameterizedType) gt;
//                        Class lll = (Class) pt.getActualTypeArguments()[0];
//                        fieldMappingList.addAll(getFieldInfo(lll, field.getName()));
//                    } else {
//                        fieldMappingList.addAll(getFieldInfo(fc, field.getName()));
//                    }
//                }
//
//            } else {
//                String name = field.getName();
//                if (StringUtils.isNotBlank(fieldName)) {
//                    name = fieldName + "." + name;
//                }
//                fieldMappingList.add(new FieldMapping(name, fieldInfo.type(), fieldInfo.participle()));
//            }
//
//        }
//        return fieldMappingList;
//
//    }
//
//    /**
//     * ??????mapping
//     *
//     * @param index  ??????
//     * @param type   ??????
//     * @param clazz  ????????????
//     * @param client es?????????
//     */
//    public static boolean createIndexAndCreateMapping(String index, String type, Class clazz, RestHighLevelClient client, boolean dropOldIndex) {
//
//        if (isIndexExists(index, client)) {
//            if (dropOldIndex) {
//                if (deleteIndex(index, client)) {
//                    return createIndexAndCreateMapping(index, type, getFieldInfo(clazz), client);
//                } else {
//                    logger.error(IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getCode(), IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getMessage());
//                    return false;
//                }
//            }
//            logger.info(IndexSyncServiceCode.CREATE_INDEX_EXISTS.getCode(), IndexSyncServiceCode.CREATE_INDEX_EXISTS.getMessage());
//            return true;
//        } else {
//            return createIndexAndCreateMapping(index, type, getFieldInfo(clazz), client);
//        }
//    }
//
//    /**
//     * ????????????????????????
//     *
//     * @param indexName: ????????????
//     * @param client:    ?????????
//     * @date 2019/11/16 18:18
//     * @author lixiangx@leimingtech.com
//     **/
//    public static boolean isIndexExists(String indexName, RestHighLevelClient client) {
//        boolean exists = false;
//        try {
//            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
//            getIndexRequest.humanReadable(true);
//            exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            log.error("??????????????????????????????:{}", e);
//        }
//        return exists;
//    }
//
//
//    /**
//     * ????????????
//     *
//     * @param indexName
//     * @return
//     */
//    public static boolean deleteIndex(String indexName, RestHighLevelClient client) {
//        boolean acknowledged = false;
//        try {
//            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
//            deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
//            AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//            acknowledged = delete.isAcknowledged();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return acknowledged;
//    }
//
//    /**
//     * ??????mapping
//     *
//     * @param index  ??????
//     * @param type   ??????
//     * @param clazz  ????????????
//     * @param client es?????????
//     */
//    public static boolean createIndexAndCreateMapping(String index, String type, Class clazz, RestHighLevelClient client) {
//        return createIndexAndCreateMapping(index, type, getFieldInfo(clazz), client);
//    }
//
//    /**
//     * ??????mapping
//     *
//     * @param index    ??????
//     * @param type     ??????
//     * @param client   es?????????
//     * @param xMapping mapping??????
//     */
//    public static void createBangMapping(String index, String type, XContentBuilder xMapping, TransportClient client) {
//        PutMappingRequest mapping = Requests.putMappingRequest(index).type(type).source(xMapping);
//        client.admin().indices().putMapping(mapping).actionGet();
//    }
//
//    /**
//     * ?????????????????????????????????mapping
//     * ??????mapping??????
//     *
//     * @param fieldMappingList ????????????
//     * @param client           es?????????
//     * @return
//     */
//    public static boolean createIndexAndCreateMapping(String index, String type, List<FieldMapping> fieldMappingList, RestHighLevelClient client) {
//        XContentBuilder mapping = null;
//        try {
//            mapping = XContentFactory.jsonBuilder()
//                    .startObject()
//                    .startObject("properties"); //?????????????????????
//            for (FieldMapping info : fieldMappingList) {
//                String field = info.getField();
//                String dateType = info.getType();
//                if (dateType == null || "".equals(dateType.trim())) {
//                    dateType = "string";
//                }
//                dateType = dateType.toLowerCase();
//                int participle = info.getParticiple();
//                if ("string".equals(dateType)) {
//                    if (participle == 0) {
//                        mapping.startObject(field)
//                                .field("type", "keyword")
////                                .field("index","not_analyzed")
//                                .endObject();
//                    } else if (participle == 1) {
//                        mapping.startObject(field)
//                                .field("type", "text")
//                                .field("analyzer", "ik_smart")
//                                .endObject();
//                    } else if (participle == 2) {
//                        mapping.startObject(field)
//                                .field("type", "text")
//                                .field("analyzer", "ik_max_word")
//                                .endObject();
//                    }
//
//                } else if ("date".equals(dateType)) {
//                    mapping.startObject(field)
//                            .field("type", dateType)
//                            .field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
//                            .endObject();
//                } else if ("float".equals(dateType) || "double".equals(dateType)) {
//                    mapping.startObject(field)
//                            .field("type", "scaled_float")
//                            .field("scaling_factor", 100)
//                            .endObject();
//                } else {
//                    mapping.startObject(field)
//                            .field("type", dateType)
//                            .field("index", true)
//                            .endObject();
//                }
//
//            }
//            mapping.endObject()
//                    .endObject();
//            // ????????????type
//            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
//            createIndexRequest.mapping(mapping);
//            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//            boolean acknowledged = createIndexResponse.isAcknowledged();
//            if (acknowledged) {
//                log.info("??????:{}????????????", index);
//                return true;
//            } else {
//                log.error("??????:{}????????????", index);
//                return false;
//            }
//        } catch (IOException e) {
//            logger.error(IndexSyncServiceCode.CREATE_INDEX_ERROR.getCode(), IndexSyncServiceCode.CREATE_INDEX_ERROR.getMessage(), e);
//            return false;
//        }
//    }
//
//
//}