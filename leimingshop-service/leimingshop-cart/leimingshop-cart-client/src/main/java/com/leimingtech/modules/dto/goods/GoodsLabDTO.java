/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品搜索DTO
 *
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 5:50 PM
 */
@Data
public class GoodsLabDTO implements Serializable {

    private static final long serialVersionUID = -5952699787340823928L;

    /**
     * 店铺类型 1:自营商户，2:普通商户
     */
    private Integer storeType;

    /**
     * 商品标签搜索实体
     */
    private List<GoodsLabelDTO> goodsLabels;


}
