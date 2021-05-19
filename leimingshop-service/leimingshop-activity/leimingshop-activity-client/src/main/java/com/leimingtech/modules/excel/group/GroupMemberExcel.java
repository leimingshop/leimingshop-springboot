/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.group;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 拼团记录用户管理
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
public class GroupMemberExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "用户id")
    private Long memberId;
    @ExcelProperty(value = "用户名称")
    private String memberName;
    @ExcelProperty(value = "用户头像")
    private String memberImage;
    @ExcelProperty(value = "拼团活动id")
    private Long groupId;
    @ExcelProperty(value = "拼团记录id")
    private Long groupRecordId;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
