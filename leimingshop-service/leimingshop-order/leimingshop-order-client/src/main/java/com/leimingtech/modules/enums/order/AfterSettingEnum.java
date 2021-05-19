/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:售后设置枚举
 * @Date: 2019/6/18 17:46
 * @Version: V1.0
 */
public enum AfterSettingEnum {

    // 否
    FALSE(0),
    // 是
    TRUE(1),
    //审核状态1:商家审核中
    SELLERAUDITING(1),
    //审核状态1:商家审核中
    ADMINAUDITING(2),
    //审核状态1:商家审核中
    FINISH(3),
    //审核结果（0:未审核）
    NOAUDIT(0),
    //审核结果（1:审核通过）
    PROCESS(1),
    //审核结果（2:审核不通过）
    REFUSE(2),

    // 平台不支持退货
    CANNOTRETURN("0"),
    // 平台支持退货
    CANRETURN("1"),
    // 平台不支持换货
    CANNOTBARTER("0"),
    // 平台支持换货
    CANBARTER("1");

    private int value;

    private String valueString;

    private Integer valueInteger;

    AfterSettingEnum(int value) {
        this.value = value;
    }

    AfterSettingEnum(String valueString) {
        this.valueString = valueString;
    }

    AfterSettingEnum(Integer valueInteger) {
        this.valueInteger = valueInteger;
    }

    public int value() {
        return this.value;
    }

    public String valueString() {
        return this.valueString;
    }

    public Integer valueInteger() {
        return this.valueInteger;
    }

}
