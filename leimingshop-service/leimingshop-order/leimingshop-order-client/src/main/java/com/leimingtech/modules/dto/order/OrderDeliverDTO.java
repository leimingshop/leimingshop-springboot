/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 功能描述：
 * <订单发货实体>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/26 11:17
 **/
@Data
@ApiModel(description = "OrderDeliverDTO")
public class OrderDeliverDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "订单id不能为空", groups = UpdateGroup.class)
    private Long id;
    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    @NotNull(message = "配送方式不能为空", groups = UpdateGroup.class)
    private Integer devlierType;
    @ApiModelProperty(value = "配送公司ID不能为空")
    private String transportCompanyId;
    @ApiModelProperty(value = "配送公司名称")
    private String transportCompanyName;
    @ApiModelProperty(value = "配送公司电话")
    private String transportCompanyPhone;
    @ApiModelProperty(value = "物流单号")
    private String transportCode;
}
