/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.after.template;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 售后模板
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-17
 */
@Data
@ApiModel(description = "AfterTemplateDTO")
public class AfterTemplateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "更新修改售后模板的时候，id必须传", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "内容")
    private String value;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "修改时间")
    private Date updateDate;

}
