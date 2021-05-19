/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.index;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xuzhch
 * @className IndexGoodsDataDTO
 * @description 首页商品基础信息
 * @date 2020/3/16/016
 */
@Data
public class IndexGoodsDataDTO implements Serializable {
    private Integer newGoodsCount;
    private Integer goodsCount;
}
