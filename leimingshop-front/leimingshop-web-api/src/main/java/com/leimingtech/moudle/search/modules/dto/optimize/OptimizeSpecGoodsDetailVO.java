/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto.optimize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 优化版商品详情页实体
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/22 11:48
 **/
@Data
@ApiModel(value = "商品详情页实体")
public class OptimizeSpecGoodsDetailVO {

    @ApiModelProperty(value = "商品详情页结果")
    private OptimizeGoodsDetailsVO goodsDetailsVO;

    @ApiModelProperty(value = "商品规格属性集合，key规格属性id，value规格属性值集合")
    private Map<Long, SpecAttrMsgVO> specAttrMap;

    @ApiModelProperty(value = "用户登录标记 0未登录，1已登录")
    private Integer loginFlag;
}
