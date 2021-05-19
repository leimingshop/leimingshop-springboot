/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.settle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退货结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_return_bill")
public class ReturnBillEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 结算单编号
     */
    private Long billTotalId;
    /**
     * 退货编号
     */
    private Long returnSn;
    /**
     * 申请时间
     */
    private Date returnTime;
    /**
     * 退款金额
     */
    private BigDecimal returnAmount;
    /**
     * 规格编号
     */
    private String specSerial;

    /**
     * 规格主图
     */
    private String specMainPicture;
    /**
     * 规格ID
     */
    private Long specId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 支付方式
     */
    private String paymentName;
    /**
     * 平台处理时间
     */
    private Date manageTime;
}