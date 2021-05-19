/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.search;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 5:50 PM
 */
@Data
public class GoodsAttrSearchDTO {
    /**
     * 主键
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 属性名称
     */
    @FieldInfo(type = "text")
    private String attrName;

    /**
     * 属性值（多个属性值之间用逗号隔开）
     */
    @FieldInfo(type = "text")
    private String attrValue;

    /**
     * 属性ID
     */
    @FieldInfo(type = "long")
    private Long attributeId;

    /**
     * 商品ID
     */
    @FieldInfo(type = "long")
    private Long goodsId;

    /**
     * 展示类型（默认0单选，1下拉框，2多选）
     */
    @FieldInfo(type = "integer")
    private Integer showType;

    /**
     * 是否索引（预留字段）
     */
    @FieldInfo(type = "integer")
    private Integer isKey;

}
