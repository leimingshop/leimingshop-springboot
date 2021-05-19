/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.brand;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 品牌管理 BrandEntity
 * @Date :2019/5/20 14:08
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_brand")
public class BrandEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌英文名称
     */
    private String brandNameEn;

    /**
     * 品牌首字母
     */
    private String brandInitials;

    /**
     * 图片
     */
    private String brandPic;

    /**
     * 排序
     */
    private Integer brandSort;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 品牌申请，0为申请中，1为通过，默认为1，申请功能是会员使用，系统后台默认为1
     */
    private Integer brandApply;

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
     * 删除标记（默认0:未删除,1:已删除）
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