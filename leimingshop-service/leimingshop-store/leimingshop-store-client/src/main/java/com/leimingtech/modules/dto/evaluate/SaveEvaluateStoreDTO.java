/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.evaluate;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 新增店铺评分表
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "SaveEvaluateStoreDTO")
public class SaveEvaluateStoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "店铺编号")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "买家编号")
    private Long memberId;
    @ApiModelProperty(value = "买家名称")
    private String memberName;
    @ApiModelProperty(value = "描述相符评分")
    @NotNull(message = "服务态度评分不能为null", groups = AddGroup.class)
    private Double sevalDesccredit;
    @NotNull(message = "服务态度评分不能为null", groups = AddGroup.class)
    @ApiModelProperty(value = "服务态度评分")
    private Double sevalServicecredit;
    @NotNull(message = "发货速度评分不能为null", groups = AddGroup.class)
    @ApiModelProperty(value = "发货速度评分")
    private Double sevalDeliverycredit;

}