/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * @Description
 * @Data: 2019/7/9 10:32
 * @Author: chengqian
 * @Version: 1.0
 */
public class SysStatusCode extends ServiceStatusCode {

    /**
     * 保存图片数据 admin
     */
    public static final String ADMIN_PICTURE_SAVE_SUCCESS = "200301";
    public static final String ADMIN_PICTURE_SAVE_SUCCESS_MESSAGE = "保存图片数据";

    /**
     * 批量修改图片分组
     */
    public static final String ADMIN_PICTURE_UPDATE_SUCCESS = "200302";
    public static final String ADMIN_PICTURE_UPDATE_SUCCESS_MESSAGE = "批量修改图片分组";


    /**
     * 保存图片数据 seller
     */
    public static final String SELLER_PICTURE_SAVE_SUCCESS = "200303";
    public static final String SELLER_PICTURE_SAVE_SUCCESS_MESSAGE = "保存图片数据";

    /**
     * 保存图片分组 seller
     */
    public static final String SELLER_PICTURE_CATEGORY_SAVE_SUCCESS = "200304";
    public static final String SELLER_PICTURE_CATEGORY_SAVE_SUCCESS_MESSAGE = "保存图片分组";

    /*========================================================== 文章分类操作日志 ==================================================================*/
    /**
     * 文章分类保存
     */
    public static final String ARTICLE_CLASS_SAVE_OPERATION_CODE = "200305";
    public static final String ARTICLE_CLASS_SAVE_OPERATION_MESSAGE = "文章分类保存";
    /**
     * 文章分类修改
     */
    public static final String ARTICLE_CLASS_UPDATE_OPERATION_CODE = "200306";
    public static final String ARTICLE_CLASS_UPDATE_OPERATION_MESSAGE = "文章分类修改";
    /**
     * 文章分类删除
     */
    public static final String ARTICLE_CLASS_DELETE_OPERATION_CODE = "200307";
    public static final String ARTICLE_CLASS_DELETE_OPERATION_MESSAGE = "文章分类删除";
    /**
     * 文章分类导出
     */
    public static final String ARTICLE_CLASS_EXPORT_OPERATION_CODE = "200308";
    public static final String ARTICLE_CLASS_EXPORT_OPERATION_MESSAGE = "文章分类导出";


    /*========================================================== 文章操作日志 ==================================================================*/
    /**
     * 文章保存
     */
    public static final String ARTICLE_SAVE_OPERATION_CODE = "200309";
    public static final String ARTICLE_SAVE_OPERATION_MESSAGE = "文章保存";
    /**
     * 文章修改
     */
    public static final String ARTICLE_UPDATE_OPERATION_CODE = "200310";
    public static final String ARTICLE_UPDATE_OPERATION_MESSAGE = "文章修改";
    /**
     * 文章删除
     */
    public static final String ARTICLE_DELETE_OPERATION_CODE = "200311";
    public static final String ARTICLE_DELETE_OPERATION_MESSAGE = "文章删除";
    /**
     * 文章导出
     */
    public static final String ARTICLE_EXPORT_OPERATION_CODE = "200312";
    public static final String ARTICLE_EXPORT_OPERATION_MESSAGE = "文章导出";

    /*========================================================== 系统操作日志 ==================================================================*/

    /**
     * 系统文章保存
     */
    public static final String DOCUMENT_SAVE_OPERATION_CODE = "200313";
    public static final String DOCUMENT_SAVE_OPERATION_MESSAGE = "系统文章保存";
    /**
     * 系统文章修改
     */
    public static final String DOCUMENT_UPDATE_OPERATION_CODE = "200314";
    public static final String DOCUMENT_UPDATE_OPERATION_MESSAGE = "系统文章修改";
    /**
     * 系统文章删除
     */
    public static final String DOCUMENT_DELETE_OPERATION_CODE = "200315";
    public static final String DOCUMENT_DELETE_OPERATION_MESSAGE = "系统文章删除";
    /**
     * 系统文章导出
     */
    public static final String DOCUMENT_EXPORT_OPERATION_CODE = "200316";
    public static final String DOCUMENT_EXPORT_OPERATION_MESSAGE = "系统文章导出";

    protected SysStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
