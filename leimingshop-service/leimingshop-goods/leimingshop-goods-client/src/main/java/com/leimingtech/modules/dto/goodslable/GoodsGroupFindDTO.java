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
 * @Description: 商品标签, 分组回显实体(分组表)
 * @Date :2019/5/20 14:28
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsGroupFindDTO")
public class GoodsGroupFindDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分组ID")
    private Long id;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "标签分组排序")
    private Integer sort;

    @ApiModelProperty(value = "关联的标签ID")
    private List<Long> labelId;

}
