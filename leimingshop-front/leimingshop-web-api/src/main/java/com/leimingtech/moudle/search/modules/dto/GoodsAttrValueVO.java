/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 3:03 PM
 */
@ApiModel
@Data
public class GoodsAttrValueVO {

    private String attrValueName;

    private Long attrValueId;
}
