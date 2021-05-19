/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.evaluate;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增商品评价表
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "SaveEvaluateGoodsDTO")
public class SaveEvaluateGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "订单商品表编号")
    @NotNull(message = "订单商品ID不能为空", groups = AddGroup.class)
    private Long orderGoodsId;
    @ApiModelProperty(value = "评价分数（1-5分）")
    @NotNull(message = "评价分数不能为空", groups = AddGroup.class)
    private Integer evaluateScores;
    @ApiModelProperty(value = "信誉评价内容")
    @NotNull(message = "评价内容不能为空", groups = AddGroup.class)
    @Length(max = 500, message = "评价内容最多500字", groups = AddGroup.class)
    private String evaluateContent;
    @ApiModelProperty(value = "晒单图片")
    private String evaluateImage;

}