/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "StoreIndexDTO")
public class StoreIndexDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺认证（默认0未认证，1已认证）")
    private Integer storeAuth;

    @ApiModelProperty(value = "店铺等级ID")
    private Long gradeId;

    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "是否关注（1:已收藏，0:未收藏）")
    private Integer isFavorite;

    @ApiModelProperty(value = "全部商品数量")
    private Integer goodsCount;

    @ApiModelProperty(value = "新品数量")
    private Integer newGoodsCount;


}