package org.xiaolong.blog.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存限流拦截器（无需Redis，适合单机部署）
 * 规则：每个IP每分钟最多提交2条评论
 */
public class RateLimitInterceptor implements HandlerInterceptor {

    // 限流规则配置
    private static final int LIMIT_COUNT = 5; // 单位时间内最大请求数
    private static final long LIMIT_TIME = 60 * 1000; // 单位时间：60秒（1分钟）

    // 本地缓存：key=IP+接口路径，value=限流信息（调用次数+过期时间）
    private final Map<String, LimitInfo> limitCache = new ConcurrentHashMap<>();

    /**
     * 请求处理前校验限流规则
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取真实客户端IP
        String clientIp = request.getHeader("X-Real-IP");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        // 2. 构建限流缓存Key（IP+接口路径，确保不同接口独立限流）
        String cacheKey = "rate_limit:comment:" + clientIp;
        long currentTime = System.currentTimeMillis();

        // 3. 校验并更新限流缓存
        LimitInfo limitInfo = limitCache.get(cacheKey);
        if (limitInfo == null) {
            // 第一次请求：初始化缓存，设置过期时间
            limitCache.put(cacheKey, new LimitInfo(1, currentTime + LIMIT_TIME));
            return true;
        }

        if (currentTime > limitInfo.getExpireTime()) {
            // 缓存过期：重置调用次数和过期时间
            limitInfo.setCount(1);
            limitInfo.setExpireTime(currentTime + LIMIT_TIME);
            return true;
        }

        if (limitInfo.getCount() >= LIMIT_COUNT) {
            // 超过限流次数：返回429请求过于频繁（兼容Spring Boot 2.x）
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":429,\"msg\":\"请求过于频繁，请1分钟后再试\"}");
            return false;
        }

        // 4. 未超过限流次数：增加调用次数
        limitInfo.setCount(limitInfo.getCount() + 1);
        return true;
    }

    /**
     * 内部类：存储限流缓存信息（调用次数+过期时间）
     */
    static class LimitInfo {
        private int count; // 已调用次数
        private long expireTime; // 缓存过期时间（时间戳）

        // 构造函数
        public LimitInfo(int count, long expireTime) {
            this.count = count;
            this.expireTime = expireTime;
        }

        // Getter & Setter
        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }
    }
}