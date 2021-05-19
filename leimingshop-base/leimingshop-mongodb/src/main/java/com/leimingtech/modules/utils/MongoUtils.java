/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils;

import cn.hutool.json.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MongoUtils {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * mongo根据传入参数精准查询数据
     *
     * @param collectionName 集合名
     * @return
     */
    public List<String> queryData(String collectionName, Map<String, Object> params) {

        BasicDBObject dbObject = this.getDBObject(params);
        return getDataList(collectionName, dbObject);
    }

    /**
     * @param collectionName:集合名称
     * @param query:              封装条件
     * @Author: SWH ab4856812@163.com
     * @Description: 组合查询（查询分页数据，增加排序等）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public List<String> composeQueryData(String collectionName, Query query) {
        List<String> resultList = new ArrayList<>();
        List<Map> result = mongoTemplate.find(query, Map.class, collectionName);
        for (Map map : result) {
            String json = JSONUtil.toJsonStr(map);
            resultList.add(json);
        }
        return resultList;
    }

    /**
     * @param collectionName:集合名称
     * @param dbObject:           封装条件
     * @Author: SWH ab4856812@163.com
     * @Description: 私有方法（获取查询数据）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    private List<String> getDataList(String collectionName, BasicDBObject dbObject) {
        List<String> resultList = new ArrayList<>();
        FindIterable<Map> result = mongoTemplate.getCollection(collectionName).find(dbObject, Map.class);
        for (Map map : result) {
            String json = JSONUtil.toJsonStr(map);
            resultList.add(json);
        }
        return resultList;
    }

    /**
     * @param collectionName:集合名称
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 增加数据（单个保存）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public boolean saveData(String collectionName, Map<String, Object> params) {
        mongoTemplate.save(params, collectionName);
        return true;
    }

    /**
     * @param collectionName:集合名称
     * @param primaryKey:根据某字段查询
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 增加数据批量（增加了判断是否重复）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public boolean saveDataBatch(String collectionName, String primaryKey, Map<String, Object> params) {

        // 封装查询条件
        Document filter = new Document();
        filter.put(primaryKey, params.get(primaryKey));

        // 封装要更新的数据
        Document insertData = new Document();
        for (String keyParamters : params.keySet()) {
            insertData.put(keyParamters, params.get(keyParamters));
        }
        // 设置要执行的方法
        Document finalData = new Document();
        finalData.append("$setOnInsert", insertData);

        mongoTemplate.getCollection(collectionName).updateMany(filter, finalData, new UpdateOptions().upsert(true));
        return true;
    }


    /**
     * 批量保存数据（仅保存）
     *
     * @param collectionName : 集合名称
     * @param dataList       : 数据集合(为了解耦，使用Object类型接收参数)
     * @date 2019/12/9 20:27
     * @author lixiangx@leimingtech.com
     **/
    public void saveBatch(List<? extends Object> dataList, String collectionName) {

        mongoTemplate.insert(dataList, collectionName);
    }

    /**
     * @param collectionName:集合名称
     * @param primaryKey:根据某字段查询
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 修改数据
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public boolean updateData(String collectionName, String primaryKey, Map<String, Object> params) {

        // 封装查询条件
        Document filter = new Document();
        filter.put(primaryKey, params.get(primaryKey));

        // 封装要更新的数据
        Document update = new Document();
        for (String keyParamters : params.keySet()) {
            if (!keyParamters.equals(primaryKey)) {
                update.put(keyParamters, params.get(keyParamters));
            }
        }
        // 设置要执行的方法
        Document finalData = new Document();
        finalData.append("$set", update);

        mongoTemplate.getCollection(collectionName).updateMany(filter, finalData, new UpdateOptions().upsert(true));
        return true;
    }

    /**
     * @param collectionName: 集合名
     * @param params:         要删除的参数
     * @Author: SWH ab4856812@163.com
     * @Description: 根据传入参数删除mongo数据
     * @Date: 2019/8/2 16:45
     * @Version: V1.0
     */
    public boolean deleteData(String collectionName, Map<String, Object> params) {

        BasicDBObject dbObject = this.getDBObject(params);
        mongoTemplate.getCollection(collectionName).deleteOne(dbObject);
        return true;
    }

    /**
     * @param params: 要封装的参数
     * @Author: SWH ab4856812@163.com
     * @Description: 私有方法（根据传入参数封装mongo需要的DBObject实体）
     * @Date: 2019/8/2 16:44
     * @Version: V1.0
     */
    private BasicDBObject getDBObject(Map<String, Object> params) {
        BasicDBObject DBObject = new BasicDBObject();
        for (String key : params.keySet()) {
            Object machValue = params.get(key);
            if (null != machValue) {
                DBObject.put(key, machValue);
            }
        }
        return DBObject;
    }

    /**
     * @param collectionName: 集合名称
     * @Author: SWH ab4856812@163.com
     * @Description: 获取数据
     * @Date: 2019/8/2 18:21
     * @Version: V1.0
     */
    public Long getCount(String collectionName, Query query) {
        return mongoTemplate.count(query, collectionName);
    }

    /**
     * 批量保存数据（仅保存）
     *
     * @param collectionName : 集合名称
     * @param object         : 要保存的对象
     * @date 2019年12月11日18:06:30
     * @author xuzhch
     **/
    public void saveObj(Object object, String collectionName) {
        mongoTemplate.insert(object, collectionName);
    }
}
