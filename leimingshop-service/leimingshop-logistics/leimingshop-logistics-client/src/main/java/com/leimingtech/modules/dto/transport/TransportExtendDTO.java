/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.transport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 运费模板扩展表
 *
 * @author zhangtai
 * @email zhangtai@leimingtech.com
 * @since 1.0.0 2019-05-14
 */
@Data
@ApiModel(description = "TransportExtendDTO")
public class TransportExtendDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "运费模板扩展ID")
    private Long id;
    @ApiModelProperty(value = "平邮py 快递kd EMS es")
    private String type;
    @ApiModelProperty(value = "市级地区ID组成的串，以，隔开，两端也有，")
    private String areaId;
    @ApiModelProperty(value = "省级地区ID组成的串，以，隔开，两端也有，")
    private String topAreaId;
    @ApiModelProperty(value = "地区name组成的串，以，隔开")
    private String areaName;
    @ApiModelProperty(value = "首件数量")
    private Integer snum;
    @ApiModelProperty(value = "首件运费")
    private BigDecimal sprice;
    @ApiModelProperty(value = "续件数量")
    private Integer xnum;
    @ApiModelProperty(value = "续件运费")
    private BigDecimal xprice;
    @ApiModelProperty(value = "是否默认运费1是2否")
    private Integer isDefault;
    @ApiModelProperty(value = "运费模板ID")
    private Long transportId;
    @ApiModelProperty(value = "运费模板")
    private String transportTitle;

}
