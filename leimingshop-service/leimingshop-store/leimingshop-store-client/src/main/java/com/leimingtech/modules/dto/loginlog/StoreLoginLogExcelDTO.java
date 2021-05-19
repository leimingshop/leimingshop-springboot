/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.loginlog;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 店铺登录日志导出实体
 * @Date :2019/6/28 10:54
 * @Version V1.0
 **/
@Data
public class StoreLoginLogExcelDTO {

    @ExcelProperty("用户名")
    private String creator;
    @ExcelProperty("用户操作")
    private Integer operation;
    @ExcelProperty(value = "状态")
    private Integer status;
    @ExcelProperty(value = "操作IP")
    private String ip;
    @ExcelProperty(value = "操作时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}
