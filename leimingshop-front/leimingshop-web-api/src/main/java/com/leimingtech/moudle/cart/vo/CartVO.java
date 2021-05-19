/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.cart.vo;

import com.leimingtech.modules.dto.cart.CartDTO;
import com.leimingtech.moudle.activity.vo.coupon.GoodsDetailCouponsListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 购物车表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Data
@ApiModel(description = "CartDTO")
public class CartVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "商品所属店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "商品所属店铺名称")
    private String storeName;
    @ApiModelProperty(value = "店铺类型")
    private Integer storeType;
    @ApiModelProperty("商品所属标签ID")
    private Long labelId;
    @ApiModelProperty("标签名称")
    private String labelName;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "购买数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品规格ID")
    private Long specId;
    @ApiModelProperty(value = "商品规格名称")
    private String specName;
    @ApiModelProperty(value = "规格编号")
    private String specSerial;
    @ApiModelProperty(value = "规格实际销售价")
    private BigDecimal specSellPrice;
    @ApiModelProperty(value = "规格原销售价")
    private BigDecimal specPrice;
    @ApiModelProperty(value = "第一次保存购物车时的价格")
    private BigDecimal specSavePrice;
    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;
    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;
    @ApiModelProperty(value = "商品规格属性值")
    private String specInfo;
    @ApiModelProperty(value = "商品所在一级分类")
    private Long firstGcId;
    @ApiModelProperty(value = "品牌id")
    private Long brandId;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "活动id")
    private Long activityId;
    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3满减 4拼团")
    private Integer activityType;
    @ApiModelProperty(value = "活动剩余库存")
    private Integer activitySurplusStorage;
    @ApiModelProperty(value = "活动结束时间")
    private Date activityEndDate;
    @ApiModelProperty(value = "活动限购数量")
    private Integer restrictionQuantity;
    @ApiModelProperty(value = "活动描述")
    private String activityDescription;
    @ApiModelProperty(value = "活动数量")
    private Integer activityNum;
    @ApiModelProperty(value = "是否选中( 0未选中  1 已选中 )")
    private Integer isSelect;
    @ApiModelProperty(value = "购物车内商品状态（0正常，1无货 ，2下架）")
    private Integer status;
    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;
    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    private Integer devlierType;
    @ApiModelProperty(value = "更新时间")
    private Long updateDate;
    @ApiModelProperty(value = "运费模板id")
    private Long freightTemplateId;
    @ApiModelProperty(value = "当前商品可用优惠券")
    private GoodsDetailCouponsListVO goodsDetailCouponsListDTO;
    @ApiModelProperty(value = "当前商品可用满减活动")
    private List<FrontReduceActivityPageVO> frontReduceActivityPageDTO;

    public static int compare(CartDTO f1, CartDTO f2) {
        return f2.getUpdateDate().compareTo(f1.getUpdateDate());
    }


}
