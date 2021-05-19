/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.label;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 商品标签管理GoodsLabelEntity
 * @Date :2019/5/20 14:24
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_label")
public class GoodsLabelEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品标签名
     */
    private String labelName;

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
