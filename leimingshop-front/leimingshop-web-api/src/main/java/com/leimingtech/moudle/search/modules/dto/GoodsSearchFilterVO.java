/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 2:59 PM
 */
@ApiModel(description = "GoodsSearchFilterVO")
@Data
public class GoodsSearchFilterVO implements Serializable {

    private static final long serialVersionUID = -3699978443425761935L;

    @ApiModelProperty(value = "商品信息")
    private List<GoodsVO> goodsVOList;

    @ApiModelProperty(value = "总页数")
    private Long totalCount;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(value = "页码")
    private Integer pageNo;
}
