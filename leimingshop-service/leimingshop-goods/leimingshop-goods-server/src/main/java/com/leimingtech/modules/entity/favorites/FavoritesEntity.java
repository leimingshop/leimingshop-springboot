/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.favorites;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户收藏
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_favorites")
public class FavoritesEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品id
     */
    private Long specId;

    /**
     * 收藏时的商品价格
     */
    private BigDecimal favPrice;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺类型
     */
    private Integer storeType;

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
