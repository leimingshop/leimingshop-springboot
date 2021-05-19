/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.reduce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 满减活动详情查询实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@ApiModel(description = "ReduceActivityDeatilDTO")
public class ReduceActivityDeatilDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "满减活动名称")
    private String activityName;
    @ApiModelProperty("活动规则名称")
    private String activityRuleName;
    @ApiModelProperty(value = "活动规则 0普通满减 1每满减 2阶梯满减")
    private Integer ruleType;
    @ApiModelProperty(value = "活动开始时间")
    private Date startDate;
    @ApiModelProperty(value = "活动结束时间")
    private Date endDate;
    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;
    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;

    @ApiModelProperty("满减规则集合")
    private List<ReduceRuleSaveDTO> reduceRuleSaveDTOList;

    @ApiModelProperty(value = "满减关联商品集合")
    private List<ReduceGoodsDTO> reduceGoodsDTOList;

}
