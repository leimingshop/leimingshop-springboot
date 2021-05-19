/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.page;

import com.leimingtech.modules.dto.BaseModelDTO;
import lombok.Data;

import java.util.*;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 基础分页模型
 * @Date: 2019/8/2 10:51
 * @Version: V1.0
 */
@Data
public class PageModelDTO extends BaseModelDTO {

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
     * 界定返回值类型
     * json;obj;
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
     * in 查询
     */
    private Map<String, List> inSearchCondition = new HashMap<>();
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
     * 高亮字段
     */
    private List<String> hightFieldList = new ArrayList<>();

    /**
     * 分词字段
     */
    private Map<String, Object> analyzersField = new HashMap<>();

    public String getSortFileds(String key) {
        return sortFileds.get(key);
    }

}
