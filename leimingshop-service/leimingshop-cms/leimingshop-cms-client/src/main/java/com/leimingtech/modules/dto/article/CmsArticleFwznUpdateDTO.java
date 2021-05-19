/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务指南新增修改文章管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "服务指南新增修改文章管理")
public class CmsArticleFwznUpdateDTO extends CmsArticleFwznSaveDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

}