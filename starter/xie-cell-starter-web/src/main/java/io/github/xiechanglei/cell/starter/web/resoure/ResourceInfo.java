package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;

/**
 * 资源说明
 *
 * @author xie
 * @date 2026/2/12
 */

@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
@Getter
@Setter
public class ResourceInfo {
    Resource resource;
    private String resourceName;

    /**
     * 使用文件创建资源说明
     *
     * @param file 资源文件
     * @return 资源说明
     */
    public static ResourceInfo withFile(File file) {
        ResourceInfo resourceInfo = new ResourceInfo();
        if (file != null) {
            resourceInfo.resource(new FileSystemResource(file)).resourceName(file.getName());
        }
        return resourceInfo;
    }

    /**
     * 使用文件路径创建资源说明
     *
     * @param filePath 资源文件路径
     * @return 资源说明
     */
    public static ResourceInfo withFilePath(String filePath) {
        if (filePath == null) {
            return new ResourceInfo();
        }
        return withFile(new File(filePath));
    }

    public static void main(String[] args) {
    }
}
