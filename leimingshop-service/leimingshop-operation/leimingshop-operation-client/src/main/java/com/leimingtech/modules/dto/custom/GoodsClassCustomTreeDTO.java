/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.custom;

import com.leimingtech.commons.tools.utils.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品自定义分类表
 *
 * @author xuzhch
 * @email 170518@163.dao
 * @since 1.0.0 2019-05-22
 */
@Data
@ApiModel(description = "GoodsClassCustomTreeDTO")
public class GoodsClassCustomTreeDTO extends TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long pid;

    @ApiModelProperty(value = "展示类目名称")
    private String gcName;

}