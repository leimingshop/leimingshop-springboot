/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.stock;

import com.leimingtech.modules.dto.goods.price.GoodsSpecStorageUpdateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Data: 2019/7/19 11:59
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel(description = "UpdateBatchStockLog")
public class UpdateBatchStockLog {

    @ApiModelProperty(value = "修改信息")
    List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList;
    @ApiModelProperty(value = "修改人")
    String userName;

    @ApiModelProperty("1 不发送库存消息")
    private Integer type;
}
