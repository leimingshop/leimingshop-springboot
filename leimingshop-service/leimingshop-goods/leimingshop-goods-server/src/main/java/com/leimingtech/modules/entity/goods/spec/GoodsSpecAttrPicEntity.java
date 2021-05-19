/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods.spec;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品规格属性值与图片关联
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_spec_attr_pic")
public class GoodsSpecAttrPicEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品规格属性值ID
     */
    private Long specAttrValueId;

    /**
     * 商品规格属性ID
     */
    private Long specAttrId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 图片地址
     */
    private String pictureUrl;
    /**
     * 100*100图片地址
     */
    private String oneHundredPxPictureUrl;

    /**
     * 200*200图片地址
     */
    private String twoHundredPxPictureUrl;

    /**
     * 400*400图片地址
     */
    private String fourHundredPxPictureUrl;

    /**
     * 800*800图片地址
     */
    private String eightHundredPxPictureUrl;
    /**
     * 是否主图（默认0否，1是）
     */
    private Integer isMainPicture;

    /**
     * 排序
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
