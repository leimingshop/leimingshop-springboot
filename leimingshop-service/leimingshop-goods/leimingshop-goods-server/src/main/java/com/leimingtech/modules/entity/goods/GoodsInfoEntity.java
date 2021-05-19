/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品附加信息表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_info")
public class GoodsInfoEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品详细内容
     */
    private String goodsBoby;

    /**
     * 商品详细内容(手机版)
     */
    private String mobileBody;

    /**
     * 商品运费承担方式(默认 0为买家承担 1为卖家承担)
     */
    private Integer goodsTransfeeCharge;

    /**
     * 售后服务
     */
    private String afterSale;

    /**
     * 商品所在地(市)
     */
    private Long cityId;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 商品所在地(省)
     */
    private Long provinceId;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 评论次数
     */
    private Integer commentNum;

    /**
     * 售出数量
     */
    private Integer saleNum;

    /**
     * 商品收藏数量
     */
    private Integer goodsCollectNum;

    /**
     * 商品浏览数
     */
    private Integer goodsBrowseNum;

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