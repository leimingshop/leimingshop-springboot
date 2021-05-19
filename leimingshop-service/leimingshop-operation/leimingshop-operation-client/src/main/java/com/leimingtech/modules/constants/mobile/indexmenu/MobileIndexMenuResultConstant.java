/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constants.mobile.indexmenu;

/**
 * 功能描述：
 * <移动首页菜单错误码常量类>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/11 16:03
 **/
public class MobileIndexMenuResultConstant {

    public static final int SUCCESS = 200;
    /**
     * 参数错误
     */
    public static final int ERR_INV_PARAMS = 400;
    /**
     * 权限不足
     */
    public static final int ERR_NO_PER = 403;
    /**
     * 重复
     */
    public static final int ERR_REPEAT = 407;
    /**
     * 查无数据
     */
    public static final int ERR_NO_RESULT = 408;
    /**
     * 代码错误
     */
    public static final int ERR_BADCODE = 500;

    private MobileIndexMenuResultConstant() {
    }

}
