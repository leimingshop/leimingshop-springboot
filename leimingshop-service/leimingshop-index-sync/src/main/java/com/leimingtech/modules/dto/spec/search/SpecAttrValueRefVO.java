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
 * @Description 返回规格属性名和属性值的关系
 **/
@Data
public class SpecAttrValueRefVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @FieldInfo(type = "long")
    private Long id;
    @FieldInfo(type = "long")
    private Long goodsId;
    /**
     * 商品规格ID
     */
    @FieldInfo(type = "long")
    private Long specId;
    /**
     * 商品规格属性ID
     */
    @FieldInfo(type = "long")
    private Long specAttrId;
    /**
     * 商品规格属性值ID
     */
    @FieldInfo(type = "long")
    private Long specAttrValueId;
    /**
     * 是否是主规格属性（0:不是,1:是）
     */
    @FieldInfo(type = "integer")
    private Integer isMain;

    /**
     * 规格上下架状态（默认:2未上架,0下架状态，1上架状态）
     */
    @FieldInfo(type = "integer")
    private Integer specShow;

    /**
     * 规格库存
     */
    @FieldInfo(type = "integer")
    private Integer specStorage;

    /**
     * 是否删除 0 未删除 1  已删除
     */
    @FieldInfo(type = "integer")
    private Integer delFlag;


}
