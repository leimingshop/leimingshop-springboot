/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto.optimize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品详情页-规格属性值VO
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/18 16:19
 **/
@Data
@ApiModel("规格属性值集合")
public class SpecAttrValueVO implements Serializable {

    private static final long serialVersionUID = 1857082009092236754L;

    @ApiModelProperty("规格属性值ID")
    private Long attrValueId;

    @ApiModelProperty("规格属性值名称（红色、M码等）")
    private String attrValueName;

    @ApiModelProperty("是否为主规格（0否，1是）")
    private Integer isMain;

    @ApiModelProperty(value = "图片地址（主图）")
    private String pictureUrl;
}
