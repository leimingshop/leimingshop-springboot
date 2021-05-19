/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order.freight;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 运费规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-22
 */
@Data
@ApiModel(description = "FreightRuleDTO")
public class FreightRuleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty(value = "规则类型：0叠加运费 1最高运费 2最低运费 3智能组合")
    private Integer ruleType;

}
