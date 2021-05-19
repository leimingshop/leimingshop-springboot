/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goodsclass;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类和品牌关联表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2019-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_class_brand")
public class GoodsClassBrandEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌Id
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 分类Id
     */
    private Long gcClassId;
}