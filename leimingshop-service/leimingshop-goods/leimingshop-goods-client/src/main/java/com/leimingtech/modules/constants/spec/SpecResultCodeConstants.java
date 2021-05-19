/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constants.spec;

/**
 * @author 刘远杰
 * @Description 规格管理返回状态码常量类
 * @Date 2019/5/16 16：18
 * Version 7.0
 **/
public class SpecResultCodeConstants {

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

    private SpecResultCodeConstants() {
    }

}
