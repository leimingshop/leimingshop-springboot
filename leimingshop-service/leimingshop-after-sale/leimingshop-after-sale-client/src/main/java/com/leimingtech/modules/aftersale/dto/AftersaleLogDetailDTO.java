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
 * @Author: SWH ab4856812@163.com
 * @Description: 售后详情日志信息
 * @Date: 2019/7/22 20:41
 * @Version: V1.0
 */
@Data
@ApiModel(description = "AftersaleLogDetailDTO")
public class AftersaleLogDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前进度（0:提交申请,1:申请审核中,2:用户取消,3:售后收货,4:换货出库,5:进行退款,6:处理完成，8:仲裁中，9:仲裁拒绝）")
    private String processString;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "是否是当前状态（0：否，1：是）")
    private Integer currentState;

}