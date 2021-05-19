/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 品牌状态码
 *
 * @Data: 2019/7/9 10:23
 * @Author: chengqian
 * @Version: 1.0
 */
public class GoodsBrandStatusCode extends ServiceStatusCode {


    /**
     * 分页查询品牌
     */
    public static final String BRAND_PAGE_SUCCESS_CODE = "200101";
    public static final String BRAND_PAGE_SUCCESS_MESSAGE = "分页查询品牌";

    /**
     * 查询品牌列表
     */
    public static final String BRAND_LIST_SUCCESS_CODE = "200121";
    public static final String BRAND_LIST_SUCCESS_MESSAGE = "查询品牌列表";

    /**
     * 根据id查询品牌
     */
    public static final String BRAND_BY_ID_SUCCESS_CODE = "200122";
    public static final String BRAND_BY_ID_SUCCESS_MESSAGE = "根据id查询品牌";

    /**
     * 保存品牌
     */
    public static final String BRAND_SAVE_SUCCESS_CODE = "200102";
    public static final String BRAND_SAVE_SUCCESS_MESSAGE = "保存品牌";

    /**
     * 修改品牌
     */
    public static final String BRAND_UPDATE_CODE = "200103";
    public static final String BRAND_UPDATE_MESSAGE = "品牌修改";
    /**
     * 品牌删除
     */
    public static final String BRAND_DELETE_CODE = "200104";
    public static final String BRAND_DELETE_MESSAGE = "品牌删除";

    /**
     * 品牌列表导出
     */
    public static final String BRAND_EXPORT_CODE = "200105";
    public static final String BRAND_EXPORT_MESSAGE = "品牌列表导出";


    protected GoodsBrandStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
