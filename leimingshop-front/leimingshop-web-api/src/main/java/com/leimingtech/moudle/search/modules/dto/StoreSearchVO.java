/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Data: 2019/12/5 10:06
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
public class StoreSearchVO implements Serializable {

    /**
     * 店铺ID
     */
    @ApiModelProperty("店铺ID")
    private Long id;

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String storeName;

    /**
     * 店铺LOGO
     */
    @ApiModelProperty("店铺LOGO")
    private String storeLogo;

    /**
     * 店铺类型
     */
    @ApiModelProperty("店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;

    /**
     * 店铺关注数量
     */
    @ApiModelProperty("店铺等级名称")
    private Integer storeFavNum;

    /**
     * 店铺等级
     */
    @ApiModelProperty("店铺等级")
    private String storeGradeName;

    /**
     * 启用禁用
     */
    @ApiModelProperty(value = "启用禁用 0：启用1：停用")
    private Integer isEnable;

    @ApiModelProperty(value = "店铺星级")
    private Integer storeStar;

    /**
     * 店铺下商品
     */
    @ApiModelProperty(value = "商品信息")
    private List<GoodsVO> goodsVOList;

}
