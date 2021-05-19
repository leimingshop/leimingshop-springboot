/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 订单物流信息
 * @Date: 2019/8/14 21:38
 * @Version: V1.0
 */
@ApiModel(description = "OrderLogisticsDTO")
@Data
public class OrderLogisticsDTO {

    @ApiModelProperty(value = "商品图片")
    private String goodsPicture;
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "配送企业")
    private String companyName;
    @ApiModelProperty(value = "快递单号")
    private String nu;
    @ApiModelProperty(value = "联系电话")
    private String companyPhone;
    @ApiModelProperty(value = "快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回")
    private Integer state;
    @ApiModelProperty(value = "是否签收标记")
    private Integer ischeck;
    @ApiModelProperty(value = "物流数据")
    private List<LogisticsProcessDTO> data;
}

