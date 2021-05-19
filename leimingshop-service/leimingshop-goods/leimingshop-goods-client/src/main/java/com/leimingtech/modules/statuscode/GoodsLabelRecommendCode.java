/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 标签推荐状态码
 *
 * @Data: 2019/7/9 10:23
 * @Author: chengqian
 * @Version: 1.0
 */
public class GoodsLabelRecommendCode extends ServiceStatusCode {

    /**
     * 分页查询商品标签推荐
     */
    public static final String LABEL_RECOMMEND_PAGE_SUCCESS_CODE = "200501";
    public static final String LABEL_RECOMMEND_PAGE_SUCCESS_MESSAGE = "分页查询商品标签推荐";

    /**
     * 查询未关联商品信息并分页
     */
    public static final String LABEL_RECOMMEND_LIST_SUCCESS_CODE = "200521";
    public static final String LABEL_RECOMMEND_LIST_SUCCESS_MESSAGE = "查询未关联商品信息并分页";

    /**
     * 根据id查询商品标签推荐
     */
    public static final String LABEL_RECOMMEND_BY_ID_SUCCESS_CODE = "200522";
    public static final String LABEL_RECOMMEND_BY_ID_SUCCESS_MESSAGE = "根据id查询商品标签推荐信息";

    /**
     * 根据标签推荐id查询关联商品信息
     */
    public static final String LABEL_RECOMMEND_BY_ID_REL_SUCCESS_CODE = "200532";
    public static final String LABEL_RECOMMEND_BY_ID_REL_SUCCESS_MESSAGE = "根据标签推荐id查询关联商品信息";

    /**
     * 保存商品标签推荐
     */
    public static final String LABEL_RECOMMEND_SAVE_SUCCESS_CODE = "200502";
    public static final String LABEL_RECOMMEND_SAVE_SUCCESS_MESSAGE = "保存商品标签推荐";

    /**
     * 批量保存商品关联信息
     */
    public static final String LABEL_RECOMMEND_BATCH_SAVE_SUCCESS_CODE = "200512";
    public static final String LABEL_RECOMMEND_BATCH_SAVE_SUCCESS_MESSAGE = "批量保存商品关联信息";

    /**
     * 商品标签推荐修改
     */
    public static final String LABEL_RECOMMEND_UPDATE_CODE = "200503";
    public static final String LABEL_RECOMMEND_UPDATE_MESSAGE = "商品标签推荐修改";

    /**
     * 标签推荐修改启用禁用状态
     */
    public static final String LABEL_RECOMMEND_UPDATE_STATUS_CODE = "200513";
    public static final String LABEL_RECOMMEND_UPDATE_STATUS_MESSAGE = "标签推荐修改启用禁用状态";
    /**
     * 商品标签推荐删除
     */
    public static final String LABEL_RECOMMEND_DELETE_CODE = "200504";
    public static final String LABEL_RECOMMEND_DELETE_MESSAGE = "商品标签推荐删除";

    /**
     * 根据商品id删除标签关联的商品信息
     */
    public static final String LABEL_RECOMMEND_REL_DELETE_CODE = "200514";
    public static final String LABEL_RECOMMEND_REL_DELETE_MESSAGE = "根据商品id删除标签关联的商品信息";


    protected GoodsLabelRecommendCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
