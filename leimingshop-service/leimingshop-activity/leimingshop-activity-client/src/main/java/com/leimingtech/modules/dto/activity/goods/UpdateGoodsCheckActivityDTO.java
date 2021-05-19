/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <编辑商品校验是否参加活动>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/4/1
 */
@Data
@ApiModel(value = "编辑商品校验是否参加活动")
public class UpdateGoodsCheckActivityDTO {

    @ApiModelProperty("参加活动状态 0未参与促销 1参与促销未开始 2参与促销进行中")
    private Integer activityFlag;

    @ApiModelProperty("操作提示信息")
    private String operationMsg;

}
