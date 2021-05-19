/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

enum AfterStatus {
    //    售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:确认收货,5:退款中,6:退款成功,7:换货失败,8:待换货入库,9:换货出库中,10:换货成功）
    CANCEL(1, "用户取消"),
    RETURN_FAIL(2, "退款失败"),
    WAIT_WAREHOUSING(3, "待退货入库"),
    CONFIRM_RECEIPT(4, "确认收货"),
    RETURN_ING(5, "退款中"),
    RETURN_SUCCESS(6, "退款成功"),
    EXCHANGE_FAIL(7, "换货失败"),
    EXCHANGE_WAREHOUSING(8, "待换货入库"),
    EXCHANGE_OUT(9, "换货出库中"),
    EXCHANGE_SUCCESS(10, "换货成功");
    private Integer value;
    private String desc;

    AfterStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}

/**
 * 售后状态导出Excel转换器
 *
 * @author xuzhch
 * @date 2020年8月31日
 */
public class AftersaleStatusConverter implements Converter<Integer> {


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
//    售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:确认收货,5:退款中,6:退款成功,7:换货失败,8:待换货入库,9:换货出库中,10:换货成功）

        if (value.equals(AfterStatus.CANCEL.getValue())) {
            return new CellData(AfterStatus.CANCEL.getDesc());
        }
        if (value.equals(AfterStatus.RETURN_FAIL.getValue())) {
            return new CellData(AfterStatus.RETURN_FAIL.getDesc());
        }
        if (value.equals(AfterStatus.WAIT_WAREHOUSING.getValue())) {
            return new CellData(AfterStatus.WAIT_WAREHOUSING.getDesc());
        }
        if (value.equals(AfterStatus.CONFIRM_RECEIPT.getValue())) {
            return new CellData(AfterStatus.CONFIRM_RECEIPT.getDesc());
        }
        if (value.equals(AfterStatus.RETURN_ING.getValue())) {
            return new CellData(AfterStatus.RETURN_ING.getDesc());
        }
        if (value.equals(AfterStatus.RETURN_SUCCESS.getValue())) {
            return new CellData(AfterStatus.RETURN_SUCCESS.getDesc());
        }
        if (value.equals(AfterStatus.EXCHANGE_FAIL.getValue())) {
            return new CellData(AfterStatus.EXCHANGE_FAIL.getDesc());
        }
        if (value.equals(AfterStatus.EXCHANGE_WAREHOUSING.getValue())) {
            return new CellData(AfterStatus.EXCHANGE_WAREHOUSING.getDesc());
        }
        if (value.equals(AfterStatus.EXCHANGE_OUT.getValue())) {
            return new CellData(AfterStatus.EXCHANGE_OUT.getDesc());
        }
        if (value.equals(AfterStatus.EXCHANGE_SUCCESS.getValue())) {
            return new CellData(AfterStatus.EXCHANGE_SUCCESS.getDesc());
        }
        return new CellData("");
    }

}
