/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.favorites;

import com.leimingtech.modules.dto.store.StoreDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: weixianchun
 * @Description: 根据id查询信息
 * @Date :2019/7/29 18:05
 * @Version V1.0
 **/
@Data
@ApiModel(description = "StoreFavoritesByStoreIdDTO")
public class StoreFavoritesByStoreIdDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "关注人数")
    private Integer count;
    @ApiModelProperty(value = "是否关注店铺(0:否，1:是)")
    private Integer isFavorites;
    @ApiModelProperty(value = "综合评分")
    private Double sevalAvg;
    @ApiModelProperty(value = "店铺信息")
    private StoreDTO storeDTO;

}