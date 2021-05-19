/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto.optimize;


import com.leimingtech.modules.dto.coupons.FrontCouponsActivityPageDTO;
import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;
import com.leimingtech.moudle.search.modules.dto.GoodsLabel;
import com.leimingtech.moudle.search.modules.dto.GoodsSpecPicVO;
import com.leimingtech.moudle.search.modules.dto.SpecActivityVO;
import com.leimingtech.moudle.search.modules.dto.SpecGoodsPicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 规格信息
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/22 11:52
 **/
@ApiModel(value = "优化版-商品信息")
@Data
public class OptimizeGoodsDetailsVO implements Serializable {

    private static final long serialVersionUID = 6687635727654733584L;

    @ApiModelProperty(value = "规格id")
    private Long id;

    @ApiModelProperty(value = "规格名称")
    private String specName;

    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品上下架（0下架状态，1上架状态，2未上架）")
    private Integer goodsShow;

    @ApiModelProperty(value = "库存")
    private Integer specStorage;

    @ApiModelProperty(value = "售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品规格属性值名称（中间用逗号隔开，红色,S码）")
    private String specAttrName;

    @ApiModelProperty(value = "商品标签")
    private List<GoodsLabel> goodsLabels;

    @ApiModelProperty(value = "商品规格属性和属性值名称（key:value）")
    private String specAttrValueName;

    @ApiModelProperty(value = "详情大图片PC")
    private String goodsBoby;

    @ApiModelProperty(value = "详情大图片H5")
    private String mobileBody;

    @ApiModelProperty(value = "售后详情")
    private String afterSale;

    @ApiModelProperty(value = "销量")
    private Integer specSaleNum;

    @ApiModelProperty(value = "选中规格的图片集合")
    private List<GoodsSpecPicVO> specAttrValuePicList;

    @ApiModelProperty(value = "商品图片集合")
    private List<SpecGoodsPicVO> goodsPicList;

    @ApiModelProperty(value = "商品信息")
    private OptimizeSpecGoodsVO specGoodsVO;

    @ApiModelProperty(value = "是否删除 0 未删除 1已删除")
    private Integer delFlag;

    @ApiModelProperty(value = "商品SPU售价")
    private BigDecimal goodsSellPrice;

    @ApiModelProperty(value = "是否有进行中优惠券活动 0无 1有")
    private Integer couponsFlag;

    @ApiModelProperty(value = "优惠券活动名称")
    private FrontCouponsActivityPageDTO frontCouponsActivityPageDTO;

    @ApiModelProperty(value = "是否有进行中满减活动 0无 1有")
    private Integer reduceFlag;

    @ApiModelProperty("满减活动")
    private FrontReduceActivityPageDTO frontReduceActivityPageDTO;

    @ApiModelProperty("商品详情活动集合")
    private List<SpecActivityVO> specActivityList;

    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;
}
