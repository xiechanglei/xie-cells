# 资源响应工具

本文档介绍 Web Starter 提供的资源响应工具，用于文件下载和媒体资源预览。

## 功能说明

提供统一的资源响应处理方式，支持文件下载、媒体资源预览、范围请求和缓存验证。

## 核心功能

### 1. 文件下载模式

使用 `useResource` 方法，Content-Disposition 为 `attachment`，浏览器会下载文件。

```java
@RestController
@RequestMapping("/api")
public class FileController {

    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable Long id) {
        File file = getFileById(id);
        ResourceInfo info = ResourceInfo.withFile(file);
        return CellResourceResponseHandler.useResource(info);
    }
}
```

### 2. 媒体资源预览模式

使用 `useMedia` 方法，Content-Disposition 为 `inline`，支持浏览器直接展示。

```java
@RestController
@RequestMapping("/api")
public class MediaController {

    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        File file = getImageById(id);
        ResourceInfo info = ResourceInfo.withFile(file);
        return CellResourceResponseHandler.useMedia(info);
    }
}
```

### 3. HTTP 206 范围请求支持

自动支持 HTTP Range 请求头，实现：
- 视频拖拽播放
- 断点续传下载
- 分片加载

**分段下载阈值：**
- 默认阈值：10MB（文件超过 10MB 时自动启用范围请求支持）
- 可自定义阈值：通过方法的 `rangeThreshold` 参数设置

```java
// 使用默认阈值（10MB）
@GetMapping("/video/{id}")
public ResponseEntity<?> getVideo(@PathVariable Long id) {
    File file = getVideoById(id);
    ResourceInfo info = ResourceInfo.withFile(file);
    return CellResourceResponseHandler.useMedia(info);
}

// 自定义阈值（5MB）
@GetMapping("/large-video/{id}")
public ResponseEntity<?> getLargeVideo(@PathVariable Long id) {
    File file = getVideoById(id);
    ResourceInfo info = ResourceInfo.withFile(file);
    return CellResourceResponseHandler.useMedia(info, 5 * 1024 * 1024);
}

// 禁用分段下载（阈值设为 0）
@GetMapping("/small-video/{id}")
public ResponseEntity<?> getSmallVideo(@PathVariable Long id) {
    File file = getVideoById(id);
    ResourceInfo info = ResourceInfo.withFile(file);
    return CellResourceResponseHandler.useMedia(info, 0);
}
```

### 4. HTTP 304 缓存验证

自动处理 If-Modified-Since 头，实现缓存验证。

**工作原理：**
1. 首次请求：服务器返回完整资源，并在响应头中包含 `Last-Modified`
2. 后续请求：浏览器发送 `If-Modified-Since` 头
3. 服务器比较：如果资源未修改，返回 304 Not Modified（无响应体）
4. 如果资源已修改，返回 200 OK 和新资源

## ResourceInfo 创建方式

| 方法 | 说明 | 适用场景 |
|-----|------|---------|
| `withFile(File)` | 使用文件对象 | 服务器上的文件 |
| `withFilePath(String)` | 使用文件路径 | 服务器上的文件路径 |
| `withBytes(byte[])` | 使用字节数组 | 内存中的数据 |
| `withUrl(String)` | 使用 URL | 网络资源 |

## 核心类

| 类名 | 说明 |
|-----|------|
| `CellResourceResponseHandler` | 资源响应辅助类 |
| `ResourceInfo` | 资源说明类 |
| `ResourceMediaType` | 媒体类型枚举 |
| `RangeResource` | 范围资源包装器 |
| `RangeRequestInfo` | 范围请求信息类 |
| `LimitedInputStream` | 限制输入流 |

## 支持的媒体类型

### 图片
JPEG, PNG, GIF, WEBP, SVG, BMP, ICO

### 视频
MP4, WEBM, OGG, MOV, AVI

### 音频
MP3, WAV, OGG, WEBM, AAC, FLAC

### 其他
PDF, TEXT, HTML, XML, JSON

## 前端使用示例

### 视频播放
```html
<video controls>
    <source src="/api/video/123" type="video/mp4">
</video>
```

### 图片预览
```html
<img src="/api/image/123" alt="图片预览">
```

### 文件下载
```javascript
fetch('/api/download/123')
    .then(response => response.blob())
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'filename';
        a.click();
    });
```
