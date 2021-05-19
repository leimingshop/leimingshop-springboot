/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto;

import lombok.Data;
import org.elasticsearch.search.SearchHit;

import java.io.Serializable;
import java.util.*;

/**
 * 分页模型
 *
 * @author kviuff
 * @version 1.0
 * @date 2018/12/03
 */
@Data
public class PageModelDTO implements Serializable {

    private static final long serialVersionUID = 192274358354304398L;
    /**
     * 数据总条数
     */
    private Long total;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 条数
     */
    private Integer pageSize = 10;

    /**
     * 界定返回值类型 json;obj;
     */
    private String resultType = "string";

    /**
     * 是否分页
     */
    private Boolean isPage = true;

    /**
     * json格式返回值
     */
    private List<String> jsonRsList = new ArrayList<String>();

    /**
     * 直接从ES获取的结果
     */
    private SearchHit[] searchHits;

    /**
     * 区间查询参数
     */
    private Map<String, RangConditionDTO> rangConditionMap = new HashMap<>();

    /**
     * 时间区间查询参数
     */
    private Map<String, RangConditionsToTimeModelDTO> rangConditionsToTimeModelMap = new HashMap<>();

    /**
     * 模糊相等查询条件，多个查询条件","进行切割
     */
    private Map<String, Object> likeSearchCondition = new HashMap<>();

    /**
     * or条件查询
     */
    private Map<String, Object> orSearchCondition = new HashMap<>();

    /**
     * or条件查询
     */
    private Map<String, Object> orLikeSearchCondition = new HashMap<>();

    /**
     * or条件查询集合类操作
     */
    private List<Map<String, Object>> orSearchConditionList = new ArrayList<>();

    /**
     * 相等查询条件，多个查询条件","进行切割
     */
    private Map<String, Object> equalsSearchCondition = new HashMap<>();

    /**
     * 过滤相等查询条件，多个查询条件","进行切割
     */
    private Map<String, Object> equalsFilterSearchCondition = new HashMap<>();

    /**
     * 功能描述:
     * 〈子元素紧缺查询〉
     *
     * @param null
     * @author : 刘远杰
     */
    private Map<String, Map<String, Object>> subEqualsSearchCondition = new HashMap<>();

    /**
     * 功能描述：
     * 子元素嵌套in查询
     *
     * @author 宋文豪
     * @email: songwenhao@leimingtech.com
     * @Date : 2020/3/13
     **/
    private Map<String, Map<String, Object>> subInFilterSearchCondition = new HashMap<>();

    /**
     * 功能描述:
     * 〈子元素紧缺查询〉
     *
     * @param null
     * @author : 刘远杰
     */
    private Map<String, Map<String, Object[]>> subInSearchCondition = new HashMap<>();

    /**
     * in 查询
     */
    private Map<String, List> inSearchCondition = new HashMap<>();

    /**
     * not in  查询
     */
    private Map<String, List> notInSearchCondition = new HashMap<>();

    /**
     * 模糊不相等的条件，多个查询条件","进行切割
     */
    private Map<String, Object> noLikeSearchConditioin = new HashMap<>();

    /**
     * 不相等的条件，多个查询条件","进行切割
     */
    private Map<String, Object> noEqualsSearchConditioin = new HashMap<>();

    /**
     * 为空过滤
     */
    private List<String> isNullConditioin = new ArrayList<>();

    /**
     * 不为空过滤
     */
    private List<String> isNotNullConditioin = new ArrayList<>();

    /**
     * 排序字段，关键字asc，desc
     */
    private Map<String, String> sortFileds = new LinkedHashMap<>();

    /**
     * 排序字段集合，方便对排序顺序的控制 关键字asc，desc
     */
    private List<Map<String, String>> sortFiledsList = new ArrayList<>();

    /**
     * 高亮字段
     */
    private List<String> hightFieldList = new ArrayList<>();

    /**
     * 去重字段
     */
    private String collapseField;

    /**
     * 指定查询结果包含的字段
     */
    private String[] fetchSourceIncludes;

    /**
     * 指定查询结果不包含的字段
     */
    private String[] fetchSourceExcludes;

    /**
     * 分词字段
     */
    private Map<String, Object> analyzersField = new HashMap<>();

    public String getSortFileds(String key) {
        return sortFileds.get(key);
    }

    public RangConditionDTO getRangConditionMap(String key) {
        return rangConditionMap.get(key);
    }

    public RangConditionsToTimeModelDTO getRangConditionsToTimeModelMap(String key) {
        return rangConditionsToTimeModelMap.get(key);
    }

    public void setJsonRsList(String json) {
        this.jsonRsList.add(json);
    }
}
