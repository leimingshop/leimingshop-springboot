/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品表
 *
 * @author chengqian
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsLiveDTO")
public class GoodsLiveDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "出售中的商品数量")
    private Integer goodsCount;
    @ApiModelProperty(name = "待审核商品数量")
    private Integer auditGoodsCount;
    @ApiModelProperty(name = "审核拒绝商品数量")
    private Integer refusedAuditGoodsCount;
    @ApiModelProperty(name = "售罄商品")
    private Integer sellOutGoods;
    @ApiModelProperty(name = "创建时间")
    private Date createDate;

}
