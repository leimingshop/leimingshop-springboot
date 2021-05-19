/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.modify;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 数据校验
 *
 * @author chengqian
 */
@Data
@ApiModel(description = "ValidSpecSerial")
public class ValidSpecSerial implements Serializable {

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "规格货号")
    @NotNull(message = "规格货号不能为空", groups = UpdateGroup.class)
    private String[] serials;
}
