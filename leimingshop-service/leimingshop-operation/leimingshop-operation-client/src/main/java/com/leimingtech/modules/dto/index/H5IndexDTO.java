/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.index;

import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.goods.RecommendGoodsDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.MobileIndexMenuDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorAndLinkDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 * <h5首页展示实体>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/15 14:05
 **/
@Data
@ApiModel(description = "H5IndexDTO")
public class H5IndexDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("h5首页轮播广告")
    private List<AdvDTO> advDTOList;

    @ApiModelProperty("h5首页楼层")
    private List<WebFloorAndLinkDTO> webFloorAndLinkDTOList;

    @ApiModelProperty("推荐商品")
    private List<RecommendGoodsDTO> goodsDTOList;

    @ApiModelProperty("移动楼层菜单")
    private List<MobileIndexMenuDTO> mobileIndexMenuDTOList;
}
