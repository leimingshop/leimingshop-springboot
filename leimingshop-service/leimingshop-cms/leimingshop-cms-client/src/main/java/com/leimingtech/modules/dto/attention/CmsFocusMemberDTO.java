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
 * @date 2020/1/6 9:46
 */

@Data
@ApiModel(value = "圈子被关注用户信息")
public class CmsFocusMemberDTO {
    @ApiModelProperty(value = "用户ID")
    private Long memberId;

}
