/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.mobile.indexmenu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 移动首页菜单实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-11
 */
@Data
@ApiModel(description = "MobileIndexMenuDTO")
public class MobileIndexMenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "移动首页菜单名称")
    private String menuName;

    @ApiModelProperty(value = "跳转类型 url外部链接 coupon优惠券 group拼团 seckill秒杀 flashsale限时抢购")
    private String linkType;

    @ApiModelProperty(value = "分类idPath")
    private String classId;


    @ApiModelProperty(value = "跳转链接url")
    private String url;

    @ApiModelProperty(value = "菜单图标")
    private String menuIcon;

    @ApiModelProperty(value = "是否显示 0不显示 1显示")
    private Integer showFlag;

    @ApiModelProperty(value = "排序优先级")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
