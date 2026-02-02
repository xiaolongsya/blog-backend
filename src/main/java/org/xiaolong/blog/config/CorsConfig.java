package org.xiaolong.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有外部前端地址访问，适配其他用户访问你的主页场景
                .allowedOriginPatterns("*")
                // 允许所有HTTP请求方法（GET/POST等，保证接口请求正常）
                .allowedMethods("*")
                // 允许请求头携带自定义字段（后续扩展如Token也能兼容）
                .allowedHeaders("*")
                // 允许携带凭证（可选，不影响当前图文展示需求）
                .allowCredentials(true)
                // 预检请求缓存1小时，优化访问速度
                .maxAge(3600);
    }
}