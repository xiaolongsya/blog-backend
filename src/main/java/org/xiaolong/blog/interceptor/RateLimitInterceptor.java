package org.xiaolong.blog.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地缓存限流拦截器（无需Redis，适合单机部署）
 * 支持全局默认限流 + 注解单独配置限流
 */
public class RateLimitInterceptor implements HandlerInterceptor {

    // ========== 核心改造：全局默认限流规则 ==========
    // 默认：1分钟内最多访问5次
    private final RateLimitRule defaultRule = new RateLimitRule(5, 60 * 1000);

    // 本地缓存：key=IP+接口路径，value=限流信息
    private final Map<String, LimitInfo> limitCache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 如果拦截到的不是 Controller 方法（比如静态资源），直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 2. 获取真实客户端IP
        String clientIp = request.getHeader("X-Real-IP");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        // 3. 获取当前请求路径
        String requestPath = request.getRequestURI();

        // 4. 动态获取限流规则（核心逻辑）
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RateLimit rateLimitAnnotation = handlerMethod.getMethodAnnotation(RateLimit.class);

        RateLimitRule rule;
        if (rateLimitAnnotation != null) {
            // 如果方法上有 @RateLimit 注解，按注解配置来
            rule = new RateLimitRule(rateLimitAnnotation.count(), rateLimitAnnotation.time());
        } else {
            // 如果没有注解，使用全局默认规则
            rule = defaultRule;
        }

        // 5. 构建限流缓存Key
        String cacheKey = "rate_limit:" + requestPath + ":" + clientIp;
        long currentTime = System.currentTimeMillis();

        // 6. 校验并更新限流缓存
        LimitInfo limitInfo = limitCache.get(cacheKey);
        if (limitInfo == null) {
            limitCache.put(cacheKey, new LimitInfo(1, currentTime + rule.getLimitTime()));
            return true;
        }

        if (currentTime > limitInfo.getExpireTime()) {
            limitInfo.getCount().set(1); // 使用 AtomicInteger 重置
            limitInfo.setExpireTime(currentTime + rule.getLimitTime());
            return true;
        }

        // 判断是否超过限流次数
        if (limitInfo.getCount().get() >= rule.getLimitCount()) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            String msg = String.format("请求过于频繁，请%d秒后再试", rule.getLimitTime() / 1000);
            response.getWriter().write("{\"code\":429,\"msg\":\"" + msg + "\"}");
            return false;
        }

        // 未超过限流次数：增加调用次数 (线程安全递增)
        limitInfo.getCount().incrementAndGet();
        return true;
    }

    // ========== 内部类：限流规则封装 ==========
    static class RateLimitRule {
        private int limitCount;
        private long limitTime;

        public RateLimitRule(int limitCount, long limitTime) {
            this.limitCount = limitCount;
            this.limitTime = limitTime;
        }
        public int getLimitCount() { return limitCount; }
        public long getLimitTime() { return limitTime; }
    }

    // ========== 内部类：限流缓存信息 ==========
    static class LimitInfo {
        private AtomicInteger count; // 改用 AtomicInteger 保证并发自增的安全
        private long expireTime;

        public LimitInfo(int count, long expireTime) {
            this.count = new AtomicInteger(count);
            this.expireTime = expireTime;
        }
        public AtomicInteger getCount() { return count; }
        public long getExpireTime() { return expireTime; }
        public void setExpireTime(long expireTime) { this.expireTime = expireTime; }
    }
}