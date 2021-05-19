/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.goods.goods.vo;

import com.leimingtech.commons.tools.page.PageData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 评价列表
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "评价列表")
public class EvaluateListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论集合")
    private PageData<EvaluateGoodsVO> page;
    @ApiModelProperty(value = "好评率")
    private String reputably;
    @ApiModelProperty("带图评价数量")
    private Integer imgEvaCount;
    @ApiModelProperty("好评数量")
    private Integer goodEvaluate;
    @ApiModelProperty("中评数量")
    private Integer mediumEvaluate;
    @ApiModelProperty("差评数量")
    private Integer negativeEvaluate;
    @ApiModelProperty("总条数")
    private Integer totalCount;

}