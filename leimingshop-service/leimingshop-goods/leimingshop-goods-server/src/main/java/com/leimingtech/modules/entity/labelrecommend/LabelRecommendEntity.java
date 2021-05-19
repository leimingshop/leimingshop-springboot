/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.labelrecommend;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签推荐表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_label_recommend")
public class LabelRecommendEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 标签标识
     */
    private String labelFlag;
    /**
     * 标签名称
     */
    private String labelName;
    /**
     * 标签状态（默认1:启用,2:禁用）
     */
    private Integer labelStatus;
}
