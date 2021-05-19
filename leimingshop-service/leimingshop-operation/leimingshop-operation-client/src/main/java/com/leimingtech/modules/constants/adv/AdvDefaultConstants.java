/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constants.adv;

/**
 * @Description 广告管理默认值常量类
 * @Author 刘远杰
 * @Date 2019/6/6 11:29
 * Version 7.0
 **/
public class AdvDefaultConstants {

    /**
     * 广告分类类型 0普通广告 1楼层广告 2分类广告
     */
    public static final int SYS_FLAG = 0;
    /**
     * 广告关联类型 url
     */
    public static final String RELATION_TYPE = "url";
    /**
     * 广告状态（默认0:未启用、1:启用、2:停用）
     */
    public static final int ADV_STATE = 1;
    /**
     * 广告审批状态（默认0:待审核、1:审核通过、2:审核未通过）
     */
    public static final int APPLY_STATE = 1;

    private AdvDefaultConstants() {
    }


}
