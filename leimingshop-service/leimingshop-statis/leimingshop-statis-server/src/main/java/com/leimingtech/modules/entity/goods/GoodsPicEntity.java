/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods;

import lombok.Data;


/**
 * 商品主图信息
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
public class GoodsPicEntity {
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品主图
     */
    private String goodsMainPicture;

}
