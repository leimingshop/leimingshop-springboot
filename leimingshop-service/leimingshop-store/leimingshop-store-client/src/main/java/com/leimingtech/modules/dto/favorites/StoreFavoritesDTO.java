/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.favorites;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 用户店铺收藏表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Data
@ApiModel(description = "StoreFavoritesDTO")
public class StoreFavoritesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

}