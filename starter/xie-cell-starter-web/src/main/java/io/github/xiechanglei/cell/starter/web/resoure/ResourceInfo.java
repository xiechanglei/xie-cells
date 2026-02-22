package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class ResourceInfo {

    Resource resource;
    private String resourceName;
    private ResourceMediaType mediaType;
    private long rangeSize= 1024 * 1024 * 10;
    private boolean inline = true;

    public ResourceInfo() {
    }

    public static ResourceInfo withFile(File file) {
        ResourceInfo resourceInfo = new ResourceInfo();
        if (file != null) {
            resourceInfo.resource(new FileSystemResource(file))
                    .resourceName(file.getName())
                    .mediaType(ResourceMediaType.fromFilename(file.getName()));
        }
        return resourceInfo;
    }

    public static ResourceInfo withFilePath(String filePath) {
        if (filePath == null) {
            return new ResourceInfo();
        }
        return withFile(new File(filePath));
    }
}
