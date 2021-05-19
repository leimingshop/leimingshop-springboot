/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.hotword;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@ApiModel(description = "HotWordUpdateDTO")
public class HotWordUpdateDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "热搜词")
    @Size(message = "30个字以内", max = 30, groups = UpdateGroup.class)
    private String hotWord;

    @ApiModelProperty(value = "排序字段")
    private int sort;

    @ApiModelProperty(value = "修改时间")
    private Date updateDate;
}
