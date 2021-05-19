/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情页商品列表实体
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/11/7 13:49
 **/
@Data
@ApiModel(description = "H5OrderDetailGoodsListDTO")
public class H5OrderDetailGoodsListDTO implements Serializable {

    @ApiModelProperty("店铺Id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("订单留言")
    private String orderMessage;

    @ApiModelProperty("订单商品集合")
    private List<OrderGoodsDTO> orderGoodsDTOList;
}
