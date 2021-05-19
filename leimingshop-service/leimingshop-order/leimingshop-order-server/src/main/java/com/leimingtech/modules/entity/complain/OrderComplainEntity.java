/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.complain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单投诉表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_complain")
public class OrderComplainEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private Long orderSn;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 会员账号
     */
    private String account;
    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 投诉类型1:服务相关,2:物流相关,3:售后相关,4:商品相关5:其他
     */
    private Integer type;
    /**
     * 投诉来源 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序
     */
    private Integer source;
    /**
     * 详细说明
     */
    private String cause;
    /**
     * 投诉图片
     */
    private String picture;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 投诉状态 10：待处理 20：已处理
     */
    private Integer status;
    /**
     * 是否展示 0 展示  1 不展示
     */
    private Integer showFlag;
    /**
     * 处理人
     */
    private String disposePerson;
    /**
     * 处理时间
     */
    private Date disposeDate;
    /**
     * 投诉判定 1：有效投诉，2：重点问题，3：无效投诉
     */
    private Integer verdict;
    /**
     * 回复内容
     */
    private String replyContent;
    /**
     * 处理备注
     */
    private String remark;
    /**
     * 评价客服满意度1-2星为差，3-4星为一般，5星为满意
     */
    private Integer customerSatisfaction;
    /**
     * 评价客服内容
     */
    private String customerContent;
}