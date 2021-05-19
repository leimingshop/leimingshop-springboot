/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.browse;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 足迹记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_browse")
public class GoodsBrowseEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品规格Id
     */
    private Long goodsSpecId;

    /**
     * 更新者
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