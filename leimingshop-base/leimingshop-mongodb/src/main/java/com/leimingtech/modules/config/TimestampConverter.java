/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.sql.Timestamp;
import java.util.Date;

@WritingConverter
@ReadingConverter
public class TimestampConverter implements Converter<Date, Timestamp> {
    @Override
    public Timestamp convert(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }


}
