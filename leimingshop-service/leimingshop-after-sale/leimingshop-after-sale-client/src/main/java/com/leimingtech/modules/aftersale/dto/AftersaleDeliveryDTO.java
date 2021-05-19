/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;


/**
 * @Author: SWH ab4856812@163.com
 * @Description:front端上传物流信息实体
 * @Date: 2019/6/20 15:44
 * @Version: V1.0
 */
@Data
@ApiModel(description = "AftersaleDeliveryDTO")
public class AftersaleDeliveryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "售后类型（0:退货,1:换货,2:仅退款）")
    private Integer aftersaleType;
    @ApiModelProperty(value = "买家发货时间")
    private Date deliveryTime;
    @ApiModelProperty(value = "买家发货联系电话")
    @Length(max = 30, message = "物流公司长度不可超过30位", groups = AddGroup.class)
    private String logisticsContactsPhone;
    @ApiModelProperty(value = "买家发货物流公司")
    @Length(max = 30, message = "物流公司长度不可超过30位", groups = AddGroup.class)
    private String logisticsCompany;
    @ApiModelProperty(value = "买家发货物流单号")
    @Length(max = 30, message = "物流单号长度不可超过30位", groups = AddGroup.class)
    private String logisticsNumber;

}
