/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.stock;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 库存修改记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_stock_log")
public class StockLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 规格ID
     */
    private Long sku;
    /**
     * 规格编号
     */
    private String specSerial;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 修改前库存
     */
    private Integer beforeRepertory;

    /**
     * 修改后库存
     */
    private Integer afterRepertory;

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
     * 删除标记（默认为0未删除，1已删除）
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