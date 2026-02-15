package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Stack;
import org.xiaolong.blog.interceptor.RateLimit;
import org.xiaolong.blog.service.StackService;

import java.util.List;

@RestController
@RequestMapping("/stack")
public class StackController
{
    @Autowired
    private StackService stackService;

    //上传技术栈
    @PostMapping("/upload")
    @Operation(summary = "上传技术栈", description = "传入技术栈，返回id")
    public Result<Long> uploadStack(@RequestBody Stack stack)
    {
        return Result.success(stackService.uploadStack(stack), "上传成功");
    }

    //获取所有技术栈
    @RateLimit(count = 30, time = 60000)
    @GetMapping("/list")
    @Operation(summary = "获取所有技术栈")
    public Result<List<Stack>> listStack()
    {
        return Result.success(stackService.listStacks(), "获取成功");
    }
}
