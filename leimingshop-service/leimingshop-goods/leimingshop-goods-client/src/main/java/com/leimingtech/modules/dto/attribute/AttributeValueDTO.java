/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.attribute;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 属性值实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Data
@ApiModel(description = "AttributeValueDTO")
public class AttributeValueDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "所属属性id")
    private Long attrId;

    @ApiModelProperty(value = "属性值名称")
    private String attrValueName;

    @ApiModelProperty(value = "属性值图片")
    private String attrValueImage;

    @ApiModelProperty(value = "属性值排序")
    private Integer attrValueSort;

}
