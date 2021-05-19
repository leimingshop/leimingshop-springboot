/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;

import com.leimingtech.modules.dto.goods.search.label.GoodsLabelSearchDTO;
import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author tyl
 * @Date 2019/7/3 18:41
 * @Description 商品规格
 * lmshop_goods_spec
 **/
@Data
public class SpecVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 规格名
     */
    @FieldInfo
    private String specName;

    @FieldInfo
    private String goodsSubTitle;
    /**
     * 商品id
     */
    @FieldInfo(type = "long")
    private Long goodsId;

    /**
     * 库存
     */
    @FieldInfo(type = "integer")
    private Integer specStorage;

    /**
     * 商品重量
     */
    @FieldInfo(type = "float")
    private Double specWeight;


    /**
     * 规格上下架状态（2未上架,0下架状态，1上架状态）
     */
    @FieldInfo(type = "integer")
    private Integer specShow;

    /**
     * 商品上下架状态（2未上架,0下架状态，1上架状态）
     */
    @FieldInfo(type = "integer")
    private Integer goodsShow;

    /**
     * 配送方式 0:无需物流 1:快递 2自提 3电子提货码
     */
    @FieldInfo(type = "integer")
    private Integer expressFlag;
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
     * 是否是主规格（默认0不是，1是）
     */
    @FieldInfo(type = "integer")
    private Integer mainFlag;

    /**
     * 售价
     */
    @FieldInfo(type = "float")
    private BigDecimal specSellPrice;

    /**
     * 商品的售价
     */
    @FieldInfo(type = "float")
    private BigDecimal goodsSellPrice;
    /**
     * 商品规格属性值名称（中间用逗号隔开）
     */
    @FieldInfo
    private String specAttrName;
    /**
     * 商品标签集合（使用嵌套类型） labelSearchDTOS
     */
    @FieldInfo(type = "nested")
    private List<GoodsLabelSearchDTO> goodsLabels;
    /**
     * 商品规格属性和属性值名称（key:value）
     */
    @FieldInfo
    private String specAttrValueName;
    /**
     * 商品规格主图
     */
    @FieldInfo
    private String specMainPicture;
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
     * 详情大图片
     */
    @FieldInfo
    private String goodsBoby;
    /**
     * 详情大图片（H5）
     */
    @FieldInfo
    private String mobileBody;
    /**
     * 售后详情
     */
    @FieldInfo
    private String afterSale;

    /**
     * 销量
     */
    @FieldInfo(type = "integer")
    private Integer specSaleNum;
    /**
     * 商品信息
     */
    @FieldInfo(type = "object")
    private GoodsVO goodsVO;


    /**
     * 规格属性值图片
     */
    @FieldInfo(type = "object")
    private List<SpecAttrValuePic> specAttrValuePicList;

    /**
     * 商品默认集合图片
     */
    @FieldInfo(type = "object")
    private List<SpecGoodsPic> goodsPicList;

    /**
     * 商品活动集合
     */
    @FieldInfo(type = "nested")
    private List<SpecActivityVO> specActivityList;

    /**
     * 是否删除 0 未删除 1已删除
     */
    @FieldInfo(type = "integer")
    private Integer delFlag;

    /**
     * 商品类型  1 自营商品 2 普通商品
     */
    @FieldInfo(type = "integer")
    private Integer goodsType;

    /**
     * 是否为满减商品 0否 1是
     */
    @FieldInfo(type = "integer")
    private Integer reduceFlag;

    /**
     * 商品规格编号
     */
    @FieldInfo
    private String specSerial;

}
