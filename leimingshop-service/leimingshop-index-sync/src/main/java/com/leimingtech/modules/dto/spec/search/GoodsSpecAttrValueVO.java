/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;


import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author tyl
 * @Date 2019/7/3 18:42
 * @Description 商品规格属性值信息
 * lmshop_goods_spec_attr_value
 **/
@Data
public class GoodsSpecAttrValueVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @FieldInfo(type = "long")
    private Long id;

    @FieldInfo
    private String goodsId;


    /**
     * 商品规格属性ID
     */
    @FieldInfo(type = "long")
    private Long specAttrId;
    /**
     * 商品规格属性值
     */
    @FieldInfo
    private String specAttrValue;


}
