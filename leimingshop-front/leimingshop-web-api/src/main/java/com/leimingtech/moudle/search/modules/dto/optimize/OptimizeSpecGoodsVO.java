/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto.optimize;

import com.leimingtech.moudle.search.modules.dto.SpecGoodsAttrVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 商品规格信息
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/22 11:55
 **/
@Data
@ApiModel(value = "优化版-商品规格信息")
public class OptimizeSpecGoodsVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规格id")
    private Long id;

    @ApiModelProperty("分类id")
    private Long gcId;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("商品对应的规格Id")
    private Long specId;

    @ApiModelProperty("分类名称")
    private String gcName;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("店铺头像")
    private String storeLogo;

    @ApiModelProperty("店铺状态 0 启用 1 禁用")
    private Integer storeIsEnable;

    @ApiModelProperty("店铺类型 1 自营 2 普通")
    private String storeType;

    @ApiModelProperty("店铺等级")
    private String storeGrade;

    @ApiModelProperty("商品属性集合")
    private List<SpecGoodsAttrVO> goodsAttrVOList;

    @ApiModelProperty("规格集合, key为规格属性值id组合而成，中间用-隔开，value为规格信息")
    private Map<String, OptimizeSpecVO> goodsSpecList;
}
