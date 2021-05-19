/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 订单物流消息记录实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@ApiModel(description = "OrderLogisticsLogDTO")
public class OrderLogisticsLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息记录id")
    private Long id;

    @ApiModelProperty(value = "买家id")
    private Long buyerId;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "快递单当前的状态(0：在途；1：揽件；2：疑难；3：签收；4：退签；5：派件；6：退回)")
    private Integer logisticsState;

    @ApiModelProperty(value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;50:已提交;60:已确认;")
    private Integer orderStatus;

    @ApiModelProperty(value = "查询物流结果状态（0：物流单暂无结果，1：查询成功2：接口出现异常 ）")
    private Integer resultStatus;

    @ApiModelProperty(value = "是否被查看过(0：否；1：是)")
    private Integer isCheck;

    @ApiModelProperty(value = "物流单号")
    private String shipmentNumber;

    @ApiModelProperty(value = "物流公司编号")
    private String companyNumber;

    @ApiModelProperty(value = "每条跟踪信息的描述")
    private String context;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
