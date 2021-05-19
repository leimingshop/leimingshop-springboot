/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods;

import com.leimingtech.modules.constants.CollectionName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品统计保存对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@Document(collection = CollectionName.GOODS_STATISTICS)
public class GoodsStatisEntity {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "一级分类ID")
    private Long firstGcId;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "规格货号")
    private String specSerial;

    @ApiModelProperty(value = "商品货号")
    private Long goodsSerial;

    @ApiModelProperty(value = "规格名称")
    private String specName;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    @ApiModelProperty(value = "浏览量")
    private Integer pageView;

    @ApiModelProperty(value = "浏览人数")
    private Integer visitorsNumber;

    @ApiModelProperty(value = "付款人数")
    private Integer paymentNumber;

    @ApiModelProperty(value = "购买人ID")
    private Long buyerId;

    @ApiModelProperty(value = "销售数量")
    private Integer salesNumber;

    @ApiModelProperty(value = "销售金额")
    private BigDecimal salesAmount;

    @ApiModelProperty(value = "创建时间（日）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（月）")
    private Date createMonthTime;


}
