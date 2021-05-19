/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.statusCode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 售后申请状态
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-09-18 16:42
 **/
public class AftersaleApplystatusCode extends ServiceStatusCode {

    /**
     * 售后申请分页访问成功
     */
    public static final ServiceStatusCode AFTERSALE_APPLY_PAGE_SUCCESS = new AftersaleApplystatusCode("200101", "售后申请分页访问成功", new Object[0]);

    protected AftersaleApplystatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
