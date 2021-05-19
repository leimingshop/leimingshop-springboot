/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品与标签关联管理
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-13
 */
@Data
@ApiModel(description = "GoodsLabelRelDTO")
public class GoodsLabelRelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "标签ID")
    private Long labelId;
    @ApiModelProperty(value = "标签名称")
    private String labelName;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "店铺数量")
    private Integer storeNum;


}
