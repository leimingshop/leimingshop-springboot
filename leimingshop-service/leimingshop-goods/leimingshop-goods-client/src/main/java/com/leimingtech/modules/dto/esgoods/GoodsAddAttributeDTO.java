/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.esgoods;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 商品属性
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsAddAttributeDTO")
public class GoodsAddAttributeDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 属性名称
     */
    private String attrName;

    /**
     * 属性值（多个属性值之间用逗号隔开）
     */
    private String attrValue;

    /**
     * 属性ID
     */
    private Long attributeId;

    /**
     * 商品ID
     */
    private Long goodsId;


}