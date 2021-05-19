/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto.optimize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品详情页-规格信息VO
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/18 16:43
 **/
@Data
@ApiModel("商品详情页-规格信息VO")
public class OptimizeSpecVO implements Serializable {
    private static final long serialVersionUID = -6494967803619541292L;

    @ApiModelProperty("规格ID")
    private Long specId;

    @ApiModelProperty(value = "规格上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer specShow;

    @ApiModelProperty(value = "删除标记（默认:0未删除,1已删除）")
    private Integer delFlag;

    @ApiModelProperty(value = "规格库存(无活动时展示普通商品库存，存在活动时展示活动库存)")
    private Integer specStorage;

    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;

    @ApiModelProperty("活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;
}
