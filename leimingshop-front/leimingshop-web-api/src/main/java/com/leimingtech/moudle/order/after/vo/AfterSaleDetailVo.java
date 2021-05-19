/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.after.vo;

import com.leimingtech.modules.aftersale.dto.AftersaleLogDetailDTO;
import com.leimingtech.modules.aftersale.dto.UserDetailArbitrationDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 售后详情接口
 *
 * @author SWH ab4856812@163.com
 * @version V1.0
 * @date 2019/6/19 22:32
 */
@Data
@ApiModel(description = "AfterSaleDetailVo")
public class AfterSaleDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单项ID")
    private Long orderGoodsId;
    @ApiModelProperty(value = "售后方式（0:退货,1:换货）")
    private Integer aftersaleType;
    @ApiModelProperty(value = "申请售后原因")
    private String aftersaleReason;
    @ApiModelProperty(value = "问题描述")
    private String aftersaleExplain;
    @ApiModelProperty(value = "审核状态（1:商家审核中,2:平台审核中,3:已完成,4:已取消）")
    private Integer auditStatus;
    @ApiModelProperty(value = "审核结果（1:审核通过,2:审核不通过）")
    private Integer auditResult;
    @ApiModelProperty(value = "收货人姓名")
    private String receiver;
    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;
    @ApiModelProperty(value = "收货人地址")
    private String receiverAddress;
    @ApiModelProperty(value = "联系人")
    private String contacts;
    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;
    @ApiModelProperty(value = "售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:确认收货,5:退款中,6:退款成功,7:换货失败,8:待换货入库,9:换货出库中,10:换货成功）")
    private Integer aftersaleStatus;
    @ApiModelProperty(value = "售后物流状态（1为买家待发货,2为卖家待收货,3卖家已收货,4为买家待收货,5为已完成）")
    private Integer logisticsStatus;
    @ApiModelProperty(value = "审核进度最新记录消息")
    private String lastLog;
    @ApiModelProperty(value = "审核进度集合")
    private List<AftersaleLogDetailDTO> detailDTOList;
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品图片")
    private String goodsImage;
    @ApiModelProperty(value = "申请时间")
    private Date createDate;
    @ApiModelProperty(value = "凭证图片")
    private String aftersalePics;
    @ApiModelProperty(value = "商家审核信息")
    private String sellerAudit;
    @ApiModelProperty(value = "平台审核信息")
    private String adminAudit;
    @ApiModelProperty(value = "买家发货联系电话")
    private String logisticsContactsPhone;
    @ApiModelProperty(value = "买家发货物流公司")
    private String logisticsCompany;
    @ApiModelProperty(value = "买家发货物流单号")
    private String logisticsNumber;
    @ApiModelProperty(value = "仲裁信息")
    private UserDetailArbitrationDTO arbitrationDTO;

}