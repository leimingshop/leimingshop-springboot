/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.goods.goodsclass.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 商品分类信息
 *
 * @author xuzhch
 * @author xuzhch@leimingtech.com
 */
@Data
@ApiModel(description = "GoodsClassVo")
public class GoodsClassResultVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private GoodsClassVo currentClassVo;
    private List<GoodsClassVo> goodsClassVos;
}