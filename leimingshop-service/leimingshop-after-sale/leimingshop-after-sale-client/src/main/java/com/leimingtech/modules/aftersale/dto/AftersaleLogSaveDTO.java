/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 售后日志
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleLogSaveDTO")
public class AftersaleLogSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "服务单号")
    private Long serviceSn;
    @ApiModelProperty(value = "当前售后状态 （1:用户取消,2:退款失败,3:待退货入库,4:确认收货,5:退款中,6:退款成功,7:换货失败,8:待换货入库,9:换货出库中,10:换货成功）")
    private Integer status;

    @ApiModelProperty(value = "信息")
    private String message;

    @ApiModelProperty(value = "日志类型（0:退货,1:换货,2:仅退款）")
    private Integer type;

    @ApiModelProperty(value = "当前进度（0:提交申请,1:申请审核中,2:用户取消,3:售后收货,4:换货出库,5:进行退款,6:处理完成,7:审核拒绝,8:仲裁中,9:仲裁拒绝）")
    private Integer process;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "更新人")
    private String updater;

}