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
import java.util.Date;


/**
 * 售后日志
 *
 * @author huangkeyuan
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleLogVo")
public class AftersaleLogVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "服务单号")
    private Long serviceSn;
    @ApiModelProperty(value = "当前售后状态")
    private Integer status;
    @ApiModelProperty(value = "信息")
    private String message;
    @ApiModelProperty(value = "日志类型（0:退货退款,1:换货）")
    private Integer type;
    @ApiModelProperty(value = "当前进度（0:提交申请,1:申请审核中,2:用户取消,3:售后收货,4:换货出库,5:进行退款,6:处理完成,7:审核拒绝,8:仲裁中,9:仲裁拒绝）")
    private Integer process;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "创建人")
    private String creator;

}