/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import com.leimingtech.moudle.search.modules.dto.optimize.SpecAttrMsgVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 商品详情页实体
 * @Date: 2019/7/23 14:23
 * @Version: V1.0
 */
@Data
@ApiModel(description = "SpecGoodsDetailVO")
public class SpecGoodsDetailVO implements Serializable {

    private static final long serialVersionUID = -5997277294626858308L;

    @ApiModelProperty(value = "商品详情页结果")
    private GoodsDetailsVO goodsDetailsVO;

    @ApiModelProperty(value = "商品规格属性和属性值对应关系")
    private List<SpecGoodsNameAttrValueVO> goodsSpecList;

    @ApiModelProperty(value = "商品规格属性名集合（废弃）")
    private List<GoodsSpecAttr> goodsSpecNameValue;

    @ApiModelProperty(value = "商品规格属性集合，key规格属性id，value规格属性值集合")
    private Map<Long, SpecAttrMsgVO> specAttrMap;

    @ApiModelProperty(value = "用户登录标记 0未登录，1已登录")
    private Integer loginFlag;
}
