/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.complain;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 处理投诉
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "处理投诉")
public class DisposeOrderComplainDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "投诉判定 1：有效投诉，2：重点问题，3：无效投诉")
    @NotNull(message = "投诉判定不能为空", groups = UpdateGroup.class)
    private Integer verdict;

    @ApiModelProperty(value = "回复内容")
    @NotNull(message = "回复内容不能为空", groups = UpdateGroup.class)
    @Length(max = 500, message = "回复内容最多500位", groups = AddGroup.class)
    private String replyContent;

    @ApiModelProperty(value = "处理备注")
    @Length(max = 500, message = "处理备注最多500位", groups = AddGroup.class)
    private String remark;

}