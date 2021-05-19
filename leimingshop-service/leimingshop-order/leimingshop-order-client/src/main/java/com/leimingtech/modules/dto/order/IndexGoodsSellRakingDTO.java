/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xuzhch
 * @className IndexGoodsSellRakingDTO
 * @description 首页商品销量排行数据
 * @date 2020/3/17/017
 */
@Data
@ApiModel(description = "IndexGoodsSellRakingDTO")
public class IndexGoodsSellRakingDTO implements Serializable {
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品销量")
    private Integer sellGoodsCount;
    @ApiModelProperty(value = "商品销售额")
    private BigDecimal sellGoodsAmount;
    @ApiModelProperty("刷新世界")
    private Date updateDate;

}
