/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * @ClassName MessageStatusCode
 * @Description 站内信提示吗
 * @Author DY
 * @Date 2019/8/8 15:44
 * @Version 1.0
 **/
public class MessageStatusCode extends ServiceStatusCode {

    /**
     * 站内信保存成功
     */
    public static final String MESSAGE_SAVE_SUCCESS = "2001001";
    public static final String MESSAGE_SAVE_SUCCESS_MSG = "站内信保存成功";

    /**
     * 站内信删除成功
     */
    public static final String MESSAGE_DEL_SUCCESS = "2001002";
    public static final String MESSAGE_DEL_SUCCESS_MSG = "站内信删除成功";

    /**
     * 站内信修改成功
     */
    public static final String MESSAGE_UPDATE_SUCCESS = "2001003";
    public static final String MESSAGE_UPDATE_SUCCESS_MSG = "站内信修改成功";

    /**
     * 站内信查询成功
     */
    public static final String MESSAGE_PAGE_SUCCESS = "2001004";
    public static final String MESSAGE_PAGE_SUCCESS_MSG = "站内信查询成功";
    public static final ServiceStatusCode SEND_NO_SELECT_FAILED = new MessageStatusCode("200100", "未选择收件人，无法发送站内信");
    private static final long serialVersionUID = 8420054968445160588L;


    protected MessageStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
