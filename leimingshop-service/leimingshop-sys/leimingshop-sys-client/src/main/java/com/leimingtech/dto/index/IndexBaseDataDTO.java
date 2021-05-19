/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.index;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xuzhch
 * @className IndexBaseDataDTO
 * @description 首页基础概况
 * @date 2020/3/17/017
 */
@Data
public class IndexBaseDataDTO implements Serializable {

    @ApiModelProperty("新增商品数量")
    private Integer newGoodsCount;

    @ApiModelProperty("所有商品数量")
    private Integer goodsCount;

    @ApiModelProperty("新增会员数量")
    private Integer newMemberCount;

    @ApiModelProperty("所有会员数量")
    private Integer memberCount;

    @ApiModelProperty("平台所选时间内全部的订单金额")
    private BigDecimal subOrderAmount;

    @ApiModelProperty("平台所选时间内全部的订单数量")
    private Integer orderCount;

    @ApiModelProperty("新增申请商户数量")
    private Integer newStoreCount;

    @ApiModelProperty("所有商户数量")
    private Integer storeCount;

    @ApiModelProperty("上次更新时间")
    private Date updateDate;
}
