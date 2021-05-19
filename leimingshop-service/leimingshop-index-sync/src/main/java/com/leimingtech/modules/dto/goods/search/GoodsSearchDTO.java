/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.search;

import com.leimingtech.modules.dto.goods.search.label.GoodsLabelSearchDTO;
import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品搜索DTO
 *
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 5:50 PM
 */
@Data
public class GoodsSearchDTO implements Serializable {

    private static final long serialVersionUID = -5952699787340823928L;
    /**
     * 主键
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 商品名称
     */
    @FieldInfo(participle = 3)
    private String goodsName;
    /**
     * 商品货号
     */
    @FieldInfo(type = "long")
    private Long goodsSerial;

    /**
     * 商品副标题
     */
    @FieldInfo(participle = 1)
    private String goodsSubTitle;

    /**
     * 商品分类id
     */
    @FieldInfo(type = "long")
    private Long gcId;

    /**
     * 商品分类名称
     */
    @FieldInfo
    private String gcName;
    /**
     * 商品分类一级id
     */
    @FieldInfo(type = "long")
    private Long firstGcId;
    /**
     * 商品分类一级名称
     */
    @FieldInfo
    private String firstGcName;
    /**
     * 商品分类二级id
     */
    @FieldInfo(type = "long")
    private Long secondGcId;
    /**
     * 商品分类二级名称
     */
    @FieldInfo
    private String secondGcName;

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
     * 店铺商品二级分类ID
     */
    @FieldInfo(type = "long")
    private Long secondStoreGoodsGcId;
    /**
     * 店铺商品二级分类名称
     */
    @FieldInfo
    private String secondStoreGoodsGcName;

    /**
     * 商品品牌id
     */
    @FieldInfo(type = "long")
    private Long brandId;

    /**
     * 品牌名称
     */
    @FieldInfo
    private String brandName;

    /**
     * 店铺ID
     */
    @FieldInfo(type = "long")
    private Long storeId;

    /**
     * 店铺名称
     */
    @FieldInfo
    private String storeName;

    /**
     * 店铺类型
     */
    @FieldInfo(type = "integer")
    private String storeType;
    /**
     * 商品默认对应的规格id
     */
    @FieldInfo(type = "long")
    private Long specId;
    /**
     * 是否开具发票（0：不开票，1：开票）
     */
    @FieldInfo(type = "integer")
    private Integer invoiceFlag;
    /**
     * 发票类型(多选 1:电子、2：纸质、3：增值税普通发票)
     */
    @FieldInfo
    private String invoiceType;
    /**
     * 发票内容（多选 1：商品明细、2：商品类别）
     */
    @FieldInfo
    private String invoiceContent;

    /**
     * 商品上下架状态（默认0:下架状态，1上架状态）
     */
    @FieldInfo(type = "integer")
    private Integer goodsShow;
    /**
     * 商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架
     */
    @FieldInfo(type = "integer")
    private Integer goodsStatus;

    /**
     * 商品评价数
     */
    @FieldInfo(type = "integer")
    private Integer evaluateCount;

    /**
     * 好评率
     */
    @FieldInfo
    private String goodEvaluate;

    /**
     * 销售价
     */
    @FieldInfo(type = "float")
    private BigDecimal specSellPrice;

    /**
     * 商品主图
     */
    @FieldInfo
    private String goodsMainPicture;
    /**
     * 100*100图片地址
     */
    @FieldInfo
    private String oneHundredPxPictureUrl;
    /**
     * 200*200图片地址
     */
    @FieldInfo
    private String twoHundredPxPictureUrl;
    /**
     * 400*400图片地址
     */
    @FieldInfo
    private String fourHundredPxPictureUrl;
    /**
     * 800*800图片地址
     */
    @FieldInfo
    private String eightHundredPxPictureUrl;

    /**
     * 商品销量
     */
    @FieldInfo(type = "integer")
    private Integer goodsSaleNum;

//    /**
//     * 配送方式 1快递 2自提 3无需物流，4电子提货
//     */
//    @FieldInfo(type = "integer")
//    private Integer devlierType;

    /**
     * 虚拟商品标记（0:否，1是）
     */
    @FieldInfo(type = "integer")
    private Integer virtualFlag;
    /**
     * 提货码有效期（-1永不过期,其他数字代表有效期天数）
     */
    @FieldInfo(type = "integer")
    private Integer codeValidDay;
    /**
     * 提货码过期退款（0不支持，1支持）
     */
    @FieldInfo(type = "integer")
    private Integer codeRefundFlag;

    /**
     * 商品属性值 （使用嵌套类型）
     */
    @FieldInfo(type = "nested")
    private List<GoodsAttrSearchDTO> attrValues;

    /**
     * 上架时间
     */
    @FieldInfo(type = "date")
    private Date goodsUpTime;
    /**
     * 商品类型  1 自营商品 2 普通商品
     */
    @FieldInfo(type = "integer")
    private Integer goodsType;

    /**
     * 搜索关键字集合
     */
    @FieldInfo(pinyin = 1)
    private List<String> keywordTips;
    /**
     * 商品标签集合（使用嵌套类型） labelSearchDTOS
     */
    @FieldInfo(type = "nested")
    private List<GoodsLabelSearchDTO> goodsLabels;

    /**
     * 商品图片集合
     */
    @FieldInfo(type = "nested")
    private List<GoodsPictureDTO> goodsPicList;
    /**
     * 是否为满减商品 0否 1是
     */
    @FieldInfo(type = "integer")
    private Integer reduceFlag;

    /**
     * 配送方式 0:无需物流 1:快递 2自提 3电子提货码
     */
    @FieldInfo(type = "integer")
    private Integer expressFlag;

    /**
     * 运费模板id
     */
    @FieldInfo(type = "long")
    private Long freightTemplateId;
    /**
     * 收藏量
     */
    @FieldInfo(type = "integer")
    private Integer collectNum;
    /**
     * 浏览数量
     */
    @FieldInfo(type = "integer")
    private Integer browseNum;

}
