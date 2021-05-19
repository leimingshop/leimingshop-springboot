/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author tyl
 * @Date 2019/7/3 18:42
 * @Description 商品信息
 * lmshop_goods
 **/
@Data
public class GoodsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @FieldInfo(type = "long")
    private Long id;

    /**
     * 分类id
     */
    @FieldInfo(type = "long")
    private Long gcId;
    /**
     * 品牌id
     */
    @FieldInfo(type = "long")
    private Long brandId;
    /**
     * 店铺id
     */
    @FieldInfo(type = "long")
    private Long storeId;

    /**
     * 运费模板id
     */
    @FieldInfo(type = "long")
    private Long freightTemplateId;

    /**
     * 分类名称
     */
    @FieldInfo
    private String gcName;

    @FieldInfo(type = "long")
    private Long firstGcId;

    @FieldInfo
    private String firstGcName;

    @FieldInfo(type = "long")
    private Long secondGcId;

    @FieldInfo
    private String secondGcName;


    /**
     * 商品副标题
     */
    @FieldInfo
    private String goodsSubTitle;

    /**
     * 品牌名称
     */
    @FieldInfo
    private String brandName;

    /**
     * 店铺名称
     */
    @FieldInfo
    private String storeName;

    /**
     * 店铺类型（1:自营商户，2:普通商户）
     */
    @FieldInfo
    private String storeType;

    /**
     * 店铺LOGO
     */
    @FieldInfo
    private String storeLogo;

    /**
     * 店铺等级
     */
    @FieldInfo
    private String storeGrade;

    /**
     * 商品对应的规格
     */
    @FieldInfo(type = "long")
    private Long specId;

    /**
     * 店铺商品一级分类ID
     */
    @FieldInfo(type = "long")
    private Long firstStoreGoodsGcId;

    /**
     * 店铺商品一级分类名称
     */
    @FieldInfo
    private String firstStoreGoodsGcName;

    /**
     * 商品属性
     */
    @FieldInfo(type = "object")
    private List<GoodsAttrVO> goodsAttrVOList;

    /**
     * 商品规格属性名集合
     */
    @FieldInfo(type = "object")
    private List<GoodsSpecAttrVO> goodsSpecAttrVOList;


    @FieldInfo(type = "object")
    private List<GoodsInfo> goodsInfo;

    /**
     * 规格属性值关联
     */
    @FieldInfo(type = "object")
    private List<SpecAttrValueRefVO> specAttrValueRefVO;
}
