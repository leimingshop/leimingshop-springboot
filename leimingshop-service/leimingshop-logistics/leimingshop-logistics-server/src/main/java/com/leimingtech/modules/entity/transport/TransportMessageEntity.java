/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.transport;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物流信息表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_transport_message")
public class TransportMessageEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回
     */
    private Integer state;
    /**
     * 是否签收标记
     */
    private Integer ischeck;
    /**
     * 快递公司编码
     */
    private String com;
    /**
     * 快递单号
     */
    private String nu;
    /**
     * 快递公司名称
     */
    private String companyName;

    /**
     * 快递公司电话
     */
    private String companyPhone;

}