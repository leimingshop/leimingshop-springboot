/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo;

import com.leimingtech.modules.dto.cart.CartDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Data: 2020/5/18 16:36
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel("失效购物车集合")
public class DisabledCartVO {

    @ApiModelProperty("店铺ID")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("购物车集合")
    private List<CartDTO> list;


}
