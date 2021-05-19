/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.coupons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <front优惠券列表店铺分组实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */
@Data
@ApiModel(description = "FrontCouponsActivityStoreGroupDTO")
public class FrontCouponsActivityStoreGroupDTO {

    @ApiModelProperty("店铺ID")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("店铺logo")
    private String storeLogo;

    @ApiModelProperty("优惠券活动集合")
    private List<FrontCouponsActivityPageDTO> frontCouponsActivityPageDTOList;

}
