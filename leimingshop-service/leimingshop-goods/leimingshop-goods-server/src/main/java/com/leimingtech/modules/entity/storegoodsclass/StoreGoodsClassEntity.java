/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.storegoodsclass;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺商品分类表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_goods_class")
public class StoreGoodsClassEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 店铺商品分类名称
     */
    private String gcName;
    /**
     * 父ID
     */
    private Long gcParentId;
    /**
     * 展示类目图片
     */
    private String gcPic;
    /**
     * 前台展示（0不展示，默认为1展示）
     */
    private Integer showFlag;
    /**
     * 排序（0-255内的整数，数值越小排序越靠前）
     */
    private Integer sort;
}