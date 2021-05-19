/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.attribute;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性分组及关联属性实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@ApiModel(description = "AttrGroupAndAttrDTO")
public class AttrGroupAndAttrDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "属性分组名称")
    private String groupName;

    @ApiModelProperty(value = "属性数量")
    private Integer lableNum;

    @ApiModelProperty(value = "属性分组状态（默认1:启用,2:禁用）")
    private Integer groupStatus;

    @ApiModelProperty(value = "关联属性列表")
    private List<AttributeDTO> attrDTOList;

}
