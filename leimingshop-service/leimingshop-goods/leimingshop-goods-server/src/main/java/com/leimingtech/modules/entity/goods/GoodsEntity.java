/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods")
public class GoodsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品货号
     */
    private Long goodsSerial;

    /**
     * 商品副标题
     */
    private String goodsSubTitle;

    /**
     * 商品分类id
     */
    private Long gcId;

    /**
     * 商品分类名称
     */
    private String gcName;

    /**
     * 商品分类一级id
     */
    private Long firstGcId;

    /**
     * 商品分类一级名称
     */
    private String firstGcName;

    /**
     * 商品分类二级id
     */
    private Long secondGcId;

    /**
     * 商品分类二级名称
     */
    private String secondGcName;

    /**
     * 商品分类三级id
     */
    private Long thirdGcId;

    /**
     * 商品分类三级名称
     */
    private String thirdGcName;

    /**
     * 商品品牌id
     */
    private Long brandId;

    /**
     * 店铺类型（1:自营商户，2:普通商户）
     */
    private Integer storeType;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商品主图
     */
    private String goodsMainPicture;
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
     * 商品默认对应的规格id
     */
    private Long specId;

    /**
     * 销售价
     */
    private BigDecimal specSellPrice;
    /**
     * 成本价
     */
    private BigDecimal specCostPrice;

    /**
     * 商品店铺价格
     */
    private BigDecimal goodsStorePrice;

    /**
     * 配送方式 0:无需物流 1:快递 2自提 3电子提货码
     */
    private Integer expressFlag;
    /**
     * 运费的承担方式 0无需物流 1买家承担 2卖家承担
     */
    private Integer freightBearType;

    /**
     * 运费模板id
     */
    private Long freightTemplateId;

    /**
     * 商品上下架状态（默认0:下架状态，1上架状态）
     */
    private Integer goodsShow;

    /**
     * 是否发布(上架)过（默认0:不是，1:是）
     */
    private Integer publishFlag;

    /**
     * 商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架
     */
    private Integer goodsStatus;

    /**
     * 商品关键字
     */
    private String goodsKeywords;

    /**
     * 是否允许开具发票（0：不允许，1：允许）
     */
    private Integer invoiceFlag;
    /**
     * 发票类型(多选 1:电子、2：纸质、3：增值税普通发票)1,2,3
     */
    private String invoiceType;
    /**
     * 发票内容（多选 1：商品明细、2：商品类别）1,2
     */
    private String invoiceContent;

    /**
     * 店铺商品一级分类ID
     */

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long firstStoreGoodsGcId;

    /**
     * 店铺商品一级分类名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String firstStoreGoodsGcName;

    /**
     * 店铺商品二级分类ID
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long secondStoreGoodsGcId;

    /**
     * 店铺商品二级分类名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String secondStoreGoodsGcName;

    /**
     * 虚拟商品标记（0:否，1是）
     */
    private Integer virtualFlag;

    /**
     * 配送方式 1快递 2自提 3无需物流，4电子提货
     */
    private Integer devlierType;
    /**
     * 提货码有效期（-1永不过期,其他数字代表有效期天数,不为负数则代表有效期天数）
     */
    private Integer codeValidDay;

    /**
     * 提货码过期退款（0不支持，1支持）
     */
    private Integer codeRefundFlag;
    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
