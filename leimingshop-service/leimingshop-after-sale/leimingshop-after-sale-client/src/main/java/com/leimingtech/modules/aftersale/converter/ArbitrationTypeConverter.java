/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.leimingtech.modules.aftersale.enmus.AfterSaleEnum;


/**
 * 仲裁类型转换器
 *
 * @author xuzhch
 * @date 2020年8月31日
 */
public class ArbitrationTypeConverter implements Converter<Integer> {
    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        value = "仲裁类型（0：售后-退货、1：售后-换货）"
        if (value.equals(AfterSaleEnum.TYPERETURN.value())) {
            return new CellData("售后-退货");
        }
        if (value.equals(AfterSaleEnum.TYPEBARTER.value())) {
            return new CellData("售后-换货");
        }
        return new CellData("");
    }
}
