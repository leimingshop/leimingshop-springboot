/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.store;

/**
 * @Description 店铺枚举
 * @Data: 2019/7/2 9:56
 * @Author: chengqian
 * @Version: 1.0
 */
public enum StoreEnum {


    /**
     * 启用 （默认）
     */
    IS_ENABLE_YES(0),
    /**
     * 自营
     */
    SELF_STORE(1),
    /**
     * 普通
     */
    COMMON_STORE(2),

    /**
     * admin端创建店铺
     */
    ADMIN_TYPE(1),

    COMPANY_AUDIT(2),

    /**
     * 审核状态（30审核未通过）
     */
    AUDIT_STATUS_NO(30),

    /**
     * 审核状态（20审核通过）
     */
    AUDIT_STATUS_YES(20),

    /**
     * 审核状态（10待审核）
     */
    AUDIT_STATUS(10),
    /**
     * 审核状态（40待提交）
     */
    AUDIT_STATUS_SUB(40),
    /**
     * 禁用
     */
    IS_ENABLE_NO(1);
    private int value;

    StoreEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
