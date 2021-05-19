/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 规格及规格值实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@ApiModel(description = "SpecAndSpecValueDTO")
public class SpecAndSpecValueDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "规格名称")
    private String specName;

    @ApiModelProperty(value = "规格值列（所有规格值组合，中间用“,”隔开）")
    private String specValue;

    @ApiModelProperty(value = "规格值")
    private List<SpecValueDTO> specValueDTOList;

}
