// 新增：全局XSS过滤拦截器（interceptor/XssFilterInterceptor.java）
package org.xiaolong.blog.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.xiaolong.blog.utils.XssUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

public class XssFilterInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 过滤URL参数
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            if (paramValue != null) {
                request.setAttribute(paramName, XssUtils.cleanXss(paramValue));
            }
        }

        // 过滤JSON请求体（需包装request，这里简化示例，实际可使用HttpServletRequestWrapper）
        // 若你的项目用@RequestBody接收参数，可结合Jackson自定义序列化器实现自动过滤

        return true;
    }
}