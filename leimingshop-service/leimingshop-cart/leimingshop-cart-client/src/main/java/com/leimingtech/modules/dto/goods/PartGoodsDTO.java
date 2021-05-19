/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述：
 * 购物车专用商品实体
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/3/19
 **/
@Data
@ApiModel(description = "PartGoodsDTO")
public class PartGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品分类一级id")
    private Long firstGcId;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品上下架状态（默认0:下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架")
    private Integer goodsStatus;

    @ApiModelProperty(value = "是否允许开具发票（0：不允许，1：允许）")
    private Integer invoiceFlag;

    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)1,2,3")
    private String invoiceType;

    @ApiModelProperty(value = "发票内容（多选 1：商品明细、2：商品类别）1,2")
    private String invoiceContent;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "配送方式 1快递 2自提 3无需物流，4电子提货")
    private Integer devlierType;
}
