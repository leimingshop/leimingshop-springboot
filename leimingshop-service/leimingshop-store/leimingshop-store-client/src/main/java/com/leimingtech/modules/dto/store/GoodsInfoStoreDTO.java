/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品详情页店铺信息
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019年6月25日
 */
@Data
@ApiModel(description = "GoodsInfoStoreDTO")
public class GoodsInfoStoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺ID")
    private Long id;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺认证（默认0未认证，1已认证）")
    private Integer storeAuth;

    @ApiModelProperty(value = "店铺等级ID")
    private Long gradeId;

    @ApiModelProperty(value = "店铺关注人数")
    private Integer favoriteCount;

    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;

    @ApiModelProperty(value = "是否关注（1:已收藏，0:未收藏）")
    private Integer isFavorite;

    @ApiModelProperty(value = "店铺综合评分")
    private Double synthesizeEvaluate;
}