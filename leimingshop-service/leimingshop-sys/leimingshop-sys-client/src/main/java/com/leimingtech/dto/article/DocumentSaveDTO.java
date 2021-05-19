/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.article;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 系统文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@ApiModel(description = "DocumentSaveDTO")
public class DocumentSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标识码")
    @NotBlank(message = "标识码不能为空", groups = AddGroup.class)
    private String docCode;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空", groups = AddGroup.class)
    private String docTitle;

    @ApiModelProperty(value = "内容")
    @NotBlank(message = "内容不能为空", groups = AddGroup.class)
    private String docContent;

}