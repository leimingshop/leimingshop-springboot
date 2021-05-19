/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;


/**
 * 系统设置表
 *
 * @author kuangweiguo
 * @email kuangweiguo@leimingtech.com
 * @since 1.0.0 2019-05-10
 */
@Data
@ApiModel(description = "SettingDTO")
public class SettingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updateDate;

}