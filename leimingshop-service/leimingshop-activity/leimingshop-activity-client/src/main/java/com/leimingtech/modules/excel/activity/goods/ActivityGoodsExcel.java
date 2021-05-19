/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.activity.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动商品实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
public class ActivityGoodsExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "秒杀场次id")
    private Long sessionId;
    @ExcelProperty(value = "活动id")
    private Long activityId;
    @ExcelProperty(value = "活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;
    @ExcelProperty(value = "商品spu id")
    private Long goodsId;
    @ExcelProperty(value = "商品sku id")
    private Long specId;
    @ExcelProperty(value = "活动库存")
    private Integer activityStorage;
    @ExcelProperty(value = "活动剩余库存")
    private Integer activitySurplusStorage;
    @ExcelProperty(value = "活动价格")
    private BigDecimal activityPrice;
    @ExcelProperty(value = "下单数")
    private Integer orderNum;
    @ExcelProperty(value = "单次购买件数（仅限制拼团活动）")
    private Integer onceBuyLimit;
    @ExcelProperty(value = "参团次数限制（仅限制拼团活动）")
    private Integer joinLimit;
    @ExcelProperty(value = "成团人数（仅限制拼团活动）")
    private Integer regimentNum;
    @ExcelProperty(value = "排序")
    private Integer sort;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
