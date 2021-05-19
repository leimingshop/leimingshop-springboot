/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.webfloor;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;


/**
 * h5楼层设置及图片链接实体（修改）
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-13
 */
@Data
@ApiModel(description = "UpdateWebFloorAndLinkDTO")
public class UpdateWebFloorAndLinkDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "h5楼层索引ID")
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "楼层名称")
    @NotBlank(message = "楼层名称不能为空", groups = UpdateGroup.class)
    @Size(max = 20, message = "楼层名称长度不大于20", groups = UpdateGroup.class)
    private String floorName;

    @NotBlank(message = "链接类型不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "楼层按钮的操作类型字段（link:链接，searchGoodsByClass:分类查询结构列表界面，searchByKeyWord:按关键字检索，goodsDetail:商品详情，xianshiqiangList:限时抢购列表）")
    private String actionType;

    @NotBlank(message = "跳转类型关键字不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "action_type对应的参数值（若actionType=link,这个字段就是连接地址...）")
    private String actionParams;

    @ApiModelProperty(value = "楼层名称图标")
    @Size(max = 100, message = "楼层名称图标长度不大于100", groups = UpdateGroup.class)
    private String nameIcon;

    @ApiModelProperty(value = "是否显示 0否 1是")
    @Min(value = 0, message = "请输入正确的显示类型", groups = UpdateGroup.class)
    @Max(value = 1, message = "请输入正确的显示类型", groups = UpdateGroup.class)
    private Integer isShow;

    @ApiModelProperty(value = "排序")
    @Min(value = 0, message = "排序范围为0-255", groups = UpdateGroup.class)
    @Max(value = 255, message = "排序范围为0-255", groups = UpdateGroup.class)
    private Integer sort;

    @ApiModelProperty(value = "商品名称")
    @Size(max = 55, message = "商品名称长度不大于55", groups = AddGroup.class)
    private String goodsName;

    @ApiModelProperty(value = "商品类别名称")
    @Size(max = 32, message = "商品类别名称长度不大于55", groups = AddGroup.class)
    private String goodsClassName;

    @ApiModelProperty(value = "展示类别ids")
    private String classIds;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty("楼层样式")
    private Integer floorStyle;

    @ApiModelProperty(value = "楼层图片链接")
    private List<InsertWebFloorLinkConfigDTO> webFloorLinkConfigDTOList;


}