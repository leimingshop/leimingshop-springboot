/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao;

import com.leimingtech.modules.dto.page.PageModelDTO;
import com.leimingtech.modules.utils.MongoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 抽象方法mongodb增删改查实现
 * @Date: 2019/8/1 15:28
 * @Version: V1.0
 */
@Component
public class MongoDao extends AbstractNosqlDao {

    @Autowired
    private MongoUtils mongoUtils;

    /**
     * @param collectionName:集合名称
     * @param params:传入参数，可多个
     * @Author: SWH ab4856812@163.com
     * @Description: 查询方法
     * @Date: 2019/8/1 14:49
     * @Version: V1.0
     */
    @Override
    public List<String> queryData(String collectionName, Map<String, Object> params) {

        return mongoUtils.queryData(collectionName, params);
    }

    /**
     * @param collectionName:集合名称
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 增加数据（单个保存）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    @Override
    public boolean saveData(String collectionName, Map<String, Object> params) {
        return mongoUtils.saveData(collectionName, params);
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
    @Override
    public boolean saveDataBatch(String collectionName, String primaryKey, Map<String, Object> params) {
        return mongoUtils.saveDataBatch(collectionName, primaryKey, params);
    }


    /**
     * 批量保存数据（仅保存）
     *
     * @param collectionName : 集合名称
     * @param dataList       : 数据集合(为了解耦，使用Object类型接收参数)
     * @date 2019/12/9 20:27
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public void saveBatch(List<? extends Object> dataList, String collectionName) {
        mongoUtils.saveBatch(dataList, collectionName);
    }

    /**
     * 批量保存数据（仅保存）
     *
     * @param collectionName : 集合名称
     * @param object         : 要保存的对象
     * @date 2019年12月11日18:06:30
     * @author xuzhch
     **/
    @Override
    public void saveObj(Object object, String collectionName) {
        mongoUtils.saveObj(object, collectionName);

    }

    /**
     * @param collectionName:集合名称
     * @param primaryKey:根据某字段查询
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 修改数据（单个和批量均支持）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    @Override
    public boolean updateData(String collectionName, String primaryKey, Map<String, Object> params) {
        return mongoUtils.updateData(collectionName, primaryKey, params);
    }

    /**
     * @param collectionName:集合名称
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 删除数据（单个删除）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    @Override
    public boolean deleteData(String collectionName, Map<String, Object> params) {
        return mongoUtils.deleteData(collectionName, params);
    }

    /**
     * @param collectionName:集合名称
     * @param pageModelDTO:       封装数据
     * @Author: SWH ab4856812@163.com
     * @Description: 组合查询（查询分页数据，增加排序等）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    @Override
    public PageModelDTO composeQueryData(String collectionName, PageModelDTO pageModelDTO) {

        //封装条件
        Query query = this.getQuery(pageModelDTO);
        // 获取总数
        Long count = mongoUtils.getCount(collectionName, query);
        pageModelDTO.setTotal(count);
        if (pageModelDTO.getIsPage()) {
            // 分页
            int pageStart = 0;
            int pageSize = 10000;
            pageSize = pageModelDTO.getPageSize();
            pageStart = (pageModelDTO.getPageNum() - 1) * pageSize;
            // 查询起始值
            query.skip(pageStart);
            // 查询大小
            query.limit(pageSize);
        }

        for (String sortKey : pageModelDTO.getSortFileds().keySet()) {
            if ("asc".equals(pageModelDTO.getSortFileds(sortKey))) {
                query.with(new Sort(new Sort.Order(Sort.Direction.ASC, sortKey)));
            } else if ("desc".equals(pageModelDTO.getSortFileds(sortKey))) {
                query.with(new Sort(new Sort.Order(Sort.Direction.DESC, sortKey)));
            }
        }

        List<String> dataList = mongoUtils.composeQueryData(collectionName, query);
        pageModelDTO.setJsonRsList(dataList);
        return pageModelDTO;
    }

    /**
     * 接接查询条件
     *
     * @param pageModel
     * @return
     */
    private Query getQuery(PageModelDTO pageModel) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        // 精确查询 and
        for (String key : pageModel.getEqualsSearchCondition().keySet()) {
            Object machValue = pageModel.getEqualsSearchCondition().get(key);
            if (null != machValue) {
                criteria.and(key).is(machValue);
            }
        }

        // 模糊匹配查询 and
        Map<String, Object> likeSearchCondition = pageModel.getLikeSearchCondition();
        Integer likeSearchConditionSize = likeSearchCondition.size();
        if (likeSearchConditionSize > 0) {
            Criteria[] LikeCriterias = new Criteria[likeSearchConditionSize];
            Integer likeCriteriasCount = 0;
            for (String key : likeSearchCondition.keySet()) {
                Object machValue = pageModel.getLikeSearchCondition().get(key);
                if (null != machValue) {
                    Pattern pattern = Pattern.compile("^.*" + machValue + ".*$", Pattern.CASE_INSENSITIVE);
                    LikeCriterias[likeCriteriasCount] = Criteria.where(key).regex(pattern);
                    likeCriteriasCount++;
                }
            }
            criteria.andOperator(LikeCriterias);
        }

        // or equals 拼接查询
        Map<String, Object> orSearchCondition = pageModel.getOrSearchCondition();
        Integer orSearchConditionSize = orSearchCondition.size();
        if (orSearchConditionSize > 0) {
            Criteria[] orSearchConditionCriteria = new Criteria[orSearchConditionSize];
            Integer orSearchConditionCriteriaCount = 0;
            for (String key : orSearchCondition.keySet()) {
                Object value = orSearchCondition.get(key);
                if (null != value) {
                    orSearchConditionCriteria[orSearchConditionCriteriaCount] = Criteria.where(key).is(value);
                    orSearchConditionCriteriaCount++;
                }
            }
            criteria.orOperator(orSearchConditionCriteria);
        }

        // or like 拼接查询
        Map<String, Object> orLikeSearchCondition = pageModel.getOrLikeSearchCondition();
        Integer orLikeSearchConditionSize = orLikeSearchCondition.size();
        if (orLikeSearchConditionSize > 0) {
            Criteria[] orLikeSearchConditionCriteria = new Criteria[orLikeSearchConditionSize];
            Integer orLikeSearchConditionCriteriaCount = 0;
            for (String key : orLikeSearchCondition.keySet()) {
                Object value = orLikeSearchCondition.get(key);
                if (null != value) {
                    Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                    orLikeSearchConditionCriteria[orLikeSearchConditionCriteriaCount] = Criteria.where(key).regex(pattern);
                    orLikeSearchConditionCriteriaCount++;
                }
            }
            criteria.orOperator(orLikeSearchConditionCriteria);
        }

        query.addCriteria(criteria);
        return query;
    }
}
