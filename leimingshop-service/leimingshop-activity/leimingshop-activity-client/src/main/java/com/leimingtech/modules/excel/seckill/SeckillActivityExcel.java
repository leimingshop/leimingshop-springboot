/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.seckill;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 秒杀活动实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
public class SeckillActivityExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "场次id")
    private Long sessionId;
    @ExcelProperty(value = "店铺id")
    private Long storeId;
    @ExcelProperty(value = "活动名称")
    private String activityName;
    @ExcelProperty(value = "活动开始时间")
    private Date activityStartDate;
    @ExcelProperty(value = "活动结束时间")
    private Date activityEndDate;
    @ExcelProperty(value = "活动有效时间")
    private Integer activityEffectiveTime;
    @ExcelProperty(value = "会员等级id")
    private Long memberGradeId;
    @ExcelProperty(value = "会员等级名称")
    private String memberGradeName;
    @ExcelProperty(value = "会员等级id")
    private Integer memberGradeLimit;
    @ExcelProperty(value = "活动限购数量")
    private Integer restrictionQuantity;
    @ExcelProperty(value = "浏览数")
    private Integer browseNum;
    @ExcelProperty(value = "下单数")
    private Integer orderNum;
    @ExcelProperty(value = "审核状态 0未审核 1审核通过 2审核拒绝")
    private Integer auditState;
    @ExcelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;
    @ExcelProperty(value = "积分是否可用 0不可用 1可用")
    private Integer pointFlag;
    @ExcelProperty(value = "余额是否可用 0不可用 1可用")
    private Integer balanceFlag;
    @ExcelProperty(value = "优惠券是否可用 0不可用 1可用")
    private Integer couponsFlag;
    @ExcelProperty(value = "满减是否可用 0不可用 1可用")
    private Integer reduceFlag;
    @ExcelProperty(value = "满包邮是否可用 0不可用 1可用")
    private Integer freeShippingFlag;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
