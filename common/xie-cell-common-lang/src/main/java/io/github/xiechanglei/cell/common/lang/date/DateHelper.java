package io.github.xiechanglei.cell.common.lang.date;


import java.util.TimeZone;

import static io.github.xiechanglei.cell.common.lang.date.DateParseInterface.DEFAULT_ZONE;

public class DateHelper {
    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone(DEFAULT_ZONE);
    // 日期解析器，提供了多种日期格式的解析功能，可以将字符串解析为Date对象
    public static DateParseInterface convertor = new DateParseInterface();
}
