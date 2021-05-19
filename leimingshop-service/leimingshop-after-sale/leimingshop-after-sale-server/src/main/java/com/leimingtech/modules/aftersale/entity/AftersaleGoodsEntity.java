/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 售后商品表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_aftersale_goods")
public class AftersaleGoodsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 售后单号
     */
    private Long aftersaleSn;

    /**
     * 订单商品ID
     */
    private Long orderGoodsId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 商品规格ID
     */
    private Long specId;

    /**
     * 商品规格货号s
     */
    private String specSerial;

    /**
     * 商品规格名称
     */
    private String specName;

    /**
     * 商品规格属性值
     */
    private String specAttrName;

    /**
     * 商品规格主图
     */
    private String specMainPicture;

    /**
     * 是否是赠送商品(0:否，1:是)
     */
    private Integer isGift;

    /**
     * 商品实际支付金额
     */
    private BigDecimal specPayPrice;
    /**
     * 商品售价
     */
    private BigDecimal specSellPrice;

    /**
     * 商品成本价
     */
    private BigDecimal specCostPrice;

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
