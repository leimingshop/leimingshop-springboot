/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.mobile.indexmenu;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;


/**
 * 编辑移动首页菜单实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-11
 */
@Data
@ApiModel(description = "UpdateMobileIndexMenuDTO")
public class UpdateMobileIndexMenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "移动首页菜单名称")
    @NotBlank(message = "移动首页菜单名称不能为空", groups = UpdateGroup.class)
    @Size(message = "移动楼层菜单名称长度不能超过10", max = 10, groups = UpdateGroup.class)
    private String menuName;

    @ApiModelProperty(value = "跳转类型 url外部链接 coupon优惠券 group拼团 seckill秒杀 flashsale限时抢购")
    @NotBlank(message = "跳转类型不能为空", groups = UpdateGroup.class)
    private String linkType;

    @ApiModelProperty(value = "跳转链接url")
    private String url;

    @ApiModelProperty(value = "分类idPath")
    private String classId;

    @ApiModelProperty(value = "菜单图标")
    @NotBlank(message = "菜单图标不能为空", groups = UpdateGroup.class)
    private String menuIcon;

    @ApiModelProperty(value = "是否显示 0不显示 1显示")
    @NotNull(message = "是否显示不能为空", groups = UpdateGroup.class)
    @Max(value = 1, message = "请填写正确的显示类型", groups = UpdateGroup.class)
    @Min(value = 0, message = "请填写正确的显示类型", groups = UpdateGroup.class)
    private Integer showFlag;

    @ApiModelProperty(value = "排序优先级")
    @NotNull(message = "排序优先级不能为空", groups = UpdateGroup.class)
    @Max(value = 255, message = "排序优先级为0-255之间的整数", groups = UpdateGroup.class)
    @Min(value = 0, message = "排序优先级为0-255之间的整数", groups = UpdateGroup.class)
    private Integer sort;

}
