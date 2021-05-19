/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 分类推荐管理
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
public class CmsArticleRecommendExcel {
    @ExcelProperty(value = "id")
    private Long id;


}