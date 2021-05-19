/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.settle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 结算
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@ApiModel(description = "UpdateBillTotalDTO")
public class UpdateBillTotalDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "备注")
    private String billRemark;

    @ApiModelProperty("商家确认状态（0 未确认 1 已确认）")
    private Integer confirmStatus;

}