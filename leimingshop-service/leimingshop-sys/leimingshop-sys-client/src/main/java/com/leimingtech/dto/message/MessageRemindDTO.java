/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 消息提醒表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-16
 */
@Data
@ApiModel(description = "MessageRemindDTO")
public class MessageRemindDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty(value = "商品审核数量")
    private Integer goodsAuditCount;
    @ApiModelProperty(value = "退货申请数量")
    private Integer saleReturnCount;

    @ApiModelProperty(value = "换货申请数量")
    private Integer saleBarterCount;

    @ApiModelProperty(value = "商品审核拒绝数量")
    private Integer goodsAuditRepulseCount;

    @ApiModelProperty(value = "平台私信提醒数量")
    private Integer privateMessageCount;

    @ApiModelProperty(value = "系统消息提醒数量")
    private Integer sysMessageCount;

    @ApiModelProperty(value = "库存预警数量")
    private Integer storageWarnCount;
}