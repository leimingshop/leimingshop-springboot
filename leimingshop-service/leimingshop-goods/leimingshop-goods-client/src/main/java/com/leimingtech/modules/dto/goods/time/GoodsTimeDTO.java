/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.time;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * @Author: weixianchun
 * @Description: 定时上架商品
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@ApiModel(description = "GoodsTimeDTO")
public class GoodsTimeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    /**
     * 商品规格ID
     */
    @ApiModelProperty(value = "商品规格ID")
    private Long specId;
    /**
     * 操作状态(0定时发布 ,默认1定时上下架)
     */
    @ApiModelProperty(value = "操作状态(0定时发布 ,默认1定时上下架)")
    private Integer operationalStatus;
    /**
     * 上架时间
     */
    @ApiModelProperty(value = "上架时间")
    private Date shelfTime;

    @ApiModelProperty(value = "操作人标识: 0 商家; 1平台")
    private Integer operatorType;

    @ApiModelProperty(value = "上下架标识:,0下架状态，1上架状态,2未上架")
    private Integer showStatus;

}