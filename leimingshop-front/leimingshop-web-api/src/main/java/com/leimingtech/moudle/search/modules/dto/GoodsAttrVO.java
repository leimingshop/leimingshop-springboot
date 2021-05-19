/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 3:03 PM
 */
@ApiModel(description = "GoodsAttrVO")
@Data
public class GoodsAttrVO implements Serializable {

    private static final long serialVersionUID = 5953212216548558365L;
    @ApiModelProperty("属性ID")
    private Long attrId;

    @ApiModelProperty("属性名称")
    private String attrName;

    @ApiModelProperty("属性值信息")
    private List<Map<String, String>> goodsAttrValueVOList;

}
