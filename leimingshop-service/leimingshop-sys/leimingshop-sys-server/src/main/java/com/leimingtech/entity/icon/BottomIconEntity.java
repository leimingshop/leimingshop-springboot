/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.icon;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * app底部图片配置表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-9
 */
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_bottom_icon")
@Data
public class BottomIconEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 底部菜单名称
     */
    private String menuName;
    /**
     * 底部图片地址 未选择
     */
    private String unselectedIcon;
    /**
     * 底部图片地址 已选择
     */
    private String selectedIcon;

    /**
     * 标识，用于识别每个icon
     */
    private String iconCode;
    /**
     * 排序
     */
    private Integer sort;
}
