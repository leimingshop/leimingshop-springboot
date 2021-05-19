/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.favorites;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户收藏表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Data
@ApiModel(description = "FavoritesDTO")
public class FavoritesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "商品规格id不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "商品规格id", required = true)
    private Long specId;

    @ApiModelProperty(value = "商品id", required = true)
    private Long goodsId;

    @NotNull(message = "商品价格不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "收藏时的商品价格", required = true)
    private BigDecimal favPrice;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty("会员ID")
    private Long memberId;

    @ApiModelProperty("会员名称")
    private String memberName;

}
