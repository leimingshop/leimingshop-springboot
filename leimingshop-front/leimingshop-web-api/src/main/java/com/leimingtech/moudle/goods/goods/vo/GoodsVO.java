/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.goods.goods.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsDTO")
public class GoodsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品货号")
    private Long goodsSerial;

    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;

    @ApiModelProperty(value = "商品分类id")
    private Long gcId;

    @ApiModelProperty(value = "商品分类名称")
    private String gcName;

    @ApiModelProperty("商品标签名称")
    private String labelName;

    @ApiModelProperty(value = "商品分类一级id")
    private Long firstGcId;

    @ApiModelProperty(value = "商品分类一级名称")
    private String firstGcName;

    @ApiModelProperty(value = "商品分类二级id")
    private Long secondGcId;

    /**
     * 店铺类型 1:自营商户，2:普通商户
     */
    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;

    @ApiModelProperty(value = "商品分类二级名称")
    private String secondGcName;

    @ApiModelProperty(value = "商品分类三级id")
    private Long thirdGcId;

    @ApiModelProperty(value = "商品分类三级名称")
    private String thirdGcName;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    @ApiModelProperty(value = "商品默认对应的规格id")
    private Long specId;

    @ApiModelProperty(value = "商品店铺价格")
    private BigDecimal goodsStorePrice;

    @ApiModelProperty(value = "是否支持快递 0不支持 1支持")
    private Integer expressFlag;

    @ApiModelProperty(value = "运费模板id")
    private Long freightTemplateId;

    @ApiModelProperty(value = "商品上下架状态（默认0:下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架")
    private Integer goodsStatus;

    @ApiModelProperty(value = "商品关键字")
    private String goodsKeywords;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    private Integer devlierType;


    @ApiModelProperty(value = "提货码有效期（-1永不过期,其他数字代表有效期天数）")
    private Integer codeValidDay;

    @ApiModelProperty(value = "提货码过期退款（0不支持，1支持）")
    private Integer codeRefundFlag;

    @ApiModelProperty(name = "销售价")
    private BigDecimal specSellPrice;
    @ApiModelProperty(name = "成本价")
    private BigDecimal specCostPrice;
    @ApiModelProperty(name = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "是否允许开具发票（0：不允许，1：允许）")
    private Integer invoiceFlag;

    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)1,2,3")
    private String invoiceType;

    @ApiModelProperty(value = "发票内容（多选 1：商品明细、2：商品类别）1,2")
    private String invoiceContent;

}
