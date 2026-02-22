package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.Getter;

@Getter
public enum ResourceMediaType {

    JPEG("image", "jpeg", "image/jpeg", ".jpg", ".jpeg", ".jpe"),
    PNG("image", "png", "image/png", ".png"),
    GIF("image", "gif", "image/gif", ".gif"),
    WEBP("image", "webp", "image/webp", ".webp"),
    SVG("image", "svg+xml", "image/svg+xml", ".svg"),
    BMP("image", "bmp", "image/bmp", ".bmp"),
    ICO("image", "x-icon", "image/x-icon", ".ico"),

    MP4("video", "mp4", "video/mp4", ".mp4", ".m4v"),
    WEBM("video", "webm", "video/webm", ".webm"),
    OGG_VIDEO("video", "ogg", "video/ogg", ".ogv"),
    QUICKTIME("video", "quicktime", "video/quicktime", ".mov"),
    AVI("video", "x-msvideo", "video/x-msvideo", ".avi"),

    MP3("audio", "mpeg", "audio/mpeg", ".mp3"),
    WAV("audio", "wav", "audio/wav", ".wav"),
    OGG_AUDIO("audio", "ogg", "audio/ogg", ".oga", ".ogg"),
    WEBM_AUDIO("audio", "webm", "audio/webm", ".weba"),
    AAC("audio", "aac", "audio/aac", ".aac"),
    FLAC("audio", "flac", "audio/flac", ".flac"),

    PDF("application", "pdf", "application/pdf", ".pdf"),
    TEXT("text", "plain", "text/plain", ".txt"),
    HTML("text", "html", "text/html", ".html", ".htm"),
    XML("text", "xml", "text/xml", ".xml"),
    JSON("application", "json", "application/json", ".json"),

    NONE("application", "octet-stream", "application/octet-stream");

    private final String type;
    private final String subtype;
    private final String mimeType;
    private final String[] extensions;

    ResourceMediaType(String type, String subtype, String mimeType, String... s) {
        this.type = type;
        this.subtype = subtype;
        this.mimeType = mimeType;
        this.extensions = s;
    }

    public static ResourceMediaType fromExtension(String extension) {
        if (extension == null) {
            return NONE;
        }
        String ext = extension.startsWith(".") ? extension.toLowerCase() : "." + extension.toLowerCase();
        for (ResourceMediaType type : values()) {
            for (String t : type.extensions) {
                if (t.equals(ext)) {
                    return type;
                }
            }
        }
        return NONE;
    }

    public static ResourceMediaType fromFilename(String filename) {
        if (filename == null) {
            return NONE;
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return NONE;
        }
        return fromExtension(filename.substring(lastDot));
    }

    public static ResourceMediaType fromMimeType(String mimeType) {
        if (mimeType == null) {
            return NONE;
        }
        for (ResourceMediaType type : values()) {
            if (type.mimeType.equalsIgnoreCase(mimeType)) {
                return type;
            }
        }
        return NONE;
    }

    public boolean isImage() {
        return "image".equals(type);
    }

    public boolean isVideo() {
        return "video".equals(type);
    }

    public boolean isAudio() {
        return "audio".equals(type);
    }

    public boolean isBrowserPreviewable() {
        return isImage() || isVideo() || isAudio() || this == PDF || this == TEXT || this == HTML || this == JSON;
    }
}
