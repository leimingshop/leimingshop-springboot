/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import com.leimingtech.modules.dto.coupons.FrontCouponsActivityPageDTO;
import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author tyl
 * @Date 2019/7/4 11:53
 * @Description
 **/
@ApiModel(description = "GoodsDetailsVO")
@Data
public class GoodsDetailsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规格id")
    private Long id;


    @ApiModelProperty(value = "规格货号")
    private String specSerial;

    @ApiModelProperty(value = "规格名")
    private String specName;

    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品上下架（2未上架,0下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "库存")
    private Integer specStorage;

    @ApiModelProperty(value = "售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品标签")
    private List<GoodsLabel> goodsLabels;

    @ApiModelProperty(value = "商品规格属性值名称（中间用逗号隔开）")
    private String specAttrName;

    @ApiModelProperty(value = "商品规格属性和属性值名称（key:value）")
    private String specAttrValueName;

    @ApiModelProperty(value = "详情大图片")
    private String goodsBoby;

    @ApiModelProperty(value = "详情大图片H5")
    private String mobileBody;

    @ApiModelProperty(value = "售后详情")
    private String afterSale;

    @ApiModelProperty(value = "销量")
    private Integer specSaleNum;

    @ApiModelProperty(value = "虚拟商品标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    private Integer expressFlag;

    @ApiModelProperty(value = "提货码有效期（-1永不过期,其他数字代表有效期天数）")
    private Integer codeValidDay;

    @ApiModelProperty(value = "提货码过期退款（0不支持，1支持）")
    private Integer codeRefundFlag;

    @ApiModelProperty(value = "规格图片信息")
    private List<GoodsSpecPicVO> specAttrValuePicList;

    @ApiModelProperty(value = "图片信息")
    private List<SpecGoodsPicVO> goodsPicList;

    @ApiModelProperty(value = "商品信息")
    private SpecGoodsVO goodsVO;

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
