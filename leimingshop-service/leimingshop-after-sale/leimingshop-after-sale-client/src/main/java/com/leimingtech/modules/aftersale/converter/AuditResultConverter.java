/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 审核结果导出转换器
 *
 * @author xuzhch
 * @date 2020年8月31日
 */
public class AuditResultConverter implements Converter<Integer> {

    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //    审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消）
        switch (value) {
            case 1:
                return new CellData("审核通过");
            case 2:
                return new CellData("审核拒绝");
            case 3:
                return new CellData("审核中");
            case 4:
                return new CellData("用户取消");
            default:
                return new CellData("");
        }
    }
}
