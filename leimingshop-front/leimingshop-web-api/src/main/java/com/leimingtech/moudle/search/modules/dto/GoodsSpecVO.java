/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import lombok.Data;

/**
 * @Author tyl
 * @Date 2019/7/4 12:01
 * @Description
 **/
@Data
public class GoodsSpecVO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long goodsId;
    /**
     * 商品规格ID
     */
    private Long specId;
    /**
     * 商品规格属性ID
     */
    private Long specAttrId;
    /**
     * 商品规格属性值ID
     */
    private Long specAttrValueId;
    /**
     * 是否是主规格属性（0:不是,1:是）
     */
    private Integer isMain;


}
