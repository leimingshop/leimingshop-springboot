/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.webfloor;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;


/**
 * h5楼层设置实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Data
@ApiModel(description = "WebFloorDTO")
public class WebFloorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "h5楼层索引ID")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "楼层名称")
    @NotBlank(message = "楼层名称不能为空", groups = AddGroup.class)
    private String floorName;

    @ApiModelProperty("店铺ID")
    private Long storeId;

    @NotBlank(message = "展示风格不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "展示风格")
    private String showType;

    @ApiModelProperty(value = "楼层主题图片")
    private String floorImg;

    @ApiModelProperty(value = "楼层按钮的操作类型字段（link:链接，searchGoodsByClass:分类查询结构列表界面，searchByKeyWord:按关键字检索，goodsDetail:商品详情，xianshiqiangList:限时抢购列表）")
    private String actionType;

    @ApiModelProperty(value = "action_type对应的参数值（若actionType=link,这个字段就是连接地址...）")
    private String actionParams;

    @ApiModelProperty("楼层配置样式 (1 图片 2 商品)")
    private Integer floorStyle;

    @ApiModelProperty(value = "楼层名称图标")
    private String nameIcon;

    @ApiModelProperty(value = "楼层按钮名称")
    private String actionName;

    @ApiModelProperty(value = "是否显示 0否 1是")
    private Integer isShow;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "广告位")
    private String advKey;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty("楼层标识：1 h5 楼层； 2 pc楼层")
    private Integer floorType;

    @ApiModelProperty(value = "商品类别名称")
    private String goodsClassName;

    @ApiModelProperty(value = "展示类别ids")
    private String classIds;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "修改时间")
    private Date updateDate;

    @ApiModelProperty(value = "修改人")
    private String updater;


}