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

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class ResourceInfo {

    Resource resource;
    private String resourceName;
    private ResourceMediaType mediaType;
    private long rangeSize = 1024 * 1024 * 10;
    private boolean inline = true;

    public ResourceInfo() {
    }

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
