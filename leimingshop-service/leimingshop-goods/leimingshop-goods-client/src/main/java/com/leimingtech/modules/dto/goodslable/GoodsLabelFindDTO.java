/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 商品标签, 分组回显实体
 * @Date :2019/5/20 14:28
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsLabelFindDTO")
public class GoodsLabelFindDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签ID")
    private Long id;

    @ApiModelProperty(value = "商品标签名")
    private String labelName;

    @ApiModelProperty(value = "关联的分组ID")
    private List<Long> groupIds;

}
