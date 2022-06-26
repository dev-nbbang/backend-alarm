package com.dev.nbbang.alarm.domain.notify.util;

import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class NotifyTypeConverter implements Converter<String, NotifyType> {
    @Override
    public NotifyType convert(String source) {
        try {
            System.out.println(source);
            return NotifyType.valueOf(source.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
