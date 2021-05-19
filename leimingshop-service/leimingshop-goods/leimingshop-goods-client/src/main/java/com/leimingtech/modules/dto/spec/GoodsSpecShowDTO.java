/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName GoodsSpecShowDTO
 * @Description 商品规格上下架
 * @Author DY
 * @Date 2019/7/18 11:03
 * @Version 1.0
 **/
@Data
@ApiModel(description = "GoodsSpecShowDTO")
public class GoodsSpecShowDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    @NotEmpty(message = "请选择至少一件商品")
    private Long goodsId;

    @ApiModelProperty(value = "规格id集合")
    @NotEmpty(message = "请选择至少一件商品")
    private List<Long> ids;
    @ApiModelProperty(value = "上下架时间")
    private Date shelfTime;
    @ApiModelProperty(value = "规格上下架状态（默认:2未上架,0下架状态，1上架状态）")
    @NotNull(message = "上下架状态为必填")
    private Integer specShow;
    @ApiModelProperty(value = "规格上架类型:0:立即下架，1定时下架")
    @NotNull(message = "上下架类型必填")
    private Integer showType;
}
