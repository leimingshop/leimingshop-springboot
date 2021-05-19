/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.evaluate;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 店铺评分表
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_evaluate_store")
public class EvaluateStoreEntity extends BaseEntity {
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
     * 店铺编号
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 买家编号
     */
    private Long memberId;

    /**
     * 买家名称
     */
    private String memberName;

    /**
     * 描述相符评分
     */
    private Double sevalDesccredit;

    /**
     * 服务态度评分
     */
    private Double sevalServicecredit;

    /**
     * 发货速度评分
     */
    private Double sevalDeliverycredit;

    /**
     * 是否删除； 1 已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 版本号
     */
    @Version
    private Integer version;

}