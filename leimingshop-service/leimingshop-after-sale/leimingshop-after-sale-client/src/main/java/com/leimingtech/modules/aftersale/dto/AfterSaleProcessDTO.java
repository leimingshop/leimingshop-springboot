/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @Author: SWH ab4856812@163.com
 * @Description:H5售后进度DTO
 * @Date: 2019/6/19 22:32
 * @Version: V1.0
 */
@Data
@ApiModel(description = "AfterSaleProcessDTO")
public class AfterSaleProcessDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品图片")
    private String goodsImage;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "审核进度最新记录消息")
    private String lastLog;
    @ApiModelProperty(value = "审核进度集合")
    private List<AftersaleLogDTO> logList;

}