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
 * 审核状态导出转换器
 *
 * @author xuzhch
 * @date 2020年8月31日
 */
public class AfterApplytypeConverter implements Converter<Integer> {
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
        //    审核流程（1:商家审核,2:平台审核,3:仲裁审核）
        switch (value) {
            case 1:
                return new CellData("商家审核");
            case 2:
                return new CellData("平台审核");
            case 3:
                return new CellData("仲裁审核");
            default:
                return new CellData("");
        }
    }
}
