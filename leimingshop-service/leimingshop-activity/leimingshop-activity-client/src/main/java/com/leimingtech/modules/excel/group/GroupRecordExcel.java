/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.group;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 拼团记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
public class GroupRecordExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "店铺id")
    private Long storeId;
    @ExcelProperty(value = "拼团id")
    private Long groupId;
    @ExcelProperty(value = "创建拼团会员id")
    private Long memberId;
    @ExcelProperty(value = "创建拼团会员名称")
    private String memberName;
    @ExcelProperty(value = "拼团活动名称")
    private String groupName;
    @ExcelProperty(value = "商品id")
    private Long goodsId;
    @ExcelProperty(value = "规格id")
    private Long specId;
    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "商品主图")
    private String goodsMainPicture;
    @ExcelProperty(value = "发起时间")
    private Date startTime;
    @ExcelProperty(value = "成团超时时间")
    private Date overTime;
    @ExcelProperty(value = "实际成团时间")
    private Date actualTime;
    @ExcelProperty(value = "所需成团人数")
    private Integer needNum;
    @ExcelProperty(value = "拼团状态")
    private Integer groupStatus;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
