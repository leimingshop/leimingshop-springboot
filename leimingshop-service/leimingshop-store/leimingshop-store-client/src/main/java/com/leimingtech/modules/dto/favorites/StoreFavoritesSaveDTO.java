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


/**
 * 用户店铺收藏表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Data
@ApiModel(description = "StoreFavoritesSaveDTO")
public class StoreFavoritesSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺ID")
    @NotNull(message = "店铺ID不能为空", groups = AddGroup.class)
    private Long storeId;

}