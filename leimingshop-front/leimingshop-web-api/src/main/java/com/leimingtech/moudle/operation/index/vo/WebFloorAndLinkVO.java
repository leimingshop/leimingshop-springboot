/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 楼层设置及楼层商品信息
 *
 * @author chenqgian
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Data
@ApiModel(description = "楼层设置及楼层商品信息")
public class WebFloorAndLinkVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "楼层索引ID")
    private Long id;

    @ApiModelProperty(value = "楼层名称")
    private String floorName;

    @ApiModelProperty(value = "展示风格")
    private String showType;

    @ApiModelProperty(value = "楼层主题图片")
    private String floorImg;

    @ApiModelProperty(value = "楼层按钮的操作类型字段（link:链接，searchGoodsByClass:分类查询结构列表界面，searchByKeyWord:按关键字检索，goodsDetail:商品详情，xianshiqiangList:限时抢购列表）")
    private String actionType;

    @ApiModelProperty(value = "action_type对应的参数值（若actionType=link,这个字段就是连接地址...）")
    private String actionParams;

    @ApiModelProperty(value = "楼层名称图标")
    private String nameIcon;

    @ApiModelProperty(value = "楼层按钮名称")
    private String actionName;

    @ApiModelProperty(value = "广告位")
    private String advKey;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品类别名称")
    private String goodsClassName;

    @ApiModelProperty(value = "展示类别ids")
    private String classIds;

    @ApiModelProperty(value = "楼层对应图片跳转路径")
    private List<WebFloorLinkConfigVO> webFloorLinkConfigDTOList;

    @ApiModelProperty(value = "楼层广告集合")
    private List<AdvVO> advDTOList;

    @ApiModelProperty(value = "店铺楼层关联的商品")
    private List<FloorGoodsVO> goodsVOList;

    @ApiModelProperty("楼层样式")
    private Integer floorStyle;


}
