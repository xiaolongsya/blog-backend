package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Reply;
import org.xiaolong.blog.service.ReplyService;

@RestController
@RequestMapping("/reply")
public class ReplyController
{
    @Autowired
    private ReplyService replyService;

    //添加回复
    @RequestMapping("/add")
    @Operation(summary = "添加回复", description = "上传一个回复，包含关联的评论，名字，评论内容, 返回回复id")
    public Result<Long> addReply(@RequestBody Reply reply) throws BusinessException
    {
        return Result.success(replyService.addReply(reply),"添加成功，返回回复id");
    }
}
