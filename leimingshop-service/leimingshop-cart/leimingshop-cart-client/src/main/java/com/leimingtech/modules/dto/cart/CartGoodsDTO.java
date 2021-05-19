/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 立即购买和去结算商品信息实体
 * @Date: 2019/6/17 13:52
 * @Version: V1.0
 */
@Data
@ApiModel(description = "CartGoodsDTO")
public class CartGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "购买数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品规格ID")
    private Long specId;


    @ApiModelProperty(value = "商品重量")
    private Double specWeight;

    @ApiModelProperty(value = "商品规格名称")
    private String specName;

    @ApiModelProperty(value = "规格编号")
    private String specSerial;

    @ApiModelProperty(value = "商品所属店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "商品所属店铺名称")
    private String storeName;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;


    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "规格活动销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "规格实际销售价")
    private BigDecimal specPrice;

    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;

    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty(value = "活动结束时间")
    private Date activityEndDate;

    @ApiModelProperty(value = "商品规格属性值")
    private String specInfo;
    @ApiModelProperty(value = "购物车内商品状态（0正常，1无货 ，2下架）")
    private Integer status;

    @ApiModelProperty(value = "是否允许开具发票（0：不允许，1：允许）")
    private Integer invoiceFlag;

    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)1,2,3")
    private String invoiceType;

    @ApiModelProperty(value = "发票内容（多选 1：商品明细、2：商品类别）1,2")
    private String invoiceContent;

    @ApiModelProperty(value = "运费模板id")
    private Long freightTemplateId;

    @ApiModelProperty(value = "结算页运费模板状态 0正常 1区域不可配送")
    private Integer freightStatus;

    @ApiModelProperty(value = "店铺商品二级分类ID")
    private Long secondStoreGoodsGcId;

    @ApiModelProperty(value = "店铺商品二级分类名称")
    private String secondStoreGoodsGcName;


    @ApiModelProperty(value = "积分是否可用(默认：0不可用，1可用)")
    private Integer pointFlag;

    @ApiModelProperty(value = "余额是否可用(默认：0不可用，1可用)")
    private Integer balanceFlag;

    @ApiModelProperty(value = "优惠券是否可用 (默认：0不可用，1可用)")
    private Integer couponsFlag;

    @ApiModelProperty(value = "满减是否可用(默认：0不可用，1可用)")
    private Integer reduceFlag;

}
