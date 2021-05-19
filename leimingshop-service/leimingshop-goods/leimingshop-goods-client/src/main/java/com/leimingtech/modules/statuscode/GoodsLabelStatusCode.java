/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 标签状态码
 *
 * @Data: 2019/7/9 10:23
 * @Author: chengqian
 * @Version: 1.0
 */
public class GoodsLabelStatusCode extends ServiceStatusCode {


    /**
     * 分页查询商品标签
     */
    public static final String LABEL_PAGE_SUCCESS_CODE = "200401";
    public static final String LABEL_PAGE_SUCCESS_MESSAGE = "分页查询商品标签";

    /**
     * 查询商品标签列表
     */
    public static final String LABEL_LIST_SUCCESS_CODE = "200421";
    public static final String LABEL_LIST_SUCCESS_MESSAGE = "查询商品标签列表";

    /**
     * 根据id查询商品标签
     */
    public static final String LABEL_BY_ID_SUCCESS_CODE = "200422";
    public static final String LABEL_BY_ID_SUCCESS_MESSAGE = "根据id查询商品标签";

    /**
     * 保存商品标签
     */
    public static final String LABEL_SAVE_SUCCESS_CODE = "200402";
    public static final String LABEL_SAVE_SUCCESS_MESSAGE = "保存商品标签";

    /**
     * 商品标签修改
     */
    public static final String LABEL_UPDATE_CODE = "200403";
    public static final String LABEL_UPDATE_MESSAGE = "商品标签修改";
    /**
     * 商品标签删除
     */
    public static final String LABEL_DELETE_CODE = "200404";
    public static final String LABEL_DELETE_MESSAGE = "商品标签删除";


    protected GoodsLabelStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
