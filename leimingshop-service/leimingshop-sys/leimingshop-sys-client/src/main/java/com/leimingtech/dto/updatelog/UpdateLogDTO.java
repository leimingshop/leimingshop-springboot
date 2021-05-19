/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.updatelog;

import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 版本更新日志
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(description = "UpdateLogDTO")
public class UpdateLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @NotBlank(message = "更新日志标题必填", groups = DefaultGroup.class)
    @ApiModelProperty(value = "更新日志标题")
    private String logTitle;

    @NotBlank(message = "更新版本号必填", groups = DefaultGroup.class)
    @ApiModelProperty(value = "版本号")
    private String logVersion;

    @NotNull(message = "更新日志类型必填", groups = DefaultGroup.class)
    @ApiModelProperty(value = "更新日志类型（0优化迭代；1新增功能）")
    private Integer logType;

    @ApiModelProperty(value = "日志更新内容")
    @NotBlank(message = "更新日志内容必填", groups = DefaultGroup.class)
    private String logDec;

    @ApiModelProperty(value = "发版日期")
    @NotNull(message = "发版日期必填", groups = DefaultGroup.class)
    private Date saveDate;

    @ApiModelProperty(value = "日志创建时间")
    private Date createDate;
}