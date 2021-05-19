/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 圈子新增修改文章管理DTO
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2019-01-15
 */
@Data
@ApiModel(value = "圈子新增修改文章管理")
public class CmsFrontArticleUpdateDTO extends CmsFrontArticlePublishDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

}
