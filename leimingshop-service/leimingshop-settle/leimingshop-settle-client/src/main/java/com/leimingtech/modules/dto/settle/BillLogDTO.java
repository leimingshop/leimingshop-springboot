/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.settle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 对账操作记录
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@ApiModel(description = "BillLogDTO")
public class BillLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "对账单ID")
    private Long billId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "操作人")
    private String operator;
    @ApiModelProperty(value = "操作类型")
    private String operatorType;
    @ApiModelProperty("操作时间")
    private Date createDate;
}