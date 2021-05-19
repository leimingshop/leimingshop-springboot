/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.label;


import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;

/**
 * 标签分组
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Data
@TableName("lmshop_goods_label_group")
public class LabelGroupEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 标签分组名称
     */
    private String groupName;
    /**
     * 标签数量
     */
    private Integer labelNum;
    /**
     * 标签分组排序
     */
    private Integer sort;
    /**
     * 标签分组状态（默认1:启用,2:禁用）
     */
    private Integer groupStatus;
}
