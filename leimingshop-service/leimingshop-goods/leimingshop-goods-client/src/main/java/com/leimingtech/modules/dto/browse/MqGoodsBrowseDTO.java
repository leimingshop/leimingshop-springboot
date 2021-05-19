/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.browse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * mq保存记录
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-03
 */
@Data
@ApiModel(description = "MqGoodsBrowseDTO")
public class MqGoodsBrowseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "足迹id")
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long memberId;
    @ApiModelProperty(value = "浏览ip")
    private String ip;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品图片")
    private String goodsImage;
    @ApiModelProperty(value = "商品价格")
    private BigDecimal goodsPrice;
    @ApiModelProperty(value = "商品规格Id")
    private Long goodsSpecId;
    @ApiModelProperty(value = "更新人")
    private String updater;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "创建时间（天）")
    private Date createDayTime;
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;
    @ApiModelProperty(value = "商品标签")
    private String labelName;

}
