/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.adv;

/**
 * 广告状态枚举
 *
 * @Description 广告状态枚举
 * @Author 刘远杰
 * @Date 2019/5/15 09:16
 * Version 7.0
 **/
public enum ApplyStateEnum {
    /**
     * 待审核
     */
    WAIT(0),
    /**
     * 审核通过
     */
    PASS(1),
    /**
     * 审核未通过
     */
    REJECT(2);


    private int value;

    ApplyStateEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
