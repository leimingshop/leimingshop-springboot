/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articleclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * H5热门分类列表DTO
 *
 * @author pixiaoyong yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "H5分类列表DTO")
public class CmsFrontClassDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "分类图标")
    private String imageUrl;

    @ApiModelProperty(value = "分类icon")
    private String acIcon;

}
