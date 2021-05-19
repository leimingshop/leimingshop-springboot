/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.cart;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Data
@ToString
public class CartEntity implements Serializable {

    private static final long serialVersionUID = 5253252753668590703L;

    @FieldInfo(type = "long")
    private Long id;

    /**
     * 会员ID
     */
    @FieldInfo(type = "long")
    private Long memberId;

    /**
     * 商品所属店铺ID
     */
    @FieldInfo(type = "long")
    private Long storeId;

    /**
     * 商品所属店铺名称
     */
    @FieldInfo
    private String storeName;

    /**
     * 商品ID
     */
    @FieldInfo(type = "long")
    private Long goodsId;

    /**
     * 购买数量
     */
    @FieldInfo(type = "integer")
    private Integer goodsNum;

    /**
     * 商品规格ID
     */
    @FieldInfo(type = "long")
    private Long specId;

    /**
     * 商品规格名称
     */
    @FieldInfo
    private String specName;

    /**
     * 规格编号
     */
    @FieldInfo
    private String specSerial;

    /**
     * 规格实际销售价
     */
    @FieldInfo(type = "float")
    private BigDecimal specPrice;

    /**
     * 规格销售价
     */
    @FieldInfo(type = "float")
    private BigDecimal specSellPrice;

    /**
     * 第一次保存购物车时的价格
     */
    @FieldInfo(type = "float")
    private BigDecimal specSavePrice;

    /**
     * 规格库存
     */
    @FieldInfo(type = "integer")
    private Integer specStorage;

    /**
     * 商品规格主图
     */
    @FieldInfo
    private String specMainPicture;

    /**
     * 商品规格属性值
     */
    @FieldInfo
    private String specInfo;

    /**
     * 商品所在一级分类
     */
    @FieldInfo(type = "long")
    private Long firstGcId;

    /**
     * 品牌id
     */
    @FieldInfo(type = "long")
    private Long brandId;

    /**
     * 活动id
     */
    @FieldInfo(type = "long")
    private Long activityId;

    /**
     * 活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购
     */
    @FieldInfo(type = "integer")
    private Integer activityType;

    /**
     * 活动剩余库存
     */
    @FieldInfo(type = "integer")
    private Integer activitySurplusStorage;

    /**
     * 活动结束时间
     */
    @FieldInfo(type = "date")
    private Date activityEndDate;

    /**
     * 活动限购数量
     */
    @FieldInfo(type = "integer")
    private Integer restrictionQuantity;

    /**
     * 是否选中( 0未选中  1 已选中 )
     */
    @FieldInfo(type = "integer")
    private Integer isSelect;

    /**
     * 更新人
     */
    @FieldInfo
    private String updater;

    /**
     * 更新时间
     */
    @FieldInfo(type = "date")
    private Date updateDate;

    /**
     * 创建者
     */
    @FieldInfo
    private String creator;

    /**
     * 创建时间
     */
    @FieldInfo(type = "date")
    private Date createDate;

    /**
     * 删除标记（默认0:未删除,1:已删除）
     */
    @FieldInfo(type = "integer")
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @FieldInfo(type = "integer")
    private Integer version;


    /**
     * 购物车内商品状态（0正常，1无货 ，2下架）
     */
    @FieldInfo(type = "integer")
    private Integer status;
}
