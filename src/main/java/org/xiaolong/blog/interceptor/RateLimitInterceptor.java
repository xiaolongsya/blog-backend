package org.xiaolong.blog.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存限流拦截器（无需Redis，适合单机部署）
 * 支持不同接口路径配置独立的限流规则
 */
public class RateLimitInterceptor implements HandlerInterceptor {

    // ========== 核心改造：按路径配置不同限流规则 ==========
    // 限流规则映射：key=接口路径，value=该路径的限流规则
    private final Map<String, RateLimitRule> pathRuleMap = new ConcurrentHashMap<>();

    // 初始化不同路径的限流规则（可根据需求调整）
    public RateLimitInterceptor() {
        pathRuleMap.put("/comment/submit", new RateLimitRule(5, 60 * 1000));
        //图片单独设置，一分钟十次
        pathRuleMap.put("/upload/image", new RateLimitRule(10, 60 * 1000));
        pathRuleMap.put("/growth/upload", new RateLimitRule(5, 60 * 1000));
        pathRuleMap.put("/development/upload", new RateLimitRule(5, 60 * 1000));
        pathRuleMap.put("/node/upload", new RateLimitRule(5, 60 * 1000));
        pathRuleMap.put("/growth/update", new RateLimitRule(5, 60 * 1000));
    }

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

        // 2. 获取当前请求路径（核心：区分不同接口）
        String requestPath = request.getRequestURI();
        // 匹配当前路径的限流规则（如果没有配置规则，直接放行）
        RateLimitRule rule = pathRuleMap.get(requestPath);
        if (rule == null) {
            return true;
        }

        // 3. 构建限流缓存Key（IP+接口路径，确保不同接口独立限流）
        String cacheKey = "rate_limit:" + requestPath + ":" + clientIp;
        long currentTime = System.currentTimeMillis();

        // 4. 校验并更新限流缓存（复用原有逻辑，只是规则从常量改为动态获取）
        LimitInfo limitInfo = limitCache.get(cacheKey);
        if (limitInfo == null) {
            // 第一次请求：初始化缓存，设置过期时间
            limitCache.put(cacheKey, new LimitInfo(1, currentTime + rule.getLimitTime()));
            return true;
        }

        if (currentTime > limitInfo.getExpireTime()) {
            // 缓存过期：重置调用次数和过期时间
            limitInfo.setCount(1);
            limitInfo.setExpireTime(currentTime + rule.getLimitTime());
            return true;
        }

        if (limitInfo.getCount() >= rule.getLimitCount()) {
            // 超过限流次数：返回429请求过于频繁
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            // 提示信息区分不同接口（可选，更友好）
            String msg = String.format("请求过于频繁，请%d秒后再试", rule.getLimitTime() / 1000);
            response.getWriter().write("{\"code\":429,\"msg\":\"" + msg + "\"}");
            return false;
        }

        // 5. 未超过限流次数：增加调用次数
        limitInfo.setCount(limitInfo.getCount() + 1);
        return true;
    }

    // ========== 新增内部类：限流规则封装 ==========
    static class RateLimitRule {
        private int limitCount; // 单位时间内最大请求数
        private long limitTime; // 单位时间（毫秒）

        public RateLimitRule(int limitCount, long limitTime) {
            this.limitCount = limitCount;
            this.limitTime = limitTime;
        }

        // Getter
        public int getLimitCount() {
            return limitCount;
        }

        public long getLimitTime() {
            return limitTime;
        }
    }

    // ========== 原有内部类：限流缓存信息 ==========
    static class LimitInfo {
        private int count; // 已调用次数
        private long expireTime; // 缓存过期时间（时间戳）

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