/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.leimingtech.modules.dto.converter.ConverterMemberSource;
import com.leimingtech.modules.dto.converter.ConverterMemberState;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName MemberExcel
 * @Description 会员表导出
 * @Author DY
 * @Date 2019/5/28 18:06
 * @Version 1.0
 **/
@Data
@ColumnWidth(value = 20)
public class EasyMemberExcelDTO implements Serializable {

    @ExcelProperty(value = "会员名称")
    private String memberName;
    @ExcelProperty(value = "用户等级")
    private String gradeName;
    @ExcelProperty(value = "用户头像")
    private String memberAvatar;
    //    @ExcelProperty(value = "会员来源",replace ={"网站注册_0","安卓注册_1","IOS注册_2","小程序注册_3"})
    @ExcelProperty(value = "会员来源", converter = ConverterMemberSource.class)
    private Integer memberSource;
    //    @ExcelProperty(value = "状态",replace ={"正常_0","锁定_1" })
    @ExcelProperty(value = "状态", converter = ConverterMemberState.class)
    private Integer memberState;
    @ExcelProperty(value = "注册时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}
