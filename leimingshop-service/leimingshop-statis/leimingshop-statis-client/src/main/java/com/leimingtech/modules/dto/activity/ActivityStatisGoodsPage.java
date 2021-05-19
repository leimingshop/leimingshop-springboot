/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 营销明细分页
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(value = "营销明细分页")
public class ActivityStatisGoodsPage implements Serializable {


    @ApiModelProperty(value = "总数量")
    private Integer total;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "每页显示多少条")
    private Integer limit;

    @ApiModelProperty(value = "活动商品信息分页")
    private List<ActivityDetailStatisDTO> detailStatisDTOList;


}
