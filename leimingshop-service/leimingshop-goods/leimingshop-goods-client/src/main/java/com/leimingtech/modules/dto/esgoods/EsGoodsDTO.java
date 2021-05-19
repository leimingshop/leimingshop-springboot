/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.esgoods;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 售后模板
 *
 * @author tyl
 * @since 1.0.0 2019-06-11
 */
@Data
@ApiModel(description = "EsGoodsDTO")
public class EsGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;
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
     * 商品品牌id
     */
    private Long brandId;

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
     * 商品默认对应的规格id
     */
    private Long specId;

    /**
     * 商品店铺价格
     */
    private BigDecimal goodsStorePrice;

    /**
     * 商品上下架状态（默认0:下架状态，1上架状态）
     */
    private Integer goodsShow;

    /**
     * 商品属性搜索名称
     */
    private String attrName;
    /**
     * 商品属性搜索名值
     */
    private String attrValue;

    /**
     * 商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架
     */
    private Integer goodsStatus;

    /**
     * 商品关键字
     */
    private String goodsKeywords;


    /**
     * 删除标记（默认0:未删除,1:已删除）
     */

    private Integer delFlag;

    /**
     * 价格
     */
    private Float specSellPrice;
    /**
     * 图片
     */
    private String specMainPicture;
    /**
     * 属性删除状态
     */
    private Integer lsDelFlag;
    /**
     * 上下架
     */
    private Integer gsGoodsShow;
    /**
     * 销量
     */
    private Integer saleNum;
    /**
     * 评论树
     */
    private Integer commentNum;


}
