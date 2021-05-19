/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 商品详情页实体
 * @Date: 2019/7/23 14:23
 * @Version: V1.0
 */
@Data
@ApiModel(description = "SpecGoodsNameAttrValueVO")
public class SpecGoodsNameAttrValueVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规格id")
    private String specId;

    @ApiModelProperty(value = "规格上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer specShow;

    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;

    @ApiModelProperty(value = "活动库存")
    private Integer activityStorage;

    @ApiModelProperty("活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty("活动状态 （0未开始 1进行中 2已结束）")
    private Integer activityState;

    @ApiModelProperty(value = "规格属性和规格属性值对应关系")
    private List<SpecGoodsValueIdVO> specAttrAndAttrValue;
    @ApiModelProperty(value = "是否删除 0 未删除 1已删除")
    private Integer delFlag;

    @ApiModelProperty(value = "属性值信息（,分隔）")
    private String specAttrValues;

}
