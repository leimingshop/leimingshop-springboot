/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.after.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 售后进度
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020年5月19日18:09:47
 */
@Data
@ApiModel(description = "AfterSaleProcessVo")
public class AfterSaleProcessVo implements Serializable {
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
    private List<AftersaleLogVo> logList;

}