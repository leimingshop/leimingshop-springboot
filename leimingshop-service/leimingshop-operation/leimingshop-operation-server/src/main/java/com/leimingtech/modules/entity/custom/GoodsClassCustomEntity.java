/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.custom;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品自定义分类表
 *
 * @author xuzhch
 * @email 170518@163.dao
 * @since 1.0.0 2019-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_class_custom")
public class GoodsClassCustomEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 父ID
     */
    private Long gcParentId;

    /**
     * 展示类别名称
     */
    private String gcName;

    /**
     * 展示类别图片
     */
    private String gcPic;

    /**
     * 0 h5展示分类 1 pc展示分配
     */
    private Integer showType;

    /**
     * 展示类别url
     */
    private String gcUrl;

    /**
     * 关联商品分类id
     */
    private Long classId;

    /**
     * 层级path。格式：1,2,3
     */
    private String idPath;

    /**
     * 前台展示（0不展示，默认为1展示）
     */
    private Integer showFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 更新者
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
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}