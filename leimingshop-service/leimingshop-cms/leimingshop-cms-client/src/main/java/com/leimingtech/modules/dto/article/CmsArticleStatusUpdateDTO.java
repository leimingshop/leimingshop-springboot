/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章状态修改DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "文章状态修改")
public class CmsArticleStatusUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "状态类型（1:置顶标识，2:头条标识，3:状态标识，4:评论标识，5:精华帖）")
    private Integer type;

    @ApiModelProperty(value = "状态值（0:关闭 1:开启）")
    private Integer code;
}