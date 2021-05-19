/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.after.vo;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 售后申请和对应的售后商品数据实体(一对多关系)
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020年5月19日18:06:02
 */
@Data
@ApiModel(description = "AftersaleApplyRecordVo")
public class AftersaleApplyRecordVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "订单项ID")
    private Long orderGoodsId;
    @ApiModelProperty(value = "申请时间")
    private Date createDate;
    @ApiModelProperty(value = "售后方式（0:退货,1:换货）")
    private Integer aftersaleType;
    @ApiModelProperty(value = "审核状态（1:商家审核中,2:平台审核中,3:已完成,4:已取消）")
    private Integer auditStatus;
    @ApiModelProperty(value = "审核结果（0:未审核,1:审核通过,2:审核不通过,3:审核中,4:用户取消）")
    private Integer auditResult;
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "主商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "主商品名称")
    private String goodsName;
    @ApiModelProperty(value = "主商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "主商品规格ID")
    private Long specId;
    @ApiModelProperty(value = "主商品规格名称")
    private String specName;
    @ApiModelProperty(value = "主商品规格属性值名称")
    private String specAttrName;
    @ApiModelProperty(value = "主商品规格主图")
    private String specMainPicture;
    @ApiModelProperty(value = "是否赠品")
    private Integer isGift;
    @ApiModelProperty(value = "售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:确认收货,5:退款中,6:退款成功,7:换货失败,8:待换货入库,9:换货出库中,10:换货成功）")
    private Integer finalStatus;


}
