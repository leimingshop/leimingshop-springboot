/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.evaluate;

/**
 * @ClassName:GoodsErrorCodeEnum
 * @Data:2019/5/17 9:25
 * @Author：chengqian 商品错枚举类
 * @Version 1.0
 */

public enum GoodsEnum {
    /**
     * 不支持快递
     */
    EXPRESS_FLAG_NO(0),
    /**
     * 支持快递
     */
    EXPRESS_FLAG_YES(1),
    /**
     * 是否评价  0 未评价
     */
    IS_EVALUATE(0),
    /**
     * 读取状态 1已读
     */
    READ_YES(1),
    /**
     * 读取状态 0未读
     */
    READ_NO(0),
    /**
     * 回复状态  1 已回复
     */
    REPLY_STATE_YES(1),

    /**
     * 回复状态  1 未回复
     */
    REPLY_STATE_NO(0),
    /**
     * 评论是否正常显示  0 正常显示
     */
    EVALUATE_STATE(0),
    /**
     * evaluateScores ==1 的时候为差评
     */
    EVALUATE_SCORES_ONE(1),
    /**
     * evaluateScores ==5 的时候为差评
     */
    EVALUATE_SCORES_FIVE(5),
    /**
     * isContent=1时有评价内容
     */
    IS_CONTENT(1),
    /**
     * isContent=0时有评价内容
     */
    IS_NOT_CONTENT(0),
    /**
     * evaluateScores ==3 的时候为差评
     */
    EVALUATE_SCORES_THREE(3);


    private int value;

    GoodsEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
