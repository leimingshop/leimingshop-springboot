/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods.record;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 商品上下架状态记录
 * @Date :2019/6/4 17:53
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_record")
public class GoodsRecordEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 规格id
     */
    private Long specId;

    /**
     * 商品上下架状态（默认:2未上架,0下架状态，1上架状态）
     */
    private Integer goodsShow;

    /**
     * 下架描述
     */
    private String remark;

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