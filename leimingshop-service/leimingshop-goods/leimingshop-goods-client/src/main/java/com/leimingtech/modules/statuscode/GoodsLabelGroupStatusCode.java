/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 标签分组状态码
 *
 * @Data: 2019/7/9 10:23
 * @Author: chengqian
 * @Version: 1.0
 */
public class GoodsLabelGroupStatusCode extends ServiceStatusCode {


    /**
     * 分页查询标签分组
     */
    public static final String LABEL_GROUP_PAGE_SUCCESS_CODE = "200301";
    public static final String LABEL_GROUP_PAGE_SUCCESS_MESSAGE = "分页查询标签分组";

    /**
     * 查询标签分组列表
     */
    public static final String LABEL_GROUP_LIST_SUCCESS_CODE = "200321";
    public static final String LABEL_GROUP_LIST_SUCCESS_MESSAGE = "查询标签分组列表";

    /**
     * 根据id查询标签分组
     */
    public static final String LABEL_GROUP_BY_ID_SUCCESS_CODE = "200322";
    public static final String LABEL_GROUP_BY_ID_SUCCESS_MESSAGE = "根据id查询标签分组";

    /**
     * 保存标签分组
     */
    public static final String LABEL_GROUP_SAVE_SUCCESS_CODE = "200302";
    public static final String LABEL_GROUP_SAVE_SUCCESS_MESSAGE = "保存标签分组";

    /**
     * 修改标签分组
     */
    public static final String LABEL_GROUP_UPDATE_CODE = "200303";
    public static final String LABEL_GROUP_UPDATE_MESSAGE = "标签分组修改";

    /**
     * 标签分组状态修改
     */
    public static final String LABEL_GROUP_UPDATE_STATUS_CODE = "200313";
    public static final String LABEL_GROUP_UPDATE_STATUS_MESSAGE = "标签分组状态修改";
    /**
     * 标签分组删除
     */
    public static final String LABEL_GROUP_DELETE_CODE = "200304";
    public static final String LABEL_GROUP_DELETE_MESSAGE = "标签分组删除";


    protected GoodsLabelGroupStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
