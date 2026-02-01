package org.xiaolong.blog.common;



import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义业务异常类：专门处理业务逻辑相关的错误（如评论为空、昵称超长、今日评论超限等）
 */
@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException
{
    // 业务错误码（可自定义，如 400=参数错误，403=权限不足，409=数据冲突）
    private Integer code;
    // 出错字段（可选，方便前端精准提示，如 "nickname"、"content"）
    private String field;

    /**
     * 构造方法：携带错误码和错误信息
     */
    public BusinessException(Integer code, String message)
    {
        // 调用父类构造方法，传递错误信息（getMessage() 可获取）
        super(message);
        this.code = code;
    }

    /**
     * 构造方法：携带错误码、错误信息、出错字段（更精准）
     */
    public BusinessException(Integer code, String message, String field)
    {
        super(message);
        this.code = code;
        this.field = field;
    }

    // ------------ 静态快捷构建方法（简化使用，不用手动 new）------------
    /**
     * 参数错误（最常用，如昵称为空、评论超长）
     */
    public static BusinessException paramError(String message)
    {
        return new BusinessException(400, message);
    }

    /**
     * 参数错误（带具体字段）
     */
    public static BusinessException paramError(String message, String field)
    {
        return new BusinessException(400, message, field);
    }

    /**
     * 业务冲突（如数据重复、今日评论超限）
     */
    public static BusinessException conflictError(String message)
    {
        return new BusinessException(409, message);
    }

    /**
     * 业务操作失败（如插入数据失败、删除失败）
     */
    public static BusinessException operateFail(String message)
    {
        return new BusinessException(500, message);
    }
}