/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车商品降价提醒
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Data
@ApiModel(description = "CartRemindDTO")
public class CartRemindDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户集合")
    private List<Long> memberList;
    @ApiModelProperty(value = "规格名称")
    private String specName;
    @ApiModelProperty(value = "修改人")
    private String updateName;
}
