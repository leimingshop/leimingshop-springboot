/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.freight.template;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 运费模板规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_freight_template_rule")
public class FreightTemplateRuleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 运费模板id
     */
    private Long templateId;

    /**
     * 首件（个）/首重（kg）
     */
    private String firstFee;

    /**
     * 首件（重）运费
     */
    private BigDecimal firstAmount;

    /**
     * 续件（个）/续重（kg）
     */
    private String additionalFee;

    /**
     * 续件（重）运费
     */
    private BigDecimal additionalAmount;

    /**
     * 地区id
     */
    private String areaIds;

    /**
     * 地区描述
     */
    private String areaDescription;

}
