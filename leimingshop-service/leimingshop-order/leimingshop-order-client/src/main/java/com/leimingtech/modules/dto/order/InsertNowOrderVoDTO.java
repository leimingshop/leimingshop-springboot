/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import com.leimingtech.modules.dto.grade.MemberGradeDTO;
import com.leimingtech.modules.dto.invoice.MemberInvoiceDTO;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 功能描述：
 * <立即购买提交订单超类实体>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/17 18:17
 **/
@Data
@ApiModel(description = "InsertNowOrderVoDTO")
public class InsertNowOrderVoDTO {

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("商品规格id")
    private Long specId;

    @ApiModelProperty("商品规格名称")
    private String specName;

    @ApiModelProperty("商品销售价格")
    private BigDecimal specSellPrice;

    @ApiModelProperty("商品实际销售价格")
    private BigDecimal specPrice;

    @ApiModelProperty("商品成本价格")
    private BigDecimal specCostPrice;

    @ApiModelProperty("商品规格主图")
    private String specMainPicture;

    @ApiModelProperty("商品购买数量")
    private Integer goodsNum;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("店铺二级分类")
    private Long secondStoreClassId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("收货地址id")
    private Long addressId;

    @ApiModelProperty("会员优惠券id")
    private Long memberCouponsId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty(value = "拼团记录id")
    private Long groupRecordId;

    @ApiModelProperty("订单留言")
    private String orderMessage;

    @ApiModelProperty(value = "运费模板id")
    private Long freightTemplateId;

    @ApiModelProperty(value = "运费金额")
    private BigDecimal shippingFee;

    @ApiModelProperty("会员信息")
    private MemberVoDTO memberVoDTO;

    @ApiModelProperty("会员等级信息")
    private MemberGradeDTO memberGradeDTO;

    @ApiModelProperty("订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer orderPlatform;
    //发票信息


    @ApiModelProperty(value = "发票类型( 默认0：不开票、1:电子、2：纸质、3：增值税普通发票)")
    private Integer invoiceType;

    @ApiModelProperty(value = "发票内容（ 1：商品明细、2：商品类别）")
    private Integer invoiceContent;

    @ApiModelProperty("用户提交抬头类型：（1、个人、2：单位）")
    private Integer companyType;

    @ApiModelProperty("个人名称（个人发票使用）")
    private String personalName;

    @ApiModelProperty("企业发票信息（单位开票使用）")
    private MemberInvoiceDTO memberInvoiceDTO;

    @ApiModelProperty("收票人名称")
    private String addresseeName;

    @ApiModelProperty("收票人手机")
    private String addresseePhone;

    @ApiModelProperty("收票人邮箱")
    private String addresseeMail;

    @ApiModelProperty("收票人地址")
    private String addresseeAddress;

    @ApiModelProperty("收票人详细地址")
    private String detailedAddress;

    @ApiModelProperty("省ID")
    private Long provinceId;

    @ApiModelProperty("市ID")
    private Long cityId;

    @ApiModelProperty("区ID")
    private Long districtId;

    @ApiModelProperty("街道ID")
    private Long streetId;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "收件人(虚拟订单必传)")
    private String memberName;

    @ApiModelProperty(value = "用户手机号(虚拟订单必传)")
    private String memberMobile;

    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
//    @ApiModelProperty(value = "配送方式 1快递 2自提 3无需物流，4电子提货")
    private Integer devlierType;

    @ApiModelProperty(value = "是否使用余额付款 0不使用 1使用")
    private Integer useBalance;

}
