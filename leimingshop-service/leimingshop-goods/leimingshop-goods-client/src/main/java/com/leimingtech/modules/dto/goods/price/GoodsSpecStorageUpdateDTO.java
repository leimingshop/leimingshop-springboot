/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.price;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: weixianchun
 * @Description: 商品库存保存对象
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsSpecStorageUpdateDTO")
public class GoodsSpecStorageUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "规格ID")
    @NotNull(message = "规格ID不能为空", groups = UpdateGroup.class)
    private Long id;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "规格编号")
    @NotNull(message = "规格ID不能为空", groups = UpdateGroup.class)
    private String specSerial;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 修改后库存
     */
    @ApiModelProperty(value = "库存")
    @Min(value = 0, message = "库存不能小于0", groups = UpdateGroup.class)
    @NotNull(message = "库存不能为空", groups = UpdateGroup.class)
    private Integer specStorage;

    /**
     * 库存
     */
    @ApiModelProperty(value = "修改前库存（后台使用）")
    private Integer beforeSpecStorage;
}