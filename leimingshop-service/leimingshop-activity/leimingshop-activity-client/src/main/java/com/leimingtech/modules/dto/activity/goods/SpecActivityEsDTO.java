/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <商品活动数据实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/11
 */
@Data
@ApiModel("商品活动数据实体")
public class SpecActivityEsDTO extends SpecActivityDTO {

    @ApiModelProperty("商品specId")
    private Long specId;

    @ApiModelProperty("商品spuId")
    private Long goodsId;

}
