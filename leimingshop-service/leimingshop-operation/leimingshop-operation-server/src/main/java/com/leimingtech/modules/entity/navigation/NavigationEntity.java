/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.navigation;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 首页导航设置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_navigation")
public class NavigationEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 导航标题
     */
    private String title;
    /**
     * 关联类型 1 自定义链接 2 商品分类
     */
    private Integer relationType;
    /**
     * 关联值
     */
    private String relationParams;
    /**
     * 是否打开新的页面 0 否 1 是
     */
    private Integer isOpen;
    /**
     * 排序（0 -255数字越小越靠前）
     */
    private Integer sort;
    /**
     * 分类ID
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String classIds;
    /**
     * 店铺ID
     */
    private Long storeId;
}