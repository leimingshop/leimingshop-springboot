/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: weixianchun
 * @Description: 商品标签管理DTO
 * @Date :2019/5/20 14:28
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsLabelDTO")
public class GoodsLabelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品标签名")
    private String labelName;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "店铺数量")
    private Integer storeNum;

    @ApiModelProperty(value = "分组名称")
    private String groupName;


}
