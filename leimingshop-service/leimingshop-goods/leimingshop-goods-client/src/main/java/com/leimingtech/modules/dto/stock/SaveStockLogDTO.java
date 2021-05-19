/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.stock;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 新增库存修改记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Data
@ApiModel(description = "SaveStockLogDTO")
public class SaveStockLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "sku")
    private Long sku;
    @ApiModelProperty(value = "商品ID ")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "修改前库存")
    @NotBlank(message = "修改前库存不能为空", groups = AddGroup.class)
    private Integer beforeRepertory;
    @ApiModelProperty(value = "修改后库存")
    @NotBlank(message = "修改后库存不能为空", groups = AddGroup.class)
    private Integer afterRepertory;

}