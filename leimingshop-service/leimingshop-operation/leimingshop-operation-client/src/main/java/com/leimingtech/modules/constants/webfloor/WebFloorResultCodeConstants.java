/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constants.webfloor;

/**
 * @Description h5楼层控制层代码返回状态码常量类
 * @Author 刘远杰
 * @Date 2019/5/13 11:29
 * Version 7.0
 **/
public class WebFloorResultCodeConstants {

    public static final int SUCCESS = 200;
    /**
     * 参数错误
     */
    public static final int ERR_INV_PARAMS = 400;
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

    private WebFloorResultCodeConstants() {
    }

}
