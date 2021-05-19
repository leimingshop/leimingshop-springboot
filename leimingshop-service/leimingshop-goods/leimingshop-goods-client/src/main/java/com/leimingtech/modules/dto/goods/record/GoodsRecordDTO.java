/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author: weixianchun
 * @Description: 商品上下架状态记录
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsRecordDTO")
public class GoodsRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long specId;

    /**
     * 商品上下架状态（默认:2未上架,0下架状态，1上架状态）
     */
    @ApiModelProperty(value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer goodsShow;

    /**
     * 下架描述
     */
    @ApiModelProperty(value = "下架描述")
    private String remark;

}