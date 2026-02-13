package org.xiaolong.blog.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ------------ 优先捕获：自定义业务异常（返回最精准的具体信息）------------
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        // 直接返回自定义异常中的错误码和错误信息
        return Result.error(e.getCode(), e.getMessage());
        // 进阶：如果需要返回出错字段，可以扩展 Result 类，添加 field 字段
        // return Result.error(e.getCode(), e.getMessage(), e.getField());
    }

    // ------------ 兜底捕获：其他未知异常（系统异常）------------
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // 开发环境打印堆栈，方便调试
        e.printStackTrace();
        // 生产环境建议隐藏具体详情，返回固定提示
        return Result.error(500, "服务器内部错误，请稍后再试");
    }
}