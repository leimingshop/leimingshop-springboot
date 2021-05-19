/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.evaluate;

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
@ApiModel(description = "UpdateEvaluateGoodsDTO")
public class UpdateEvaluateGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评价主键ID集合")
    private Long[] ids;

    @ApiModelProperty(value = "评价信息的状态 0为正常 1为禁止显示")
    private Integer state;

    @ApiModelProperty(value = "读取状态 默认0未读 1已读")
    private Integer readState;

}
