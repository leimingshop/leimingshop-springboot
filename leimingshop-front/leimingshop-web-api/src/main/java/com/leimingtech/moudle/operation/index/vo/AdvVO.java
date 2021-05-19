/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.index.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 首页轮播广告
 * @Data: 2020/5/17 23:15
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel("首页轮播广告")
public class AdvVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "广告标题")
    private String advTitle;
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
    @ApiModelProperty(value = "关联目标（链接地址、店铺ID等等）")
    private String relationTarget;
    @ApiModelProperty(value = "关联类型（链接、店铺、活动等等）")
    private String relationType;

}
