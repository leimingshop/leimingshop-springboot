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
 * 查询购物车
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Data
@ApiModel(description = "FindCartDTO")
public class FindCartDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "是否全选(0 未选  1全选)")
    private Integer isSelectAll;
    @ApiModelProperty(value = "最新时间")
    private Long updateDate;
    @ApiModelProperty(value = "是否可领券 0否 1是")
    private Integer couponsFlag;

    @ApiModelProperty(value = "当前店铺下的活动购物车分组集合")
    private List<ActivityCartGroupDTO> activityCartGroupDTOList;

    public static int compare(FindCartDTO f1, FindCartDTO f2) {
        return f2.getUpdateDate().compareTo(f1.getUpdateDate());
    }
}
