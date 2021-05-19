/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.settle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对账操作记录
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_bill_log")
public class BillLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 对账单ID
     */
    private Long billId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作类型
     */
    private String operatorType;
}