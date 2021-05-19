/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.freight.template;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运费模板实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_freight_template")
public class FreightTemplateEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型：0按件数 1按重量
     */
    private Integer templateType;

    /**
     * 是否默认运费模板：0否 1是
     */
    private Integer defaultFlag;

}
