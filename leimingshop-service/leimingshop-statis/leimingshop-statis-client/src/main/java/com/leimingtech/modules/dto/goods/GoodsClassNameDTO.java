/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类信息
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
public class GoodsClassNameDTO implements Serializable {

    @ApiModelProperty(value = "分类ID")
    private Long classId;

    @ApiModelProperty(value = "分类名称")
    private String className;

}
