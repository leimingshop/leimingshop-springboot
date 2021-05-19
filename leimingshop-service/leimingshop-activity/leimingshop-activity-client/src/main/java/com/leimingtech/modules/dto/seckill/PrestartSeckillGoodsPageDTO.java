/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import com.leimingtech.commons.tools.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <即将开始秒杀商品分页展示实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/13
 */
@Data
@ApiModel("即将开始秒杀商品分页展示实体")
public class PrestartSeckillGoodsPageDTO {

    @ApiModelProperty("活动id")
    private Long activityId;

    @ApiModelProperty("场次id")
    private Long sessionId;

    @ApiModelProperty("商品spu id")
    private Long id;

    @ApiModelProperty("商品sku id")
    private Long specId;

    @ApiModelProperty("商品主图")
    private String goodsMainPicture;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品副标题")
    private String goodsSubTitle;

    @ApiModelProperty("活动开始时间")
    private Date activityStartDate;

    @ApiModelProperty("活动开始时间字符串")
    private String activityStartDateStr;

    @ApiModelProperty(value = "活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "活动库存")
    private Integer activityStorage;

    @ApiModelProperty(value = "限购数量")
    private Integer restrictionQuantity;

    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;

    @ApiModelProperty(value = "是否设置提醒 0未提醒 1已提醒")
    private Integer remindFlag;

    public String getactivityStartDateStr() {
        if (activityStartDate != null) {
            return DateUtils.format(activityStartDate, "HH:mm");
        } else {
            return null;
        }
    }

}
