/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.transport;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物流公司表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_transport_company")
public class TransportCompanyEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 快递公司编码
     */
    private String companyId;
    /**
     * 快递公司名称
     */
    private String companyName;
    /**
     * 快递公司电话
     */
    private String companyPhone;
    /**
     * 快递公司网址
     */
    private String site;
}