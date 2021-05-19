/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.payment;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 提现日志表
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Data
public class WithdrawalApplicationLogExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "申请id")
    private Long applyId;
    @Excel(name = "会员id")
    private Long memberId;
    @Excel(name = "状态: 1提交申请；2已取消；3提现审核(已驳回)；4发放审核(已驳回)；5发放审核(待发放）；6发放审核(已发放)；")
    private Integer auditStatus;
    @Excel(name = "创建人")
    private String creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新人")
    private String updater;
    @Excel(name = "更新时间")
    private Date updateDate;

}
