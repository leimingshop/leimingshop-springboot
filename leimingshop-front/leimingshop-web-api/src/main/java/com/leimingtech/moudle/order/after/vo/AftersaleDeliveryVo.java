/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.after.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 上传物流信息实体
 *
 * @author xuzhch
 * @version V1.0
 * @date 2019/6/20 15:44
 */
@Data
@ApiModel(description = "AftersaleDeliveryVo")
public class AftersaleDeliveryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "售后类型（0:退货,1:换货）")
    private Integer aftersaleType;
    @ApiModelProperty(value = "买家发货时间")
    private Date deliveryTime;
    @ApiModelProperty(value = "买家发货联系电话")
    private String logisticsContactsPhone;
    @ApiModelProperty(value = "买家发货物流公司")
    private String logisticsCompany;
    @ApiModelProperty(value = "买家发货物流单号")
    private String logisticsNumber;

}