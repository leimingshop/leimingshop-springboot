/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单物流消息记录实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_logistics_log")
public class OrderLogisticsLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 买家id
     */
    private Long buyerId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 快递单当前的状态(0：在途；1：揽件；2：疑难；3：签收；4：退签；5：派件；6：退回)
     */
    private Integer logisticsState;

    /**
     * 订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;50:已提交;60:已确认;
     */
    private Integer orderStatus;

    /**
     * 查询物流结果状态（0：物流单暂无结果，1：查询成功2：接口出现异常 ）
     */
    private Integer resultStatus;

    /**
     * 是否被查看过(0：否；1：是)
     */
    private Integer isCheck;

    /**
     * 物流单号
     */
    private String shipmentNumber;

    /**
     * 物流公司编号
     */
    private String companyNumber;

    /**
     * 每条跟踪信息的描述
     */
    private String context;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认为0未删除，1已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
