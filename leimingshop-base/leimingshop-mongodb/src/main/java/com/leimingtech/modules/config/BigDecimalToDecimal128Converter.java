/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.config;

import org.bson.types.Decimal128;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

//@ReadingConverter
@Configuration
//@WritingConverter
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {

    @Override
    public Decimal128 convert(BigDecimal bigDecimal) {
        return new Decimal128(bigDecimal);
    }

}
