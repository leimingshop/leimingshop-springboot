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
@ApiModel(description = "GoodsLabelRelSaveDTO")
public class GoodsLabelRelSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "标签ID")
    private Long labelId;
    @ApiModelProperty(value = "标签名称")
    private String labelName;
}
