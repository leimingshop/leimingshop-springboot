/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.navigation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 首页导航设置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-06-11
 */
@Data
@ApiModel(value = "首页导航设置")
public class NavigationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "导航主键id")
    private Long id;
    @ApiModelProperty(value = "导航标题")
    private String title;
    @ApiModelProperty(value = "关联类型 1 自定义链接 2 商品分类")
    private Integer relationType;
    @ApiModelProperty(value = "关联值")
    private String relationParams;
    @ApiModelProperty(value = "是否打开新的页面 0 否 1 是")
    private Integer isOpen;
    @ApiModelProperty(value = "排序（0 -255数字越小越靠前）")
    private Integer sort;
    @ApiModelProperty(value = "分类ID")
    private String classIds;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
}