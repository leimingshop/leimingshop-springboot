/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.hotword;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "HotWordSaveDTO")
public class HotWordSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "热搜词")
    @Size(message = "30个字以内", max = 30, groups = AddGroup.class)
    @NotNull(message = "热搜词不能为空", groups = AddGroup.class)
    private String hotWord;

    @ApiModelProperty(value = "排序字段")
    private int sort;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
