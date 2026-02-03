package org.xiaolong.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xiaolong.blog.interceptor.RateLimitInterceptor;
import org.xiaolong.blog.interceptor.XssFilterInterceptor;

@Configuration
// 可选：重命名为 WebConfig，更符合当前功能（仅配置拦截器和全局web设置）
// public class WebConfig implements WebMvcConfigurer {
public class WebConfig implements WebMvcConfigurer {

    /**
     * 注册拦截器：仅保留 XSS 过滤和限流，彻底删除 IP 白名单相关代码
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 0. 全局XSS过滤拦截器（保留，保护所有接口防XSS攻击）
        registry.addInterceptor(new XssFilterInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .order(0);

        // 1. 彻底删除 IP 白名单拦截器的所有注册代码，无任何残留

        // 2. 限流拦截器（完整保留，核心需求不变，正常生效）
        registry.addInterceptor(new RateLimitInterceptor())
                .addPathPatterns(
                        "/growth/upload",
                        "/development/upload",
                        "/node/upload",
                        "/growth/update"
                )
                .excludePathPatterns("/error")
                .order(1); // 可选：将 order 改为 1（因为删除了 IP 拦截器，顺序更合理，不改也不影响功能）
    }
}