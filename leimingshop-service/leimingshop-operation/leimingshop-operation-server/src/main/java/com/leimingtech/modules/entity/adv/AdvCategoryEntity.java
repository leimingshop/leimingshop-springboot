/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.adv;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 广告类别表
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_adv_category")
public class AdvCategoryEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 广告类别标识
     */
    private String advKey;

    /**
     * 广告类别名称
     */
    private String categoryName;

    /**
     * 广告上传提示语
     */
    private String advTips;

    /**
     * 广告像素宽（预留字段）
     */
    private Float advWeidth;

    /**
     * 广告像素高（预留字段）
     */
    private Float advHeight;

    /**
     * 广告展示类型 0：单图 1：多图
     */
    private Integer categoryType;

    /**
     * 广告分类类型 0普通广告 1楼层广告 2分类广告
     */
    private Integer sysFlag;

    /**
     * 广告类别状态 1启用 2停用
     */
    private Integer status;

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
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
