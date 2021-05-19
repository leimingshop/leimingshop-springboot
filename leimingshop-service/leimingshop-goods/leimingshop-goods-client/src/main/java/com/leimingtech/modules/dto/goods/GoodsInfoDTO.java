/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品附加信息表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsInfoDTO")
public class GoodsInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品详细内容")
    private String goodsBoby;
    @ApiModelProperty(value = "商品详细内容(手机版)")
    private String mobileBody;
    @ApiModelProperty(value = "商品运费承担方式(默认 0为买家承担 1为卖家承担)")
    private Integer goodsTransfeeCharge;
    @ApiModelProperty(value = "售后服务")
    private String afterSale;
    @ApiModelProperty(value = "商品所在地(市)")
    private Long cityId;
    @ApiModelProperty(value = "市名称")
    private String cityName;
    @ApiModelProperty(value = "商品所在地(省)")
    private Long provinceId;
    @ApiModelProperty(value = "省名称")
    private String provinceName;
    @ApiModelProperty(value = "评论次数")
    private Integer commentNum;
    @ApiModelProperty(value = "售出数量")
    private Integer saleNum;
    @ApiModelProperty(value = "商品收藏数量")
    private Integer goodsCollectNum;
    @ApiModelProperty(value = "商品浏览数")
    private Integer goodsBrowseNum;
    @ApiModelProperty(value = "乐观锁")
    private Integer version;

}