package org.xiaolong.blog.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义限流注解
 * 作用于方法上，用于覆盖全局默认限流规则
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 单位时间内的最大请求次数
     */
    int count() default 5;

    /**
     * 单位时间（毫秒），默认 60000 毫秒（1分钟）
     */
    long time() default 60 * 1000;
}