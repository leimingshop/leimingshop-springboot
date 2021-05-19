/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 订单退货
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleApplyDetailDTO")
public class AftersaleApplyDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申请原因信息")
    private AftersaleApplyDTO aftersaleApplyDTO;
    @ApiModelProperty(value = "商家换货/退货审核信息")
    private List<AftersaleAuditLogDTO> aftersaleAuditLogDTOList;
    @ApiModelProperty(value = "退货商品信息")
    private List<AftersaleGoodsDTO> aftersaleGoodsDTOList;
    @ApiModelProperty(value = "退货操作日志信息")
    private List<AftersaleLogListDTO> aftersaleLogListDTOList;

}
