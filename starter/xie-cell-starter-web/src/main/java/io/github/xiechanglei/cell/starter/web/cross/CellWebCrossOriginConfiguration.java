package io.github.xiechanglei.cell.starter.web.cross;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>
 * 配置跨域,此功能主要用于解决前后端分离项目中的跨域问题,
 * 通常在成熟的前端开发框架中,如Vue,React,Angular等,都会有自己的跨域解决方案,
 * 实际的产品部署中也会在Nginx等反向代理服务器中进行跨域配置,
 * 此功能主要主要用于在一些需要跨域的场景中使用,默认是开启的,支持自定义关闭,
 * 支持在application.properties或application.yml中进行自定义配置,
 * 具体的配置参数参照{@link CellWebCrossConfigProperties}
 * </pre>
 * <p>
 * 如果此跨域功能不能满足对应的需求,可以关闭此功能,使用其他方式解决跨域问题
 */
@Log4j2
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cell.web.cross", name = "enable", havingValue = "true", matchIfMissing = true)
public class CellWebCrossOriginConfiguration implements WebMvcConfigurer {
    /**
     * 获取用户自定义的跨域配置参数
     */
    private final CellWebCrossConfigProperties cellWebCrossConfigProperties;

    /**
     * 跨域功能实现,在spring boot 2.4.0之后,WebMvcConfigurerAdapter已经被废弃,
     * 通过实现WebMvcConfigurer接口来实现跨域功能
     *
     * <pre>
     * 该方法用于配置跨域访问的参数
     * 1. addMapping: 配置可以被跨域的路径,可以任意配置,可以具体到直接请求路径
     * 2. allowedMethods: 允许的请求方式,如GET,POST,PUT,DELETE等
     * 3. allowedOrigins: 允许的访问源,如http://www.baidu.com,如果是*则表示所有源
     * 4. allowedHeaders: 允许的请求头,如X-Requested-With,Content-Type,Authorization等
     * 5. allowCredentials: 是否允许发送Cookie,默认为false
     * 6. maxAge: 预检请求的有效期,单位为秒,有效期内不需要预检请求
     * </pre>
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("自动开启跨域配置，关闭请设置: cell.web.cross.enable=false");
        registry.addMapping(cellWebCrossConfigProperties.getMapping())
                .allowedMethods(cellWebCrossConfigProperties.getMethods())
                .allowedOrigins(cellWebCrossConfigProperties.getOrigins())
                .allowedHeaders(cellWebCrossConfigProperties.getHeaders())
                .allowCredentials(cellWebCrossConfigProperties.isAllowCredentials())
                .maxAge(cellWebCrossConfigProperties.getMaxAge());
    }
}
