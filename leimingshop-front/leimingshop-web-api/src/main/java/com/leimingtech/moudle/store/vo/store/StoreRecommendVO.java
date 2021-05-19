/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.store.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Data: 2020/5/18 20:54
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel("店铺推荐专区")
public class StoreRecommendVO {
    @ApiModelProperty("推荐名称")
    private String name;

    @ApiModelProperty("推荐图片地址")
    private String imgUrl;

    @ApiModelProperty("副标题")
    private String title;
}
