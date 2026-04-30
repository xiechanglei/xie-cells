package io.github.xiechanglei.cell.common.lang.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间格式化与解析的接口
 */
public class DateParseInterface {
    protected static final String DEFAULT_ZONE = "Asia/Shanghai";
    protected static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_ZONE);
    // simple Date format
    protected static final DateConverter DEFAULT_DATETIME_PARSER = DateConverter.of("yyyy-MM-dd HH:mm:ss", DEFAULT_ZONE_ID);
    protected static final DateConverter DEFAULT_DATE_PARSER = DateConverter.of("yyyy-MM-dd", DEFAULT_ZONE_ID);
    // default ZONE

    protected DateParseInterface() {
    }

    /**
     * 按照默认的格式解析日期，如果是10位的字符串，则按照yyyy-MM-dd解析，如果是19位的字符串，则按照yyyy-MM-dd HH:mm:ss解析
     */
    public Date parse(String dateStr) {
        if (dateStr.length() == 10) {
            return DEFAULT_DATE_PARSER.parseDate(dateStr);
        } else if (dateStr.length() == 19) {
            return DEFAULT_DATETIME_PARSER.parseDateTime(dateStr);
        } else {
            throw new IllegalArgumentException("dateStr length must be 10 or 19");
        }
    }

    public String format(Date date) {
        return DEFAULT_DATETIME_PARSER.format(date);
    }

    /**
     * 以默认时区构建解析日期的解析器，在比较高使用频率的情况下，可以使用该方法构建解析器，避免重复构建解析器
     */
    public DateConverter buildConverter(String pattern) {
        return DateConverter.of(pattern, DEFAULT_ZONE_ID);
    }

    /**
     * 以指定时区构建解析日期的解析器，在比较高使用频率的情况下，可以使用该方法构建解析器，避免重复构建解析器
     */
    public DateConverter buildConverter(String pattern, ZoneId zoneId) {
        return DateConverter.of(pattern, zoneId);
    }

    /**
     * 时间解析器
     */
    public static class DateConverter {
        private DateTimeFormatter formatter;
        private ZoneId zoneId;
        private boolean onlyDate;

        private DateConverter() {
        }

        protected static DateConverter of(String pattern, ZoneId zoneId) {
            DateConverter dateConverter = new DateConverter();
            dateConverter.onlyDate = !(pattern.contains("HH") || pattern.contains("mm") || pattern.contains("ss"));
            dateConverter.formatter = DateTimeFormatter.ofPattern(pattern);
            dateConverter.zoneId = zoneId;
            return dateConverter;
        }

        /**
         * 格式化时间
         */
        public String format(Date date) {
            return this.formatter.format(LocalDateTime.ofInstant(date.toInstant(), this.zoneId));
        }

        /**
         * 根据pattern自动判断是解析日期还是解析时间，最好少用，知道解析什么类型就用什么类型
         */
        public Date parse(String dateStr) {
            return onlyDate ? this.parseDate(dateStr) : this.parseDateTime(dateStr);
        }

        /**
         * 解析日期,pattern 中只有年月日参数
         */
        private Date parseDate(String dateStr) {
            LocalDate localDate = LocalDate.parse(dateStr, this.formatter);
            return Date.from(localDate.atStartOfDay().atZone(this.zoneId).toInstant());
        }

        /**
         * 解析时间，pattern 中有年月日，时分秒字段，
         */
        private Date parseDateTime(String dateStr) {
            LocalDateTime localDate = LocalDateTime.parse(dateStr, this.formatter);
            return Date.from(localDate.atZone(this.zoneId).toInstant());
        }

    }

}
