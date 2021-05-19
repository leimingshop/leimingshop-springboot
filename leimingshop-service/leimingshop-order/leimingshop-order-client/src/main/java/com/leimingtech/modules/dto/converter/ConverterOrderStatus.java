/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.Data;

/**
 * <订单状态转换文字实体>
 * 上面程序的 Converter 接口的泛型是指要转换的 Java 数据类型，与 supportJavaTypeKey 方法中的返回值类型一致
 *
 * @author weixianchun
 * @date 2020/3/11 13:57
 **/
@Data
public class ConverterOrderStatus implements Converter<Integer> {

    //订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;
    public static final Integer ORDER_CANCEL = 0;
    public static final Integer ORDER_PENDING_PAYMENT = 10;
    public static final Integer ORDER_PENDING_SHIP = 20;
    public static final Integer ORDER_PENDING_RECEIPT = 30;
    public static final Integer ORDER_COMPLETE = 40;

    //Converter 接口的泛型是指要转换的 Java 数据类型，与 supportJavaTypeKey 方法中的返回值类型一致

    /**
     * 功能描述:
     * <p>
     * <返回Java中的对象类型>
     *
     * @return 支持Java类
     * @date 2020/3/11 14:32
     * @author weixianchun
     **/
    @Override
    public Class supportJavaTypeKey() {
        return Integer.class;
    }

    /**
     * 功能描述:
     * <返回excel中的对象枚举>
     * Excel内部数据类型:STRING,NUMBER,BOOLEAN,EMPTY,ERROR,
     * IMAGE - 当前仅在写入时支持图像
     * DIRECT_STRING -无需在“ sharedStrings.xml”中读取此类数据，仅用于过渡使用，并且数据*将存储为,@link #STRING
     *
     * @return {@link CellDataTypeEnum}的支持
     * @date 2020/3/11 14:33
     * @author weixianchun
     **/
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /**
     * 功能描述:
     * <将excel对象转换为Java对象>(这里读的时候会调用)
     *
     * @param cellData             Excel单元格数据
     * @param excelContentProperty 内容属性。可为空
     * @param globalConfiguration  全局配置
     * @return 放入Java对象的数据
     * @date 2020/3/11 14:33
     * @author weixianchun
     **/
    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    /**
     * 功能描述:
     * <将Java对象转换为excel对象>(这里是写的时候会调用)
     *
     * @param value
     * @param contentProperty     内容属性。可为空
     * @param globalConfiguration 全局配置
     * @return 数据放入Excel
     * @date 2020/3/12 17:11
     * @author weixianchun
     **/
    @Override
    public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        //订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;
        if (ORDER_CANCEL.equals(value)) {
            return new CellData("已取消");
        } else if (ORDER_PENDING_PAYMENT.equals(value)) {
            return new CellData("待付款");
        } else if (ORDER_PENDING_SHIP.equals(value)) {
            return new CellData("待发货");
        } else if (ORDER_PENDING_RECEIPT.equals(value)) {
            return new CellData("待收货");
        } else {
            return new CellData("交易完成");
        }
    }


}
