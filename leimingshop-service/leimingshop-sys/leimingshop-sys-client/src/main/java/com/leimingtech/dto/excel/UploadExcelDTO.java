/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description
 * @Data: 2020/5/6 10:38
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel(value = "导入禁用词 ")
public class UploadExcelDTO {

    @ExcelProperty(value = "禁用词名称")
    private String name;
}
