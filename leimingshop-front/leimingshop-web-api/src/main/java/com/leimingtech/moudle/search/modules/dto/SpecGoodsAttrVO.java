/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author tyl
 * @Date 2019/7/4 14:43
 * @Description
 **/
@Data
@ApiModel(description = "SpecGoodsAttrVO")
public class SpecGoodsAttrVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "属性名")
    private String attrName;

    @ApiModelProperty(value = "商品规格属性值")
    private String attrValueName;

    @ApiModelProperty(value = "展示类型（默认0单选，1下拉框，2多选）")
    private Integer showType;


}
