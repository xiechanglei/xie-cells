package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.net.MalformedURLException;

/**
 * 资源说明类，用于封装 Web 响应中的资源信息。
 * <p>
 * 该类提供了资源的元数据信息，包括资源对象、资源名称、媒体类型、分段大小等，
 * 主要用于文件下载、媒体资源预览等场景。支持链式调用和流式 API。
 * </p>
 *
 * @author xie
 * @date 2026/2/12
 */
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class ResourceInfo {

    /**
     * 资源对象，可以是文件、字节数组、URL 等
     */
    Resource resource;

    /**
     * 资源名称，通常用于下载时的文件名
     */
    private String resourceName;

    /**
     * 资源的媒体类型，用于设置响应的 Content-Type
     */
    private ResourceMediaType mediaType;

    /**
     * 分段下载的大小阈值，默认为 10MB
     * 当文件大小超过此值时，自动启用范围请求支持
     */
    private long rangeSize = 1024 * 1024 * 10;

    /**
     * 是否以内联方式展示资源，默认为 true
     * true 表示浏览器尝试直接展示（预览），false 表示下载
     */
    private boolean inline = true;

    /**
     * 默认构造函数
     */
    public ResourceInfo() {
    }

    /**
     * 设置资源名称并自动推断媒体类型
     *
     * @param resourceName 资源名称（文件名）
     * @return 当前资源说明对象，支持链式调用
     */
    public ResourceInfo resourceName(String resourceName) {
        this.resourceName = resourceName;
        this.mediaType(ResourceMediaType.fromFilename(resourceName));
        return this;
    }


    /**
     * 使用文件创建资源说明，适用于服务器上的文件输出
     *
     * @param file 文件对象，通常是服务器上的文件
     * @return 资源说明对象，包含文件资源和默认的媒体类型（根据文件扩展名推断）
     */
    public static ResourceInfo withFile(File file) {
        ResourceInfo resourceInfo = new ResourceInfo();
        if (file != null) {
            return resourceInfo.resource(new FileSystemResource(file)).resourceName(file.getName());
        }
        return resourceInfo;
    }

    /**
     * 使用文件路径创建资源说明，适用于服务器上的文件输出
     *
     * @param filePath 文件路径，通常是服务器上的绝对路径或相对路径
     * @return 资源说明对象，包含文件资源和默认的媒体类型（根据文件扩展名推断）
     */
    public static ResourceInfo withFilePath(String filePath) {
        if (filePath == null) {
            return new ResourceInfo();
        }
        return withFile(new File(filePath));
    }


    /**
     * 使用字节数组创建资源说明，适用于内存中的数据输出
     *
     * @param bytes 字节数组，通常用于生成的文件内容
     * @return 资源说明对象，包含字节数组资源和默认的媒体类型（application/octet-stream）
     */
    public static ResourceInfo withBytes(byte[] bytes) {
        ResourceInfo resourceInfo = new ResourceInfo();
        if (bytes != null) {
            resourceInfo.resource(new ByteArrayResource(bytes));
        }
        return resourceInfo;
    }

    /**
     * 使用 URL 创建资源说明，适用于网络资源输出
     * <p>
     * todo ，这里需要测试真是的url是否能够正常工作
     *
     * @param url URL 地址，通常是一个有效的网络资源链接
     * @return 资源说明对象，包含 URL 资源和默认的媒体类型（根据 URL 的文件扩展名推断）
     * @throws MalformedURLException 如果提供的 URL 无效或格式错误，将抛出此异常
     */
    public static ResourceInfo withUrl(String url) throws MalformedURLException {
        ResourceInfo resourceInfo = new ResourceInfo();
        if (url != null) {
            resourceInfo.resource(new UrlResource(url));
        }
        return resourceInfo;
    }
}
