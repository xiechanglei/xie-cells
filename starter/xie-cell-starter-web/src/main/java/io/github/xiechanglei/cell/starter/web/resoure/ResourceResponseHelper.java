package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 资源响应辅助类,一般做为web请求中的文件输出
 *
 * @author xie
 * @date 2026/2/12
 */
public class ResourceResponseHelper {
    public static ResponseEntity<Resource> useResource(ResourceInfo info) {
        try {
            Resource resource = info.resource();
            if (resource != null && !resource.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(getResourceName(info), StandardCharsets.UTF_8) + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            return new ResponseEntity<>(info.resource(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取资源名称，如果资源说明中没有设置资源名称，则使用默认的资源名称
     *
     * @param info 资源说明
     * @return 资源名称
     */
    private static @NonNull String getResourceName(ResourceInfo info) {
        String resourceName = info.resourceName();
        if (resourceName == null) {
            resourceName = "UnknownNameResource";
        }
        return resourceName;
    }
}
