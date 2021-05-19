/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.price;

import com.leimingtech.modules.dto.goods.price.GoodsSpecPriceUpdateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Data: 2019/7/19 13:11
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel(description = "UpdateBatchPriceLog")
public class UpdateBatchPriceLog {
    @ApiModelProperty(value = "修改信息")
    List<GoodsSpecPriceUpdateDTO> goodsSpecPriceUpdateDTOList;
    @ApiModelProperty(value = "修改人")
    String userName;
}
