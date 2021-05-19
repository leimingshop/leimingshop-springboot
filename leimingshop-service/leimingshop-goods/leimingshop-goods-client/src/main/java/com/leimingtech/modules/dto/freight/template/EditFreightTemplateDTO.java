/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.freight.template;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * 修改你运费模板实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-22
 */
@Data
@ApiModel(description = "EditFreightTemplateDTO")
public class EditFreightTemplateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "模板名称")
    @NotBlank(message = "运费模板名称不能为空", groups = UpdateGroup.class)
    @Length(min = 2, max = 20, message = "运费模板名称长度在2-20位之间", groups = UpdateGroup.class)
    private String templateName;

    @ApiModelProperty(value = "模板类型：0按件数 1按重量")
    @NotNull(message = "运费模板类型不能为空", groups = UpdateGroup.class)
    private Integer templateType;

    @ApiModelProperty(value = "是否默认运费模板：0否 1是")
    @NotNull(message = "是否默认运费模板不能为空", groups = UpdateGroup.class)
    private Integer defaultFlag;

    @ApiModelProperty(value = "运费模板规则集合")
    @NotEmpty(message = "配送区域不能为空", groups = UpdateGroup.class)
    private List<InsertFreightTemplateRuleDTO> freightTemplateRuleList;

}
