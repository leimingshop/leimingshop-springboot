/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.evaluate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 回复评价表
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "ReplyEvaluateGoodsDTO")
public class ReplyEvaluateGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评价主键ID")
    private Long id;
    @ApiModelProperty(value = "回复内容")
    private String content;
}