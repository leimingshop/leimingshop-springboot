/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articleclass;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pixiaoyong@leimingtech.com
 * @date 2020/4/1 9:44
 * @email 1609973595@qq.com
 */
@Data
public class CmsClassIdDTO {

    @ApiModelProperty(value = "主键")
    private Long[] id;

}
