package io.github.xiechanglei.cell.starter.spring.log;

import org.slf4j.Marker;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件标记，当日志中包含该标记时，将日志输出到指定文件
 *
 * @author xie
 * @date 2025/4/2
 */
public class FileMarker extends NamedMarker {
    private static final String MARK_PREFIX = "FILE_MARK_";

    private static final Map<String, Marker> MARKER_MAP = new ConcurrentHashMap<>();

    public FileMarker(String name) {
        super(name);
    }

    public static Marker of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        name = MARK_PREFIX + name;
        return MARKER_MAP.computeIfAbsent(name, FileMarker::new);
    }

    public static String convert(Marker marker) {
        if (marker == null || marker.getName() == null || !marker.getName().startsWith(MARK_PREFIX)) {
            return null;
        }
        String fileName = sanitizeFolderName(marker.getName().substring(MARK_PREFIX.length()));
        return StringUtils.hasText(fileName) ? fileName : null;
    }

    /**
     * 格式化fileName,防止出现问题
     */
    private static String sanitizeFolderName(String name) {
        String sanitized = name.replaceAll("[\\\\/:*?\"<>|@#$&\\s.]+", "_");
        return sanitized.length() > 240 ? sanitized.substring(0, 240) : sanitized;
    }

}
