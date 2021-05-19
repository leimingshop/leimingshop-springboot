/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.transport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 物流信息表
 *
 * @author songwenhao
 * @since v1.0.0 2019-08-13
 */
@Data
@ApiModel(description = "TransportFollowDTO")
public class TransportFollowDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配送企业")
    private String comanyName;
    @ApiModelProperty(value = "快递单号")
    private String nu;
    @ApiModelProperty(value = "联系电话")
    private String comanyPhone;
    @ApiModelProperty(value = "快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回")
    private Integer state;
    @ApiModelProperty(value = "是否签收标记")
    private Integer ischeck;
    @ApiModelProperty(value = "物流详细信息")
    private List<TransportProcessDTO> data;


}