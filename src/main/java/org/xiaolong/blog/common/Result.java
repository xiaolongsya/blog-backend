package org.xiaolong.blog.common;


import lombok.Data;

/**
 * 全局统一返回结果类
 */
@Data
public class Result<T> {
    // 响应码（200=成功，500=异常，400=参数错误）
    private Integer code;
    // 响应信息（成功/失败提示）
    private String msg;
    // 响应数据（正常返回时携带的数据，异常时可null）
    private T data;

    //支持自定义文字
    public static <T> Result<T> success(T data,String msg)
    {
        return new Result<>(200, msg, data);
    }
    // 静态方法：快速构建成功返回结果（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 静态方法：快速构建成功返回结果（不带数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 静态方法：快速构建失败返回结果
    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    // 构造方法（私有，只能通过静态方法构建）
    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
