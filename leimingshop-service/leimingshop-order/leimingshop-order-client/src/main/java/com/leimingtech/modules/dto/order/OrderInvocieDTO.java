/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 订单发票实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@ApiModel(description = "OrderInvocieDTO")
public class OrderInvocieDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "索引ID")
    private Long id;
    @ApiModelProperty(value = "买家ID")
    private Long buyerId;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "发票类型（1:普通发票，2:增值税发票，3:电子发票）")
    private Integer invState;
    @ApiModelProperty(value = "普通发票类型（1:个人发票，2:单位发票）")
    private Integer invType;
    @ApiModelProperty(value = "发票内容")
    private String invContent;
    @ApiModelProperty(value = "个人发票抬头名称")
    private String invPersonal;
    @ApiModelProperty(value = "纳税人识别号")
    private String invCode;
    @ApiModelProperty(value = "注册地址名称")
    private String invRegAddr;
    @ApiModelProperty(value = "注册电话")
    private String invRegPhone;
    @ApiModelProperty(value = "开户银行名称")
    private String invRegBname;
    @ApiModelProperty(value = "银行帐户")
    private String invRegBaccount;
    @ApiModelProperty(value = "收票人姓名")
    private String invRecName;
    @ApiModelProperty(value = "收票人手机号")
    private String invRecMobile;
    @ApiModelProperty(value = "收票人省市区ID（省市区ID中间使用,分割）")
    private String invRecProvince;
    @ApiModelProperty(value = "收票详细地址")
    private String invRecAddr;

}