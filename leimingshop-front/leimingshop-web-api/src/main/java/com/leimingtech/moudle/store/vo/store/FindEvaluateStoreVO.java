/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.store.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 查询店铺评分表
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "FindEvaluateStoreDTO")
public class FindEvaluateStoreVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "描述相符评分")
    private Double sevalDesccredit;
    @ApiModelProperty(value = "服务态度评分")
    private Double sevalServicecredit;
    @ApiModelProperty(value = "发货速度评分")
    private Double sevalDeliverycredit;

    @ApiModelProperty(value = "店铺评价")
    private Double storeEvaluate;
    @ApiModelProperty(value = "物流评分")
    private Double logisticsEvaluate;
    @ApiModelProperty(value = "售后评分")
    private Double afterSaleEvaluate;
    @ApiModelProperty(value = "店铺星级")
    private Integer storeLevel;
    @ApiModelProperty(value = "商品评价")
    private Double goodsEvaluate;

}