/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.stopword;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 修改广告禁语
 *
 * @author chengqian
 * @since v1.0.0 2019-08-21
 */
@Data
@ApiModel(value = "修改广告禁语 ")
public class UpdateStopWordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空", groups = UpdateGroup.class)
    private Long id;
    @ApiModelProperty(value = "名称")
    @NotNull(message = "禁用词名称不能为空", groups = UpdateGroup.class)
    private String name;
}