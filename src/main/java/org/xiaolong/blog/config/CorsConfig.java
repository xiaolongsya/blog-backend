package org.xiaolong.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类，仅允许指定域名调用评论接口
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 配置跨域规则（最小权限原则，仅开放必要域名和方法）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 全局匹配所有接口
                .allowedOrigins("http://localhost:8083", "http://124.221.211.146", "https://xiaolongya.cn","http://localhost:5173" )
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 开放常用请求方法
                .allowCredentials(true)
                .maxAge(3600);
    }
}