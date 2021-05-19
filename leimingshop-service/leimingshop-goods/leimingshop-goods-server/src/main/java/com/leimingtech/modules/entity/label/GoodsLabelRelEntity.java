/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.label;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品与标签关联表
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_label_rel")
public class GoodsLabelRelEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 标签ID
     */
    private Long labelId;
    /**
     * 标签名称
     */
    private String labelName;
}
