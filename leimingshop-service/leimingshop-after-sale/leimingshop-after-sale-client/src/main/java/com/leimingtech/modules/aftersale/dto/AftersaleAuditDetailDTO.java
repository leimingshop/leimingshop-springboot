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
 * @Author: SWH ab4856812@163.com
 * @Description: 售后审核详情
 * @Date: 2019/8/16 16:06
 * @Version: V1.0
 */
@Data
@ApiModel(description = "AftersaleAuditDetailDTO")
public class AftersaleAuditDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "审核id")
    private Long id;
    @ApiModelProperty(value = "申请原因信息")
    private AftersaleApplyDTO aftersaleApplyDTO;
    @ApiModelProperty(value = "商家换货/退货审核信息")
    private List<AftersaleAuditLogDTO> aftersaleAuditLogDTOList;
    @ApiModelProperty(value = "退货商品信息")
    private List<AftersaleGoodsDTO> aftersaleGoodsDTOList;
    @ApiModelProperty(value = "退货操作日志信息")
    private List<AftersaleLogListDTO> aftersaleLogListDTOList;

}
