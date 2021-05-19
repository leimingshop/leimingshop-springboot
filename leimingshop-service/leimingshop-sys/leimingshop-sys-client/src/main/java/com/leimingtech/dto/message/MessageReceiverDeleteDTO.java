/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.message;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <站内信接收人删除实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/11/29
 */
@Data
@ApiModel(description = "MessageReceiverDeleteDTO")
public class MessageReceiverDeleteDTO {

    @ApiModelProperty("删除站内信ids")
    @NotEmpty(message = "请选择至少一条站内信", groups = UpdateGroup.class)
    private Long[] ids;

}
