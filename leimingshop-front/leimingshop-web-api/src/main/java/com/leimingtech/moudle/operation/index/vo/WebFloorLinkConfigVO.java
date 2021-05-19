/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 楼层图片链接
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Data
@ApiModel(description = "楼层图片链接")
public class WebFloorLinkConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "h5楼层图片链接索引")
    private Long id;

    @ApiModelProperty(value = "h5楼层索引")
    private Long floorId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "跳转到不同的页面所需要的字段")
    private String typeKeyWord;

    @ApiModelProperty(value = "图片url")
    private String imgUrl;

    @ApiModelProperty(value = "链接类型：link:链接，searchGoodsByClass:分类查询结构列表界面，searchByKeyWord:按关键字检索，goodsDetail:商品详情，xianshiqiangList:限时抢购列表")
    private String linkType;

    @ApiModelProperty(value = "位置标识(第几张图片)")
    private String imgMarking;

    @ApiModelProperty(value = "图片所属的样式标识")
    private String typeShowMarking;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品类别名称")
    private String gcName;

    @ApiModelProperty(value = "展示类别ids")
    private String classIds;


}