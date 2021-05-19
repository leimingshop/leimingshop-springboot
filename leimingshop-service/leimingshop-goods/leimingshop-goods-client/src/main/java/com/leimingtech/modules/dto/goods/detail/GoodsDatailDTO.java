/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.detail;

import com.leimingtech.modules.dto.goods.spec.DefaultSpecPictureDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsDatailDTO")
public class GoodsDatailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品分类id")
    private Long gcId;

    @ApiModelProperty(value = "商品分类名称")
    private String gcName;

    @ApiModelProperty(value = "商品货号")
    private Long goodsSerial;

    @ApiModelProperty(value = "商品分类名称(a>b>c)格式")
    private String gcNameVal;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品分类一级id")
    private Long firstGcId;

    @ApiModelProperty(value = "商品分类一级名称")
    private String firstGcName;

    @ApiModelProperty(value = "商品分类二级id")
    private Long secondGcId;

    @ApiModelProperty(value = "商品分类二级名称")
    private String secondGcName;

    @ApiModelProperty(value = "商品分类三级id")
    private Long thirdGcId;

    @ApiModelProperty(value = "商品分类三级名称")
    private String thirdGcName;

    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;

    @ApiModelProperty("店铺商品一级分类ID")
    private Long firstStoreGoodsGcId;
    @ApiModelProperty("店铺商品一级分类名称")
    private String firstStoreGoodsGcName;

    @ApiModelProperty("店铺商品二级分类ID")
    private Long secondStoreGoodsGcId;
    @ApiModelProperty("店铺商品二级分类名称")
    private String secondStoreGoodsGcName;

    /**
     * 总库存
     */
    @ApiModelProperty(value = "总库存")
    private Integer specStorage;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "成本价")
    private BigDecimal specCostPrice;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品品牌id")
    private Long goodsInfoId;

    @ApiModelProperty(value = "商品详细内容")
    private String goodsBoby;

    @ApiModelProperty(value = "商品详细内容(手机版)")
    private String mobileBody;

    @ApiModelProperty(value = "售后服务")
    private String afterSale;

    @ApiModelProperty(value = "标签ids")
    private List<Long> labelIds;
    @ApiModelProperty(value = "运费承担方式（0:无需物流，1:买家承担，2:卖家承担）")
    private Integer freightBearType;

    @ApiModelProperty(value = "商品属性集合")
    private List<GoodsAttributeInfoDTO> goodsAttributeDTOList;

    @ApiModelProperty(value = "商品规格属性和规格值集合")
    private List<SpecAttributeDetailDTO> specAttributeDetailDTOList;

    @ApiModelProperty(value = "商品图片集合")
    private List<SpecAttributePictureDetailDTO> specAttributePictureDetailDTOList;

    @ApiModelProperty(value = "商品规格集合")
    private List<GoodsSpecDetailDTO> goodsSpecDetailDTOList;

    @ApiModelProperty(value = "商品默认图片")
    private List<DefaultSpecPictureDTO> specPictureListDefaultDTO;

    @ApiModelProperty(value = "是否支持快递 0不支持 1支持")
    private Integer expressFlag;

    @ApiModelProperty(value = "运费模板id")
    private Long freightTemplateId;

    @ApiModelProperty(value = "是否开具发票（0：不开票，1：开票）")
    private Integer invoiceFlag;

    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)")
    private String invoiceType;

    @ApiModelProperty(value = "发票内容（多选 1：商品明细、2：商品类别）")
    private String invoiceContent;

    @ApiModelProperty(value = "商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布")
    private Integer goodsStatus;
    @ApiModelProperty(value = "配送方式 1快递 2自提 3无需物流，4电子提货")
    private Integer devlierType;

    @ApiModelProperty(value = "虚拟商品标记（0:否，1是）")
    private Integer virtualFlag;

    @ApiModelProperty(value = "提货码有效期（-1永不过期,其他数字代表有效期天数）")
    private Integer codeValidDay;

    @ApiModelProperty(value = "提货码过期退款（0不支持，1支持）")
    private Integer codeRefundFlag;

    @ApiModelProperty(value = "商品发布类型 0:立即发布;  1:定时发布")
    private Integer goodsType;

    @ApiModelProperty(value = "发布时间")
    private Date shelfTime;

    @ApiModelProperty(value = "更新人")
    private String updater;
}
