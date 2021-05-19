/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <活动去凑单实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/16
 */
@Data
@ApiModel(description = "GoCollectBillsDTO")
public class GoCollectBillsDTO {

    @ApiModelProperty("满减活动id")
    @NotNull(message = "满减活动id不能为空", groups = AddGroup.class)
    private Long activityId;

    @ApiModelProperty(value = "每页显示条数", required = true)
    @NotNull(message = "每页显示条数不能为空", groups = AddGroup.class)
    private Integer pageSize;

    @ApiModelProperty(value = "第几页", required = true)
    @NotNull(message = "第几页不能为空", groups = AddGroup.class)
    private Integer pageNo;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序类型：DESC倒叙，ASC正序", required = true)
    private String sortType;

    @ApiModelProperty(value = "凑单类型 0立即购买去结算页凑单 1.立即购买凑单页勾选商品 2.购物车结算去凑单页 3.其他进入结算页", required = true)
    @NotNull(message = "凑单类型不能为空", groups = AddGroup.class)
    private Integer collectBillType;

    @ApiModelProperty("凑单前价格")
    private BigDecimal beforeCollectAmount;

    @ApiModelProperty("凑单前商品skuID")
    private Long beforeCollectSpecId;

    @ApiModelProperty("凑单前商品数量")
    private Integer beforeCollectNum;

    @ApiModelProperty("总条数")
    private Long totalNum;

}
