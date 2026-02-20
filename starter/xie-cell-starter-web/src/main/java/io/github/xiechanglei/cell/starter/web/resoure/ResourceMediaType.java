package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 资源媒体类型枚举
 * <p>
 * 定义常见的浏览器可预览资源的媒体类型
 * </p>
 *
 * @author xie
 * @date 2026/2/20
 */
@Getter
@RequiredArgsConstructor
public enum ResourceMediaType {

    // ==================== 图片类型 ====================
    /**
     * JPEG 图片
     */
    JPEG("image", "jpeg", "image/jpeg", ".jpg", ".jpeg", ".jpe"),
    /**
     * PNG 图片
     */
    PNG("image", "png", "image/png", ".png"),
    /**
     * GIF 图片
     */
    GIF("image", "gif", "image/gif", ".gif"),
    /**
     * WebP 图片
     */
    WEBP("image", "webp", "image/webp", ".webp"),
    /**
     * SVG 图片
     */
    SVG("image", "svg+xml", "image/svg+xml", ".svg"),
    /**
     * BMP 图片
     */
    BMP("image", "bmp", "image/bmp", ".bmp"),
    /**
     * ICO 图标
     */
    ICO("image", "x-icon", "image/x-icon", ".ico"),

    // ==================== 视频类型 ====================
    /**
     * MP4 视频
     */
    MP4("video", "mp4", "video/mp4", ".mp4", ".m4v"),
    /**
     * WebM 视频
     */
    WEBM("video", "webm", "video/webm", ".webm"),
    /**
     * Ogg 视频
     */
    OGG_VIDEO("video", "ogg", "video/ogg", ".ogv"),
    /**
     * QuickTime 视频
     */
    QUICKTIME("video", "quicktime", "video/quicktime", ".mov"),
    /**
     * AVI 视频
     */
    AVI("video", "x-msvideo", "video/x-msvideo", ".avi"),

    // ==================== 音频类型 ====================
    /**
     * MP3 音频
     */
    MP3("audio", "mpeg", "audio/mpeg", ".mp3"),
    /**
     * WAV 音频
     */
    WAV("audio", "wav", "audio/wav", ".wav"),
    /**
     * Ogg 音频
     */
    OGG_AUDIO("audio", "ogg", "audio/ogg", ".oga", ".ogg"),
    /**
     * WebM 音频
     */
    WEBM_AUDIO("audio", "webm", "audio/webm", ".weba"),
    /**
     * AAC 音频
     */
    AAC("audio", "aac", "audio/aac", ".aac"),
    /**
     * FLAC 音频
     */
    FLAC("audio", "flac", "audio/flac", ".flac"),

    // ==================== 文档类型 ====================
    /**
     * PDF 文档
     */
    PDF("application", "pdf", "application/pdf", ".pdf"),
    /**
     * Plain Text 文本
     */
    TEXT("text", "plain", "text/plain", ".txt"),
    /**
     * HTML 文档
     */
    HTML("text", "html", "text/html", ".html", ".htm"),
    /**
     * XML 文档
     */
    XML("text", "xml", "text/xml", ".xml"),
    /**
     * JSON 文档
     */
    JSON("application", "json", "application/json", ".json");

    /**
     * 主类型（image/video/audio/application/text）
     */
    private final String type;

    /**
     * 子类型
     */
    private final String subtype;

    /**
     * 完整的 MIME 类型
     */
    private final String mimeType;

    /**
     * 文件扩展名列表
     */
    private final String[] extensions;

    /**
     * 根据文件扩展名获取媒体类型
     *
     * @param extension 文件扩展名（带点或不带点均可）
     * @return 对应的媒体类型，未找到返回 null
     */
    public static ResourceMediaType fromExtension(String extension) {
        if (extension == null) {
            return null;
        }
        // 确保扩展名以点开头
        String ext = extension.startsWith(".") ? extension.toLowerCase() : "." + extension.toLowerCase();
        for (ResourceMediaType type : values()) {
            for (String t : type.extensions) {
                if (t.equals(ext)) {
                    return type;
                }
            }
        }
        return null;
    }

    /**
     * 根据文件名获取媒体类型
     *
     * @param filename 文件名
     * @return 对应的媒体类型，未找到返回 null
     */
    public static ResourceMediaType fromFilename(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return null;
        }
        return fromExtension(filename.substring(lastDot));
    }

    /**
     * 根据 MIME 类型获取媒体类型
     *
     * @param mimeType MIME 类型
     * @return 对应的媒体类型，未找到返回 null
     */
    public static ResourceMediaType fromMimeType(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        for (ResourceMediaType type : values()) {
            if (type.mimeType.equalsIgnoreCase(mimeType)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为图片类型
     *
     * @return 是否为图片类型
     */
    public boolean isImage() {
        return "image".equals(type);
    }

    /**
     * 判断是否为视频类型
     *
     * @return 是否为视频类型
     */
    public boolean isVideo() {
        return "video".equals(type);
    }

    /**
     * 判断是否为音频类型
     *
     * @return 是否为音频类型
     */
    public boolean isAudio() {
        return "audio".equals(type);
    }

    /**
     * 判断是否为浏览器可直接预览的类型
     *
     * @return 是否为浏览器可预览类型
     */
    public boolean isBrowserPreviewable() {
        return isImage() || isVideo() || isAudio() || this == PDF || this == TEXT || this == HTML || this == JSON;
    }
}
