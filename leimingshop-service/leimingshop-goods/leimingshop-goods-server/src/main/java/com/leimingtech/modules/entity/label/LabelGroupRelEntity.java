/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.label;


import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;


/**
 * 标签与标签分组关联管理
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Data
@TableName("lmshop_goods_label_group_rel")
public class LabelGroupRelEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 标签分组ID
     */
    private Long groupId;
    /**
     * 标签ID
     */
    private Long labelId;
}
