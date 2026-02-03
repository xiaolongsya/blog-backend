package org.xiaolong.blog.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Comment;
import org.xiaolong.blog.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController
{
    @Autowired
    private CommentService commentService;

    // 上传评论接口
    @PostMapping("/upload")
    @Operation(summary = "上传评论接口", description = "需要上传昵称和评论内容，每IP每小时限5次")
    public Result<Long> uploadComment(@RequestBody Comment comment, HttpServletRequest request) throws BusinessException {
        // 调用服务层方法，传入comment和request（用于获取真实IP）
        Long commentId = commentService.uploadComment(comment, request);
        return Result.success(commentId, "上传成功");
    }
    //查询评论接口
    @GetMapping("/list")
    @Operation(summary = "查询评论接口(按时间倒序)")
    public Result<List<Comment>> listCommentsByTimeDesc() throws BusinessException
    {
        List<Comment> comments = commentService.listCommentsByTimeDesc();
        return Result.success(comments, "查询成功");
    }
}
