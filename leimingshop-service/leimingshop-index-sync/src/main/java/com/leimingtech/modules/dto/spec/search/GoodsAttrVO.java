/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author tyl
 * @Date 2019/7/3 18:44
 * @Description 商品属性
 * lmshop_goods_attribute
 **/
@Data
public class GoodsAttrVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
    @FieldInfo(type = "long")
    private Long attributeId;
    /**
     * 属性名
     */
    @FieldInfo
    private String attrName;
    /**
     * 属性值
     */
    @FieldInfo
    private String attrValueName;
    /**
     * 展示类型（默认0单选，1下拉框，2多选）
     */
    @FieldInfo(type = "integer")
    private Integer showType;


}
