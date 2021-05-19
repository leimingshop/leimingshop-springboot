/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author weixianchun
 * @Description seller商家备注实体
 * @Date 2019/11/11 10:01
 */
@Data
@ApiModel(description = "OrderSellerRemarkDTO")
public class OrderSellerRemarkDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商家备注等级")
    private Integer sellerRemarkGrade;

    @ApiModelProperty(value = "商家备注")
    private String sellerRemark;
}
