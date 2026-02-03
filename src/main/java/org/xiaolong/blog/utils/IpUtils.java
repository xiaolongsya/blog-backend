package org.xiaolong.blog.utils;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {
    /**
     * 提取请求的真实IP（适配反向代理、Nginx转发）
     */
    public static String getRealIp(HttpServletRequest request) {
        // 1. 优先从 X-Real-IP 请求头获取（Nginx 等反向代理常用）
        String realIp = request.getHeader("X-Real-IP");
        // 2. 若不存在，从 X-Forwarded-For 请求头获取（多个代理时，第一个为真实IP）
        if (realIp == null || realIp.length() == 0 || "unknown".equalsIgnoreCase(realIp)) {
            realIp = request.getHeader("X-Forwarded-For");
        }
        // 3. 若仍不存在，获取原生的 remoteAddr
        if (realIp == null || realIp.length() == 0 || "unknown".equalsIgnoreCase(realIp)) {
            realIp = request.getRemoteAddr();
            // 本地环境默认返回 0:0:0:0:0:0:0:1，可替换为 127.0.0.1 方便测试
            if ("0:0:0:0:0:0:0:1".equals(realIp)) {
                realIp = "127.0.0.1";
            }
        }
        // 4. 处理 X-Forwarded-For 多个IP的情况（格式：真实IP, 代理IP1, 代理IP2）
        if (realIp != null && realIp.contains(",")) {
            realIp = realIp.split(",")[0].trim();
        }
        return realIp;
    }
}
