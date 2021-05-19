/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 确认核销
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-04-29 18:34
 **/
@Data
@ApiModel(description = "OrderVerifyCodeDTO")
public class OrderVerifyCodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "核销数量")
    private Integer verifyNum;

}
