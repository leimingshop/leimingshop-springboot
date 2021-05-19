/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 功能描述：
 * <运营模块日志结果码>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/11 16:03
 **/
public class OperationStatusCode extends ServiceStatusCode {


    /*========================================================== 移动首页菜单==================================================================*/
    /**
     * 移动首页菜单分页接口访问成功
     */
    public static final String MOBILE_INDEX_MENU_PAGE_SUCCESS_CODE = "200101";
    public static final String MOBILE_INDEX_MENU_PAGE_SUCCESS_MESSAGE = "移动首页菜单分页接口访问成功";

    /**
     * 移动首页菜单详情接口访问成功
     */
    public static final String MOBILE_INDEX_MENU_DETAIL_SUCCESS_CODE = "200102";
    public static final String MOBILE_INDEX_MENU_DETAIL_SUCCESS_MESSAGE = "移动首页菜单详情接口访问成功";

    /**
     * 移动首页菜单保存接口访问成功
     */
    public static final String MOBILE_INDEX_MENU_SAVE_SUCCESS_CODE = "200103";
    public static final String MOBILE_INDEX_MENU_SAVE_SUCCESS_MESSAGE = "移动首页菜单保存接口访问成功";

    /**
     * 移动首页菜单修改接口访问成功
     */
    public static final String MOBILE_INDEX_MENU_UPDATE_SUCCESS_CODE = "200104";
    public static final String MOBILE_INDEX_MENU_UPDATE_SUCCESS_MESSAGE = "移动首页菜单修改接口访问成功";

    /**
     * 移动首页菜单删除接口访问成功
     */
    public static final String MOBILE_INDEX_MENU_DELETE_SUCCESS_CODE = "200105";
    public static final String MOBILE_INDEX_MENU_DELETE_SUCCESS_MESSAGE = "移动首页菜单删除接口访问成功";

    /**
     * 移动首页菜单批量删除接口访问成功
     */
    public static final String MOBILE_INDEX_MENU_BATCH_DELETE_SUCCESS_CODE = "200106";
    public static final String MOBILE_INDEX_MENU_BATCH_DELETE_SUCCESS_MESSAGE = "移动首页菜单批量删除接口访问成功";

    /**
     * 移动首页菜单新增校验楼层名称是否重复接口访问成功
     */
    public static final String MOBILE_INDEX_MENU_CHECK_NAME_SAVE_SUCCESS_CODE = "200107";
    public static final String MOBILE_INDEX_MENU_CHECK_NAME_SAVE_SUCCESS_MESSAGE = "移动首页菜单新增校验楼层名称是否重复接口访问成功";

    /**
     * 移动首页菜单修改校验楼层名称是否重复接口访问成功
     */
    public static final String MOBILE_INDEX_MENU_CHECK_NAME_UPDATE_SUCCESS_CODE = "200108";
    public static final String MOBILE_INDEX_MENU_CHECK_NAME_UPDATE_SUCCESS_MESSAGE = "移动首页菜单修改校验楼层名称是否重复接口访问成功";

    /**
     * 移动首页菜单保存接口访问失败，请求参数校验不合法
     */
    public static final String MOBILE_INDEX_MENU_SAVE_CLIENT_VALIDATION_ERROR_CODE = "401103";
    public static final String MOBILE_INDEX_MENU_SAVE_CLIENT_VALIDATION_ERROR_MESSAGE = "移动首页菜单保存接口访问失败，请求参数校验不合法";

    /**
     * 移动首页菜单修改接口访问失败，请求参数校验不合法
     */
    public static final String MOBILE_INDEX_MENU_UPDATE_CLIENT_VALIDATION_ERROR_CODE = "401104";
    public static final String MOBILE_INDEX_MENU_UPDATE_CLIENT_VALIDATION_ERROR_MESSAGE = "移动首页菜单修改接口访问失败，请求参数校验不合法";

    /**
     * 移动首页菜单批量删除接口访问失败，服务器内部异常
     */
    public static final String MOBILE_INDEX_MENU_BATCH_DELETE_SERVER_INTERNAL_ERROR_CODE = "500106";
    public static final String MOBILE_INDEX_MENU_BATCH_DELETE_SERVER_INTERNAL_ERROR_MESSAGE = "移动首页菜单批量删除接口访问失败，服务器内部异常";

    /**
     * 移动首页菜单分页接口访问返回无数据
     */
    public static final String MOBILE_INDEX_MENU_PAGE_RESPONSE_VALIDATION_ERROR_CODE = "501101";
    public static final String MOBILE_INDEX_MENU_PAGE_RESPONSE_VALIDATION_ERROR_MESSAGE = "移动首页菜单分页接口访问返回无数据";

    /**
     * 移动首页菜单详情接口访问返回无数据
     */
    public static final String MOBILE_INDEX_MENU_DETAIL_RESPONSE_VALIDATION_ERROR_CODE = "501102";
    public static final String MOBILE_INDEX_MENU_DETAIL_RESPONSE_VALIDATION_ERROR_MESSAGE = "移动首页菜单详情接口访问返回无数据";

    /**
     * 移动首页菜单删除接口访问失败，首页菜单不存在
     */
    public static final String MOBILE_INDEX_MENU_DELETE_RESPONSE_VALIDATION_ERROR_CODE = "501105";
    public static final String MOBILE_INDEX_MENU_DELETE_RESPONSE_VALIDATION_ERROR_MESSAGE = "移动首页菜单删除接口访问失败，首页菜单不存在";

    /*========================================================== 商品展示类目 ==================================================================*/

    /**
     * 展示类目保存
     */
    public static final String CUSTOM_SAVE_OPERATION_CODE = "200109";
    public static final String CUSTOM_SAVE_OPERATION_MESSAGE = "保存展示类目操作信息";

    /**
     * 展示类目修改
     */
    public static final String CUSTOM_UPDATE_OPERATION_CODE = "200110";
    public static final String CUSTOM_UPDATE_OPERATION_MESSAGE = "修改展示类目操作信息";

    /**
     * 展示类目删除
     */
    public static final String CUSTOM_DELETE_OPERATION_CODE = "200111";
    public static final String CUSTOM_DELETE_OPERATION_MESSAGE = "删除展示类目操作信息";

    /**
     * 展示类目批量删除
     */
    public static final String CUSTOM_DELETE_BRATCH_OPERATION_CODE = "200112";
    public static final String CUSTOM_DELETE_BRATCH_OPERATION_MESSAGE = "批量删除展示类目操作信息";

    /**
     * 展示类目导出
     */
    public static final String CUSTOM_EXPORT_OPERATION_CODE = "200113";
    public static final String CUSTOM_EXPORT_OPERATION_MESSAGE = "导出展示类目操作信息";

    /**
     * 展示类目启用\禁用
     */
    public static final String CUSTOM_UPDATE_SHOW_BRATCH_OPERATION_CODE = "200114";
    public static final String CUSTOM_UPDATE_SHOW_OPERATION_MESSAGE = "启用/禁用展示类目操作信息";


    protected OperationStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
