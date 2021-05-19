/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.mobile.indexmenu;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 移动首页菜单实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_mobile_index_menu")
public class MobileIndexMenuEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 移动首页菜单名称
     */
    private String menuName;

    /**
     * 跳转类型 url外部链接 coupon优惠券 group拼团 seckill秒杀 flashsale限时抢购
     */
    private String linkType;

    /**
     * 跳转链接url
     */
    private String url;

    /**
     * 分类id
     */
    private String classId;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 是否显示 0不显示 1显示
     */
    private Integer showFlag;

    /**
     * 排序优先级
     */
    private Integer sort;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认为0未删除，1已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
