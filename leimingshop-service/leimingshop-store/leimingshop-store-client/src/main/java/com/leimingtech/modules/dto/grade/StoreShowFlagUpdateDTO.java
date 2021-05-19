/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.grade;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 店铺等级表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-30
 */
@Data
@ApiModel(description = "StoreShowFlagUpdateDTO")
public class StoreShowFlagUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "是否启用（0禁用，为1启用）")
    @NotNull(message = "状态不能为空", groups = UpdateGroup.class)
    private Integer showFlag;


}