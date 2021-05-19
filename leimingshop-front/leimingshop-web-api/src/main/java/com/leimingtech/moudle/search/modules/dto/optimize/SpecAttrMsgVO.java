/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto.optimize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lixiang
 * @version V1.0
 * @date 2020/5/18 20:33
 **/
@Data
@ApiModel("规格属性信息")
public class SpecAttrMsgVO implements Serializable {
    private static final long serialVersionUID = 756623036354422298L;

    @ApiModelProperty("规格属性名称（颜色、尺码等）")
    private String specAttrName;

    @ApiModelProperty("规格属性值集合")
    private List<SpecAttrValueVO> specAttrValueVOList;
}
