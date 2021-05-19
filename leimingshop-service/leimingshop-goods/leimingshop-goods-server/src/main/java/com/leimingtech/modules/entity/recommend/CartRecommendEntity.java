/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.recommend;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 购物车推荐商品表
 *
 * @author 程前
 * @since v1.0.0 2019-12-6
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cart_recommend")
public class CartRecommendEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品spu的id
     */
    private Long goodsId;
    /**
     * 排序号
     */
    private Integer sortNum;

    /**
     * 所属分类
     */
    private String category;

}