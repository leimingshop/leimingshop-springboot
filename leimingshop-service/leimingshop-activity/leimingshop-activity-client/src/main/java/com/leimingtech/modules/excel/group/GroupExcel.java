/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.group;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 拼团
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
public class GroupExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "店铺ID")
    private Long storeId;
    @ExcelProperty(value = "拼团名称")
    private String groupName;
    @ExcelProperty(value = "拼团开始时间")
    private Date startTime;
    @ExcelProperty(value = "拼团结束时间")
    private Date endTime;
    @ExcelProperty(value = "参团条件（默认0无限制，1新用户）")
    private Integer joinFlag;
    @ExcelProperty(value = "活动预热时间（小时）")
    private Integer groupPreheat;
    @ExcelProperty(value = "推荐拼团（0开启，1关闭）")
    private Integer recommendFlag;
    @ExcelProperty(value = "模拟成团（0开启，1关闭）")
    private Integer simulateFlag;
    @ExcelProperty(value = "成团有效时间（小时）")
    private Integer validTime;
    @ExcelProperty(value = "订单支付有效期（分钟）")
    private Integer payEndTime;
    @ExcelProperty(value = "下单使用优惠（0允许，默认1不允许）")
    private Integer discountFlag;
    @ExcelProperty(value = "下单可用抵扣（（0允许，默认1不允许））")
    private Integer deductionFlag;
    @ExcelProperty(value = "审核状态（10：发布 20：审核 30：审核通过 40：审核未通过 50：取消）")
    private Integer auditStatus;
    @ExcelProperty(value = "拼团众筹活动状态 10：未开始 20：活动中 30：活动结束")
    private Integer activityStatus;
    @ExcelProperty(value = "商品个数（待定）")
    private Integer goodsNum;
    @ExcelProperty(value = "成团个数（待定）")
    private Integer groupNum;
    @ExcelProperty(value = "支付订单数（待定）")
    private Integer paymentNum;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
