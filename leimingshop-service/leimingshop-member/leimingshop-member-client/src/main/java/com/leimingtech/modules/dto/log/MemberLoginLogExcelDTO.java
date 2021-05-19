/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.log;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户登录日志表
 *
 * @author dy 1197793912@qq.com
 * @since 1.0.0 2019-05-30
 */
@Data
public class MemberLoginLogExcelDTO {


    @ExcelProperty(value = "会员ID")
    private Long memberId;
    @ExcelProperty(value = "会员名称")
    private String loginName;
    @ExcelProperty(value = "手机号")
    private String phoneNumber;
    @ExcelProperty(value = "操作人ip")
    private String ip;
    @ExcelProperty(value = "登录端")
    private String source;
    @ExcelProperty(value = "操作时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "操作内容")
    private Integer status;


}
