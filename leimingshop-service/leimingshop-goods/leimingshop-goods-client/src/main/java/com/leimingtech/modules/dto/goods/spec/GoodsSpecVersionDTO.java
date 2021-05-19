/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 商品规格全部信息实体（Service内部提交订单校验库存使用）
 * @Date: 23:57 2019/6/26
 * @Version: V1.0
 */
@Data
@ToString
public class GoodsSpecVersionDTO implements Serializable {

    private static final long serialVersionUID = 6275585030035620449L;

    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品规格编号
     */
    private String specSerial;

    /**
     * 商品规格名称
     */
    private String specName;

    /**
     * 规格库存
     */
    private Integer specStorage;

    /**
     * 销售价
     */
    private BigDecimal specSellPrice;

    /**
     * 成本价
     */
    private BigDecimal specCostPrice;

    /**
     * 商品规格属性值名称（中间用逗号隔开）
     */
    private String specAttrName;
    /**
     * 商品规格属性和属性值名称（key:value）
     */
    private String specAttrValueName;

    /**
     * 是否启用（默认0启用，1未启用）
     */
    private Integer isEnable;

    /**
     * 商品规格主图
     */
    private String specMainPicture;

    /**
     * 规格销售数量
     */
    private Integer specSaleNum;

    /**
     * 规格上下架状态（默认:2未上架,0下架状态，1上架状态）
     */
    private Integer specShow;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 删除标记（默认为0未删除，1已删除）
     */
    private Integer delFlag;

    /**
     * 乐观锁
     */
    private Integer version;
}
