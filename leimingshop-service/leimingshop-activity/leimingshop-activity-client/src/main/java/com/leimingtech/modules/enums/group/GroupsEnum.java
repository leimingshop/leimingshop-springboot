/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.group;

/**
 * 拼团活动相关枚举类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/7/15 14:34
 **/
public enum GroupsEnum {


    /**
     * 拼团众筹活动状态 0未开始
     */
    ACTIVITY_STATUS_NO(0),

    /**
     * 拼团众筹活动状态 1活动中
     */
    ACTIVITY_STATUS_ONGOING(1),

    /**
     * 拼团众筹活动状态 2活动结束
     */
    ACTIVITY_STATUS_END(2),

    /**
     * 拼团记录活动状态 0进行中
     */
    GROUP_STATUS_ONGOING(0),

    /**
     * 拼团记录活动状态 1拼团成功
     */
    GROUP_STATUS_SUCCESS(1),

    /**
     * 拼团记录活动状态 2拼团失败
     */
    GROUP_STATUS_FAIL(2),

    /**
     * 拼团众筹审核状态 10审核发布
     */
    AUDIT_STATUS_PUBLIC(10),

    /**
     * 拼团众筹审核状态 20审核中
     */
    AUDIT_STATUS_AUDIT(20),

    /**
     * 拼团众筹审核状态 30审核通过
     */
    AUDIT_STATUS_PASS(30),

    /**
     * 拼团众筹审核状态 40审核未通过
     */
    AUDIT_STATUS_REJECT(40),

    /**
     * 拼团众筹审核状态 50审核取消
     */
    AUDIT_STATUS_CANCEL(50);

    private int value;

    GroupsEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
