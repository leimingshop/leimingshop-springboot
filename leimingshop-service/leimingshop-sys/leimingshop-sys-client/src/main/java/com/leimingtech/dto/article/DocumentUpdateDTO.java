/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.article;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 系统文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@ApiModel(description = "DocumentUpdateDTO")
public class DocumentUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @NotNull(message = "ID不能为空", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空", groups = UpdateGroup.class)
    private String docTitle;

    @ApiModelProperty(value = "内容")
    @NotBlank(message = "内容不能为空", groups = UpdateGroup.class)
    private String docContent;

}