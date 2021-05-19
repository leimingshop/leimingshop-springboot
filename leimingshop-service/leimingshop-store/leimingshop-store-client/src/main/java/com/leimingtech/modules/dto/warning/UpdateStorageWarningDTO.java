/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.warning;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 库存预警表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-12
 */
@Data
@ApiModel(description = "UpdateStorageWarningDTO")
public class UpdateStorageWarningDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @NotNull(message = "id不能为空", groups = UpdateGroup.class)
    private Long id;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "库存预警值")
    @NotNull(message = "预警值不能为空", groups = UpdateGroup.class)
    private Integer storage;
    @ApiModelProperty(value = "是否发送短信（0 发送，1 不发送）")
    private Integer isSendSms;
    @ApiModelProperty(value = "是否启用（0 启用，1禁用）")
    private Integer isEnable;
    @ApiModelProperty("店铺Id")
    private Long storeId;
}