/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * @Description
 * @Data: 2019/7/9 11:12
 * @Author: chengqian
 * @Version: 1.0
 */
public class StoreStatusCode extends ServiceStatusCode {

    /*========================================================== 店铺操作==================================================================*/

    /**
     * 新增店铺
     */
    public static final String ADMIN_STORE_SAVE_SUCCESS = "200401";
    public static final String ADMIN_STORE_SAVE_SUCCESS_MESSAGE = "新增店铺";

    /**
     * 修改店铺信息 admin
     */
    public static final String ADMIN_STORE_UPDATE_SUCCESS = "200402";
    public static final String ADMIN_STORE_UPDATE_SUCCESS_MESSAGE = "修改店铺";
    /**
     * 修改店铺信息 seller
     */
    public static final String SELLER_STORE_UPDATE_SUCCESS = "200403";
    public static final String SELLER_STORE_UPDATE_SUCCESS_MESSAGE = "修改店铺";

    /**
     * 获取店铺菜单
     */
    public static final String SELLER_STORE_MENU_SUCCESS = "200404";
    public static final String SELLER_STORE_MENU_SUCCESS_MESSAGE = "获取店铺菜单";

    /**
     * 新增店铺信息
     */
    public static final String ADMIN_STORE_SAVE_USER_SUCCESS = "200405";
    public static final String ADMIN_STORE_SAVE_USER_SUCCESS_MESSAGE = "新增店铺信息";

    /*========================================================== 店铺等级操作 ==================================================================*/


    /**
     * 店铺等级保存
     */
    public static final String STORE_GRADE_SAVE_OPERATION_CODE = "200406";
    public static final String STORE_GRADE_SAVE_OPERATION_MESSAGE = "店铺等级保存";

    /**
     * 店铺等级修改
     */
    public static final String STORE_GRADE_UPDATE_OPERATION_CODE = "200407";
    public static final String STORE_GRADE_UPDATE_OPERATION_MESSAGE = "店铺等级修改";

    /**
     * 店铺等级删除
     */
    public static final String STORE_GRADE_DELETE_OPERATION_CODE = "200408";
    public static final String STORE_GRADE_DELETE_OPERATION_MESSAGE = "店铺等级删除";

    /**
     * 店铺等级启用/禁用
     */
    public static final String STORE_GRADE_SHOW_OPERATION_CODE = "200409";
    public static final String STORE_GRADE_SHOW_OPERATION_MESSAGE = "店铺等级启用/禁用";

    /**
     * 店铺等级导出
     */
    public static final String STORE_GRADE_EXPORT_OPERATION_CODE = "2004010";
    public static final String STORE_GRADE_EXPORT_OPERATION_MESSAGE = "店铺等级导出";

    public static final ServiceStatusCode STORE_IS_NULL_ENABLE = new StoreStatusCode("400411", "该店铺已禁用，请联系运营平台");
    public static final ServiceStatusCode STORE_GOODS_MORE = new StoreStatusCode("400412", "该商家上架商品数量已超过修改等级规定数量，无法修改，请下架商品");
    public static final ServiceStatusCode STORE_IS_NOT_FOUND = new StoreStatusCode("400413", "要上架商品的店铺不存在");


    protected StoreStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
