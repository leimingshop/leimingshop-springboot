/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <admin订单完成进度实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/11/12
 */
@Data
@ApiModel(description = "AdminOrderTimeDTO")
public class AdminOrderTimeDTO {

    @ApiModelProperty("操作信息")
    private String orderOperation;

    @ApiModelProperty("操作时间")
    private Date operationDate;

}
