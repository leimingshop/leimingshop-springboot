/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.after.template;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 售后模板
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-17
 */
@Data
@ApiModel(description = "AfterTemplateSaveDTO")
public class AfterTemplateSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "名称不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "名称")
    @Length(min = 0, max = 64, message = "最多可输入64个字符")
    private String name;

    @NotNull(message = "内容不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "内容")
    private String value;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

}
