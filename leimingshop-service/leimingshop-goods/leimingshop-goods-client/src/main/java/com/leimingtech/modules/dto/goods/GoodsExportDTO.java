/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import com.leimingtech.modules.dto.goods.detail.GoodsSpecDetailDTO;
import com.leimingtech.modules.dto.goods.detail.SpecAttributePictureDetailDTO;
import com.leimingtech.modules.dto.goods.spec.DefaultSpecPictureDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: lishuo
 * @Date: 2/7/2020 17:52
 * @email: lishuo@leimingtech.com
 * @Description: goods excel导出类 dto
 */
@Data
@ApiModel("goodsExportDTO")
public class GoodsExportDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("goodsId")
    private Long goodsId;
    @ApiModelProperty(value = "商品分类名称")
    private String gcName;
    @ApiModelProperty(value = "商品分类一级名称")
    private String firstGcName;
    @ApiModelProperty(value = "商品分类二级名称")
    private String secondGcName;
    @ApiModelProperty(value = "商品分类三级名称")
    private String thirdGcName;
    @ApiModelProperty(value = "店铺一级分类名称")
    private String firstStoreGoodsGcName;
    @ApiModelProperty(value = "店铺二级分类名称")
    private String secondStoreGoodsGcName;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品名称副标题")
    private String goodsSubTitle;
    @ApiModelProperty("品牌名称")
    private String brandName;
    @ApiModelProperty("标签名称")
    private String goodsLabelName;
    @ApiModelProperty("成本价格")
    private BigDecimal specCostPrice;
    @ApiModelProperty("售价")
    private BigDecimal specSellPrice;
    @ApiModelProperty("配送方式 1快递 2自提 3无需物流，4电子提货")
    private Integer devlierType;
    @ApiModelProperty("配送方式 1快递 2自提 3电子提货")
    private Integer expressFlag;
    @ApiModelProperty("运费模板名称")
    private String freightTemplateName;
    @ApiModelProperty("运费承担方式(0无需物流 1买家承担 2卖家承担)")
    private String freightBearType;
    @ApiModelProperty("否允许开具发票（0：不允许，1：允许）")
    private Integer invoiceFlag;
    @ApiModelProperty("发票类型(多选 1:电子、2：纸质、3：增值税普通发票)1,2,3")
    private String invoiceType;
    @ApiModelProperty("发票内容（多选 1：商品明细、2：商品类别）1,2")
    private String invoiceContent;
    @ApiModelProperty("商品属性的信息")
    private List<GoodsAttributeDTO> goodsAttributeDTOList;
    @ApiModelProperty("商品规格的信息")
    private List<GoodsSpecDetailDTO> goodsSpecDetailDTOList;
    @ApiModelProperty("商品规格属性值与图片")
    private List<DefaultSpecPictureDTO> defaultSpecPictureDTOList;
    @ApiModelProperty("商品规格属性值与图片关联表")
    private List<SpecAttributePictureDetailDTO> specAttributePictureDetailDTOList;
    @ApiModelProperty("商品附加信息")
    private GoodsInfoDTO goodsInfoDTO;
}