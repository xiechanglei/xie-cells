package io.github.xiechanglei.cell.common.lang.date;


import java.time.ZoneId;

public class DateHelper {

    protected static final String DEFAULT_ZONE = "Asia/Shanghai";
    protected static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_ZONE);
    // 日期解析器，提供了多种日期格式的解析功能，可以将字符串解析为Date对象
    public static final DateParseInterface convertor = new DateParseInterface();
}
