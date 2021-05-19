/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author huangkeyuan
 * @Description 高级设置
 * @Date 17:05 2019-05-14
 * @Param
 * @return
 */
@Data
@ApiModel(description = "SettingAftersaleDTO")
public class SettingAftersaleDTO implements Serializable {

    @ApiModelProperty(value = "商品退货(1:开启，0:关闭)")
    private String goodsReturn;

    @ApiModelProperty(value = "买家发起退货申请xx天后，商家未处理，按商家同意退货处理")
    private String agreeReturn;

    @ApiModelProperty(value = "审核同意退货申请xx天后，买家未处理，自动取消退货处理")
    private String cancelReturn;

    @ApiModelProperty(value = "买家已退货xx天后，商家未处理，自动进行退货处理")
    private String autoReturn;

    @ApiModelProperty(value = "商品换货(1:开启，0:关闭)")
    private String goodsBarter;

    @ApiModelProperty(value = "买家发起退货申请xx天后，商家未处理，按商家同意换货处理")
    private String agreeBarter;

    @ApiModelProperty(value = "审核同意退货申请xx天后，买家未处理，自动取消换货处理")
    private String cancelBarter;

    @ApiModelProperty(value = "买家已退货xx天后，商家未处理，平台强制要求商家换货")
    private String autoBarter;

    @ApiModelProperty(value = "退换货物流单号是否必填(1:开启，0:关闭)")
    private String aftersaleExpressSn;

}
