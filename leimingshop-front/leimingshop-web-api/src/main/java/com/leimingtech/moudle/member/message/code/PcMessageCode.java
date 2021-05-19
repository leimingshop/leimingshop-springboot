/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.message.code;

/**
 * 用户发票信息入参日志码
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020/5/16 9:48
 **/
public interface PcMessageCode {

    /**
     * 用户发票列表
     */
    String MEMBER_MESSAGE_PAGE_CODE = "200604";
    String MEMBER_MESSAGE_PAGE_DESC = "访问用户消息列表";


    /**
     * 访问用户消息详情
     */
    String MEMBER_MESSAGE_DETAIL_CODE = "200605";
    String MEMBER_MESSAGE_DETAIL_DESC = "访问用户消息详情";
    /**
     * 访问用户消息详情
     */
    String MEMBER_MESSAGE_READ_CODE = "200606";
    String MEMBER_MESSAGE_READ_DESC = "访问用户一键已读";

    /**
     * 用户删除发票信息
     */
    String MEMBER_MESSAGE_DELETE_CODE = "200606";
    String MEMBER_MESSAGE_DELETE_DESC = "访问用户删除删除消息";

}
