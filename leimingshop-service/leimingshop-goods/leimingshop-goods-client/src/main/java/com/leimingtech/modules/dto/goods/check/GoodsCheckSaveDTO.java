/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.check;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 商品审核表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "GoodsCheckSaveDTO")
public class GoodsCheckSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品结果，20:审核未通过，30:审核通过,40:违规下架")
    @NotNull(message = "商品结果为必填", groups = AddGroup.class)
    private Integer goodState;

    @ApiModelProperty(value = "原因")
    private String remarks;

    @ApiModelProperty(value = "商品ids")
    @NotNull(message = "商品id为必填", groups = AddGroup.class)
    private Long[] goodsIds;

}