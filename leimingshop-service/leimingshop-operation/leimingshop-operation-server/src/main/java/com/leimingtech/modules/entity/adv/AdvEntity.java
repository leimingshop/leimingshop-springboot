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
 * 广告表
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_adv")
public class AdvEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 广告类别ID
     */
    private Long categoryId;

    /**
     * 广告类别标识
     */
    private String advKey;

    /**
     * 广告标题
     */
    private String advTitle;
    /**
     * 分类idPath
     */
    private String classId;

    /**
     * 广告开始时间（精确到日）
     */
    private Date startDate;

    /**
     * 广告结束时间（精确到日）
     */
    private Date endDate;

    /**
     * 广告类型 0：普通广告 1：楼层广告 2：分类广告
     */
    private Integer advType;

    /**
     * 楼层广告、分类广告关联内容id，普通广告为空
     */
    private Long relationId;
    /**
     * 分类广告关联名称
     */
    private String relationName;

    /**
     * 关联类型（链接、店铺、活动等等）
     */
    private String relationType;

    /**
     * 关联目标（链接地址、店铺ID等等）
     */
    private String relationTarget;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 区域ID
     */
    private String siteId;

    /**
     * 广告状态（默认0:未启用、1:启用、2:停用）
     */
    private Integer advState;

    /**
     * 广告审批状态（默认0:待审核、1:审核通过、2:审核未通过）
     */
    private Integer applyState;

    /**
     * 审批人
     */
    private String applyBy;

    /**
     * 审批时间
     */
    private Date applyDate;

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
