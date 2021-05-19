/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.modules.dto.invoice.MemberInvoiceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 功能描述：
 * <>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/17 18:17
 **/
@Data
@ApiModel("立即购买提交订单实体")
public class InsertNowOrderDTO {

    @ApiModelProperty("商品规格id")
    @NotNull(message = "商品规格id不能为空", groups = AddGroup.class)
    private Long specId;

    @ApiModelProperty("商品销售价格")
    @NotNull(message = "商品销售价格不能为空", groups = AddGroup.class)
    private BigDecimal specSellPrice;

    @ApiModelProperty("商品购买数量")
    @NotNull(message = "商品购买数量不能为空", groups = AddGroup.class)
    @Max(value = 200, message = "商品最大购买数量为200", groups = AddGroup.class)
    @Min(value = 1, message = "商品最小购买数量为1", groups = AddGroup.class)
    private Integer goodsNum;

    @ApiModelProperty("收货地址id")
    private Long addressId;

    @ApiModelProperty(value = "收件人(虚拟订单必传)")
    private String memberName;

    @ApiModelProperty(value = "用户手机号(虚拟订单必传)")
    private String memberMobile;

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

    @ApiModelProperty("订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer orderPlatform;


    @ApiModelProperty(value = "发票类型(默认0：不开票、 1:电子、2：纸质、3：增值税普通发票)")
    private Integer invoiceType;

    @ApiModelProperty(value = "发票内容（ 1：商品明细、2：商品类别）")
    private Integer invoiceContent;

    @ApiModelProperty("用户提交抬头类型（不开票、1：个人、2：单位）")
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

    @ApiModelProperty(value = "是否使用余额付款 0不使用 1使用")
    private Integer useBalance;

    @ApiModelProperty("使用余额时，必传支付密码")
    private String payPassword;

}
