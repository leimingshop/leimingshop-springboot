/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 规格分组实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@ApiModel(description = "SpecGroupDTO")
public class SpecGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "规格分组名称")
    private String groupName;

    @ApiModelProperty(value = "规格数量")
    private Integer lableNum;

    @ApiModelProperty(value = "规格分组排序")
    private Integer sort;

    @ApiModelProperty(value = "规格分组状态（默认1:启用,2:禁用）")
    private Integer groupStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
