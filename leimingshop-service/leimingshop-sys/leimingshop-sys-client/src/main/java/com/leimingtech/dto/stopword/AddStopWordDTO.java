/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.stopword;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 保存广告禁语
 *
 * @author chengqian
 * @since v1.0.0 2019-08-21
 */
@Data
@ApiModel(value = "保存广告禁语 ")
public class AddStopWordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "禁用词名称不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "敏感词编码")
    private String banCode;

}