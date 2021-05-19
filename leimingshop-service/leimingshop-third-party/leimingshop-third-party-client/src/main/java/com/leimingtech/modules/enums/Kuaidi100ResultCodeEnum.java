/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 快递100返回值枚举
 * @Date: 2019/7/31 15:46
 * @Version: V1.0
 */
public enum Kuaidi100ResultCodeEnum {

    /**
     * 提交成功
     */
    SUCCESS(200),

    /**
     * 拒绝订阅的快递公司
     */
    REFUSE_COMPANY(701),

    /**
     * 订阅方的订阅数据存在错误（如不支持的快递公司、单号为空、单号超长等）或错误的回调地址
     */
    FALSE(700),

    /**
     * POLL:识别不到该单号对应的快递公司
     */
    NO_COMPANY(702),

    /**
     * 您不是合法的订阅者（即授权Key出错）
     */
    REFUSE_KEY(600),

    /**
     * POLL:KEY已过期
     */
    KEY_EXPIRE(601),

    /**
     * 服务器错误（即快递100的服务器出理间隙或临时性异常，有时如果因为不按规范提交请求，比如快递公司参数写错等，也会报此错误）
     */
    ERROR(500),

    /**
     * 重复订阅（请格外注意，501表示这张单已经订阅成功且目前还在跟踪过程中（即单号的status=polling），
     * 快递100的服务器会因此忽略您最新的此次订阅请求，从而返回501。一个运单号只要提交一次订阅即可，若要提交多次订阅，
     * 请在收到单号的status=abort或shutdown后隔半小时再提交订阅
     */
    REPEAT(501);


    private Integer value;

    Kuaidi100ResultCodeEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }
}
