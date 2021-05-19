/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.article.code;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * @author xuzhch
 * @className ArticleCode
 * @description 文章状态码
 * @date 2020/2/24/024
 */
public class ArticleStatusCode extends ServiceStatusCode {
    public static final ServiceStatusCode DOCUMENT_CODE_REPEA = new ArticleStatusCode("400401", "文章标识码重复");


    protected ArticleStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
