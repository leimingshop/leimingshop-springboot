/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.invoice;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 发票信息表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_invoice_info")
public class InvoiceInfoEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 发票表ID
     */
    private Long invoiceId;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品编号
     */
    private Long spu;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer goodsNum;
    /**
     * 规格ID
     */
    private Long specId;
    /**
     * 规格描述
     */
    private String specInfo;
    /**
     * 商品规格价格
     */
    private BigDecimal specPayPrice;
    /**
     * 商品总价格
     */
    private BigDecimal specTotalPrice;
    /**
     * 一级分类ID
     */
    private Long firstGcId;
    /**
     * 一级分类名称
     */
    private String firstGcName;
}
