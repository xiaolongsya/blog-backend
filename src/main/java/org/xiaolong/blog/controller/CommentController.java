package org.xiaolong.blog.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Comment;
import org.xiaolong.blog.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController
{
    @Autowired
    private CommentService commentService;

    //上传评论接口
    @PostMapping("/upload")
    @Operation(summary = "上传评论接口", description = "需要上传昵称和评论内容")
    public Result<Long> uploadComment(@RequestBody Comment comment) throws BusinessException
    {
        if(comment.getName() == null || comment.getName().trim().isEmpty())
        {
            throw new BusinessException(400, "昵称不能为空");
        }
        if(comment.getName().length() > 40)
        {
            throw new BusinessException(400, "昵称不能超过40个字符");
        }
        if(comment.getContent() == null || comment.getContent().trim().isEmpty())
        {
            throw new BusinessException(400, "内容不能为空");
        }
        if(comment.getContent().length() > 400)
        {
            throw new BusinessException(400, "内容不能超过400个字符");
        }
        return Result.success(commentService.uploadComment(comment), "上传成功");
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
