/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author tyl
 * @Date 2019/7/3 18:44
 * @Description 商品规格属性
 * lmshop_goods_spec_attr
 **/
@Data
public class GoodsSpecAttrVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @FieldInfo(type = "long")
    private Long id;
    /**
     * 规格属性名称
     */
    @FieldInfo
    private String specAttrName;
    /**
     * 是否选中（默认0未选中，1选中）
     */
    @FieldInfo(type = "integer")
    private Integer isSelect;
    /**
     * 规格id
     */
    @FieldInfo(type = "long")
    private Long attrSpecId;
    @FieldInfo(type = "long")
    private Long specAttrId;

    @FieldInfo(type = "long")
    private Long goodsId;


    /**
     * 是否主图
     */
    private Integer isMain;

    @FieldInfo(type = "long")
    private Long specAttrValueId;


    @FieldInfo(type = "object")
    private List<GoodsSpecAttrValueVO> goodsSpecAttrValueVOList;
}
