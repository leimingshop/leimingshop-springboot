/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.picture;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 图片分类表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_picture_category")
public class PictureCategoryEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图片分类名称
     */
    private String categoryName;

    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 父分类ID
     */
    private Long parentCategoryId;
    /**
     * 父分类名称
     */
    private String parentCategoryName;


}