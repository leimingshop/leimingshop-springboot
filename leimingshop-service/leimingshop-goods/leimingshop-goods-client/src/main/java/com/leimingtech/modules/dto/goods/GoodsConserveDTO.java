/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.goods.spec.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName GoodsConserveDTO
 * @Description 商品保存DTO
 * @Author DY
 * @Date 2019/6/5 9:44
 * @Version 1.0
 **/
@Data
@ApiModel(description = "GoodsConserveDTO")
public class GoodsConserveDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键")
    @Null(message = "id必须为空", groups = AddGroup.class)
    @NotNull(message = "id不能为空", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "商品分类id")
    @NotNull(message = "商品分类不能为空")
    private Long gcId;

    /**
     * 销售价
     */
    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;
    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    private BigDecimal specCostPrice;
    /**
     * 规格库存
     */
    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;

    @ApiModelProperty(value = "商品货号")
    @NotNull(message = "商品货号不能为空")
    private Long goodsSerial;

    @ApiModelProperty(value = "商品分类名称")
    @NotBlank(message = "商品分类名称不能为空")
    private String gcName;

    @ApiModelProperty(value = "商品名称")
    @NotBlank(message = "商品名称不能为空")
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

    @ApiModelProperty(value = "商品店铺价格")
    private BigDecimal goodsStorePrice;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "标签ids")
    private Long[] labelIds;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品详细内容")
    private String goodsBoby;

    @ApiModelProperty(value = "商品详细内容(手机版)")
    private String mobileBody;

    @ApiModelProperty(value = "售后服务")
    @Length(max = 800, message = "最大限制800字符")
    private String afterSale;

    @ApiModelProperty(value = "商品属性集合")
    private List<GoodsAttributeSaveDTO> goodsAttributeSaveDTOList;

    @ApiModelProperty(value = "商品规格属性和规格值集合")
    private List<SpecAttributeAndValueSaveDTO> specAttributeAndValueSaveDTOList;

    @ApiModelProperty(value = "规格图片集合")
    private List<SpecAttributePictureSaveDTO> specAttributePictureSaveDTOList;

    @ApiModelProperty(value = "商品规格集合")
    private List<GoodsSpecSaveDTO> goodsSpecSaveDTOList;

    @ApiModelProperty(value = "商品发布类型 0:立即发布;  1:定时发布")
    @NotNull(message = "商品发布类型不能为空")
    private Integer goodsType;


    @ApiModelProperty(value = "是否允许开具发票（0：不允许，1：允许）")
    @NotNull(message = "是否允许开具发票不能为空")
    private Integer invoiceFlag;

    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)1,2,3")
    @NotNull(message = "发票类型不能为空")
    private String invoiceType;

    @ApiModelProperty(value = "发票内容（多选 1：商品明细、2：商品类别）1,2")
    @NotNull(message = "发票内容不能为空")
    private String invoiceContent;

    @ApiModelProperty(value = "发布时间")
    private Date shelfTime;

    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    @NotNull(message = "配送方式不能为空")
    private Integer expressFlag;

    @ApiModelProperty(value = "运费承担方式 0无需物流 1买家承担 2卖家承担")
    private Integer freightBearType;

    @ApiModelProperty(value = "虚拟商品标记（0:否，1是）")
    private Integer virtualFlag;

    @ApiModelProperty(value = "提货码有效期（-1永不过期,其他数字代表有效期天数）")
    private Integer codeValidDay;

    /**
     * 默认规格重量
     */
    @ApiModelProperty(value = "默认规格重量")
    private Double specWeight;

    @ApiModelProperty(value = "提货码过期退款（0不支持，1支持）")
    private Integer codeRefundFlag;

    @ApiModelProperty(value = "运费模板id")
    private Long freightTemplateId;

    @ApiModelProperty("店铺商品一级分类ID")
    private Long firstStoreGoodsGcId;
    @ApiModelProperty("店铺商品一级分类名称")
    private String firstStoreGoodsGcName;

    @ApiModelProperty("店铺商品二级分类ID")
    private Long secondStoreGoodsGcId;
    @ApiModelProperty("店铺商品二级分类名称")
    private String secondStoreGoodsGcName;

    @ApiModelProperty(value = "默认商品图片")
    private List<DefaultSpecPictureDTO> specAttributePictureSaveListDefaultDTO;

}
