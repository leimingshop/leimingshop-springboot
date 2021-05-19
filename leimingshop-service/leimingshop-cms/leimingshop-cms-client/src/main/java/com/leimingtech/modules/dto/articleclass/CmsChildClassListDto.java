/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articleclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhangyuhao
 * @email 2625024471@qq.com
 * @date 2020/1/7 20:29
 */
@Data
@ApiModel("子分类下拉框展示对象")
public class CmsChildClassListDto {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 分类名
     */
    @NotNull(message = "分类名称不能为空")
    @ApiModelProperty(value = "分类名称")
    private String acName;
}
