/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums.synonym;

/**
 * 同义词枚举
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 10:40
 **/
public enum SynonymEnum {

    /**
     * 同义词启用
     */
    ON(1),

    /**
     * 同义词停用
     */
    OFF(0);

    private int value;

    SynonymEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
