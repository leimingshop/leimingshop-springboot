/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 功能描述：
 * <配送方式枚举>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/25 17:22
 **/
public enum DevlierTypeEnum {
    //    配送方式 0:无需物流 1:快递 2自提 3电子提货码
    //无需物流
    NO_LOGISTICS(0),
    //快递
    EXPRESS(1),
    //自提
    SELF_MENTION(2),
    //电子提货码
    FETCH_CODE(3);

    private Integer value;

    DevlierTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }

}
