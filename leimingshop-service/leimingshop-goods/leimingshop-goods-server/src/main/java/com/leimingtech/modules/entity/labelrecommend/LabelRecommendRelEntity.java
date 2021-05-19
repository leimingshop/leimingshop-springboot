/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.labelrecommend;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签推荐关联表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_label_recommend_rel")
public class LabelRecommendRelEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    private Long labelId;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 标签推荐排序
     */
    private Integer sort;
}
