/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 库存修改记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Data
@ApiModel(description = "StockLogDTO")
public class StockLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "规格ID")
    private Long sku;
    @ApiModelProperty(value = "规格编号")
    private String specSerial;
    @ApiModelProperty(value = "商品Id")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品图片")
    private String specMainPicture;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "规格名称")
    private String specName;
    @ApiModelProperty(value = "修改前库存")
    private Integer beforeRepertory;
    @ApiModelProperty(value = "修改后库存")
    private Integer afterRepertory;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "更新人")
    private String updater;

}