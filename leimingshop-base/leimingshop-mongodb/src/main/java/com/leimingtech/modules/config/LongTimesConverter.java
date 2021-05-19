/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Date;

@ReadingConverter
@WritingConverter
public class LongTimesConverter implements Converter<Date, Long> {
    @Override
    public Long convert(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }
}
