/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 标签数量
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Data
@ApiModel(description = "LabelNumDTO")
public class LabelNumDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long groupId;
    @ApiModelProperty(value = "标签数量")
    private Integer labelNum;

}
