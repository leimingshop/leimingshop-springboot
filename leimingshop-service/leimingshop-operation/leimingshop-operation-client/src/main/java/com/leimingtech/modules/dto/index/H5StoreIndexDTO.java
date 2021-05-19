/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.index;

import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreIndexGoodsClassDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorAndLinkDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 * <店铺首页>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Data
@ApiModel(description = "店铺首页")
public class H5StoreIndexDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("h5首页轮播广告")
    private List<AdvDTO> advDTOList;

    @ApiModelProperty("h5首页楼层")
    private List<WebFloorAndLinkDTO> webFloorAndLinkDTOList;

    @ApiModelProperty("店铺商品分类")
    private List<StoreIndexGoodsClassDTO> storeGoodsClass;

}
