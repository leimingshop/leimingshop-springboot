/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 查询活动统计
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(value = "查询活动统计")
public class ActivityStatisFindDTO implements Serializable {


    @ApiModelProperty(value = "营销概况")
    private ActivityStatisDTO activityStatisDTO;

    @ApiModelProperty(value = "营销明细")
    private ActivityStatisGoodsPage activityStatisGoodsPage;

    @ApiModelProperty(value = "营销趋势")
    private Map<String, ActivityStatisDTO> map;
}
