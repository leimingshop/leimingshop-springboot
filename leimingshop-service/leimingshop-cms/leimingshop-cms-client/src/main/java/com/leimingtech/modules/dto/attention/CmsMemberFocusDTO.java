/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.attention;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyuhao
 * @email 2625024471@qq.com
 * @date 2020/1/6 9:31
 */

@Data
@ApiModel(value = "用户关注圈子信息")
public class CmsMemberFocusDTO {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "被关注的圈子ID")
    private Long circleId;
}
