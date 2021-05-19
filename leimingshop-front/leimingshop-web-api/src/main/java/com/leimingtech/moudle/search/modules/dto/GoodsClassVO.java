/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 3:01 PM
 */
@Data
@ApiModel(description = "GoodsClassVO")
public class GoodsClassVO implements Serializable {

    private static final long serialVersionUID = 9184960312350749677L;
    @ApiModelProperty("分类ID")
    private Long gcId;

    @ApiModelProperty("分类名称")
    private String gcName;
}
