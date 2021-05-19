/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.evaluate;

import com.leimingtech.commons.tools.page.PageData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 信誉评价表
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "H5EvaluateGoodsDTO")
public class H5EvaluateGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论集合")
    private PageData<FindEvaluateGoodsDTO> page;
    @ApiModelProperty(value = "好评率")
    private String reputably;

    @ApiModelProperty("总条数")
    private Integer totalCount;
    @ApiModelProperty("带图评价数量")
    private Integer imgEvaCount;
    @ApiModelProperty("好评数量")
    private Integer goodEvaluate;
    @ApiModelProperty("中评数量")
    private Integer mediumEvaluate;
    @ApiModelProperty("差评数量")
    private Integer negativeEvaluate;
}