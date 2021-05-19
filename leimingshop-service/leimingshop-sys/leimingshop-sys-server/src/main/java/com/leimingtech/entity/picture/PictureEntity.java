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
 * 图片表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_picture")
public class PictureEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 图片分类ID
     */
    private Long categoryId;

    /**
     * 分组名称
     */
    private String categoryName;

    /**
     * 图片名称
     */
    private String pictureName;

    /**
     * 缩略图地址
     */
    private String thumbPath;

    /**
     * 图片地址
     */
    private String picturePath;
}