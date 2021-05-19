/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.favorities.vo.favorites;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户店铺收藏表
 *
 * @author chengqian
 * @since 1.0.0 2019-05-15
 */
@Data
@ApiModel(description = "用户店铺收藏表")
public class StoreFavoritesPageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    @ApiModelProperty(value = "店铺收藏数")
    private String storeCollection;

    @ApiModelProperty(value = "店铺类型 1 自营 2 普通")
    private String storeType;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
