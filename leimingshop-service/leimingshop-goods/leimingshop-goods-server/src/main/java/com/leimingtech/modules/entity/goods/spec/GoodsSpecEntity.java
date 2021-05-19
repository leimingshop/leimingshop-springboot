/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods.spec;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:51
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_spec")
public class GoodsSpecEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 是否是主规格（默认0不是，1是）
     */
    @ApiModelProperty(value = "是否是主规格（默认0不是，1是）")
    private Integer mainFlag;

    /**
     * 是否启用（默认0启用，1未启用）
     */
    private Integer isEnable;

    /**
     * 商品规格主图
     */
    private String specMainPicture;

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
     * 规格销售数量
     */
    private Integer specSaleNum;
    /**
     * 规格重量
     */
    private Double specWeight;
    /**
     * 规格上下架状态（默认:2未上架,0下架状态，1上架状态）
     */
    private Integer specShow;

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
     * 删除标记（默认为0未删除，1已删除）
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
