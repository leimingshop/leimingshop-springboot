/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author: SWH ab4856812@163.com
 * @Description:封装售后申请售后商品数据实体
 * @Date: 2019/6/15 16:07
 * @Version: V1.0
 */
@Data
@ApiModel(description = "AftersaleApplyGoodsDTO")
public class AftersaleApplyGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品规格ID")
    private Long specId;
    @ApiModelProperty(value = "商品规格名称")
    private String specName;
    @ApiModelProperty(value = "商品规格属性值名称")
    private String specAttrName;

}