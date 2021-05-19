/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods.time;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 定时上架商品
 * @Date :2019/6/4 17:54
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_time")
public class GoodsTimeEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 商品规格ID
     */
    private Long specId;

    /**
     * 操作状态(0定时发布 ,默认1定时上下架)
     */
    private Integer operationalStatus;

    /**
     * 上下架标识:,0下架状态，1上架状态,2未上架
     */
    private Integer showStatus;

    /**
     * 上架时间
     */
    private Date shelfTime;

    /**
     * 操作人标识: 0 商家; 1平台
     */
    private Integer operatorType;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认0:未删除,1:已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}