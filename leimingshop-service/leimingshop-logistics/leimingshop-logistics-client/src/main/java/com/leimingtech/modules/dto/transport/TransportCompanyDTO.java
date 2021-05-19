/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.transport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 物流公司表
 *
 * @author songwenhao
 * @since v1.0.0 2019-08-13
 */
@Data
@ApiModel(description = "TransportCompanyDTO")
public class TransportCompanyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "快递公司ID")
    private Long id;
    @ApiModelProperty(value = "快递公司编码")
    private String companyId;
    @ApiModelProperty(value = "快递公司名称")
    private String companyName;
    @ApiModelProperty(value = "快递公司电话")
    private String companyPhone;
    @ApiModelProperty(value = "快递公司网址")
    private String site;
}