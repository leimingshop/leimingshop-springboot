/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.warning;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 库存预警表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-12
 */
@Data
@ApiModel(description = "StorageWarningDTO")
public class StorageWarningDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "库存预警值")
    private Integer storage;
    @ApiModelProperty(value = "是否发送短信（0 发送，1 不发送）")
    private Integer isSendSms;
    @ApiModelProperty(value = "是否启用（0 启用，1禁用）")
    private Integer isEnable;
    @ApiModelProperty("店铺Id")
    private Long storeId;
}