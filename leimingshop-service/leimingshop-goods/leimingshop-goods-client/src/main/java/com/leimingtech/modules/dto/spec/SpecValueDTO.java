/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 规格值实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@ApiModel(description = "SpecValueDTO")
public class SpecValueDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "所属规格id")
    private Long specId;

    @ApiModelProperty(value = "规格值名称")
    private String specValueName;

    @ApiModelProperty(value = "规格图片")
    private String specValueImage;

    @ApiModelProperty(value = "规格值排序")
    private Integer specValueSort;

}
