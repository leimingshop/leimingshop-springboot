/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods.check;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品审核表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_check")
public class GoodsCheckEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品结果，20:审核未通过，30:审核通过,40:违规下架
     */
    private Integer goodState;


    /**
     * 操作类型，1:商品发布，2:商品更新,3:审核通过,4:审核拒绝
     */
    private Integer operationType;

    /**
     * 原因
     */
    private String remarks;

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