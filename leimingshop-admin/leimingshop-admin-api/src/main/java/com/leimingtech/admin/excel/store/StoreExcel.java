/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.store;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 店铺表
 *
 * @author chengqian 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
public class StoreExcel {
    @ExcelProperty(value = "商户ID")
    private String id;
    @ExcelProperty(value = "店铺名称")
    private String storeName;
    @ExcelProperty(value = "商户管理账号")
    private String account;
    @ExcelProperty(value = "商户等级")
    private String gradeName;
    @ExcelProperty(value = "商户类别")
    private String storeType;
    @ExcelProperty(value = "状态")
    private String isEnable;
    @ExcelProperty(value = "创建时间")
    private String createDate;

}
