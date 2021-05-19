/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性筛选条件实体
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/7/25 18:00
 **/
@Data
@ApiModel(description = "AttrListDTO")
public class AttrListDTO implements Serializable {

    private static final long serialVersionUID = -636806784587269923L;
    @ApiModelProperty(value = "属性ID")
    private Long attrId;

    @ApiModelProperty(value = "属性值集合")
    private List<String> attrValueList;

}
